package com.example.proyecto_semestral.data.repository

import com.example.proyecto_semestral.data.dto.auth.AuthResponseDto
import com.example.proyecto_semestral.data.dto.auth.LoginRequestDto
import com.example.proyecto_semestral.data.dto.auth.RegisterRequestDto
import com.example.proyecto_semestral.data.local.AuthTokenStore
import com.example.proyecto_semestral.data.mapper.auth.toModel
import com.example.proyecto_semestral.data.remote.AuthApi
import com.example.proyecto_semestral.domain.repository.AuthException
import com.example.proyecto_semestral.domain.repository.AuthRepository
import com.example.proyecto_semestral.model.auth.User
import com.google.gson.JsonParser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import java.io.IOException

class AuthRepositoryImpl(
    private val tokenStore: AuthTokenStore,
    private val api: AuthApi
) : AuthRepository {
    override val hasStoredSession: Flow<Boolean> = tokenStore.token.map { it != null }

    override suspend fun register(name: String, email: String, password: String): User =
        apiCall {
            persistSession(
                api.register(
                    RegisterRequestDto(
                        nombre = name.trim(),
                        email = email.trim().lowercase(),
                        password = password
                    )
                )
            )
        }

    override suspend fun login(email: String, password: String): User =
        apiCall {
            persistSession(
                api.login(
                    LoginRequestDto(
                        email = email.trim().lowercase(),
                        password = password
                    )
                )
            )
        }

    override suspend fun restoreSession(): User? {
        if (tokenStore.getToken() == null) return null
        return try {
            api.me().toModel()
        } catch (exception: HttpException) {
            if (exception.code() == 401) tokenStore.clearToken()
            throw exception.toAuthException()
        } catch (_: IOException) {
            throw AuthException("No se pudo conectar con el servidor. Revisa tu conexión.")
        }
    }

    override suspend fun logout() {
        tokenStore.clearToken()
    }

    private suspend fun persistSession(response: AuthResponseDto): User {
        tokenStore.saveToken(response.accessToken)
        return response.usuario.toModel()
    }

    private suspend fun <T> apiCall(block: suspend () -> T): T =
        try {
            block()
        } catch (exception: HttpException) {
            throw exception.toAuthException()
        } catch (_: IOException) {
            throw AuthException("No se pudo conectar con el servidor. Revisa tu conexión.")
        }

    private fun HttpException.toAuthException(): AuthException {
        val serverMessage = runCatching {
            val body = response()?.errorBody()?.string().orEmpty()
            val json = JsonParser.parseString(body).asJsonObject
            sequenceOf("message", "mensaje", "detail", "error")
                .mapNotNull { key ->
                    json.get(key)?.takeIf { it.isJsonPrimitive }?.asString
                }
                .firstOrNull { it.isNotBlank() }
        }.getOrNull()

        val fallback = when (code()) {
            400 -> "Los datos ingresados no son válidos."
            401 -> "El correo o la contraseña son incorrectos."
            403 -> "No tienes autorización para realizar esta acción."
            409 -> "Ya existe una cuenta asociada a ese correo."
            in 500..599 -> "El servidor no está disponible. Inténtalo más tarde."
            else -> "No se pudo completar la solicitud."
        }
        return AuthException(serverMessage ?: fallback)
    }
}
