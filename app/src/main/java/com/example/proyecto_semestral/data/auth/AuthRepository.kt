package com.example.proyecto_semestral.data.auth

import com.example.proyecto_semestral.data.remote.AuthApi
import com.example.proyecto_semestral.data.remote.NetworkProvider
import com.google.gson.JsonParser
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException
import java.io.IOException

class AuthRepository(
    private val tokenStore: AuthTokenStore,
    private val api: AuthApi = NetworkProvider.createRetrofit(tokenStore)
        .create(AuthApi::class.java)
) {
    val token: Flow<String?> = tokenStore.token

    suspend fun register(nombre: String, email: String, password: String): UserDto =
        apiCall {
            val response = api.register(
                RegisterRequestDto(
                    nombre = nombre.trim(),
                    email = email.trim().lowercase(),
                    password = password
                )
            )
            persistSession(response)
        }

    suspend fun login(email: String, password: String): UserDto =
        apiCall {
            val response = api.login(
                LoginRequestDto(
                    email = email.trim().lowercase(),
                    password = password
                )
            )
            persistSession(response)
        }

    suspend fun restoreSession(): UserDto? {
        if (tokenStore.getToken() == null) return null
        return try {
            api.me()
        } catch (exception: HttpException) {
            if (exception.code() == 401) tokenStore.clearToken()
            throw exception.toAuthException()
        } catch (_: IOException) {
            throw AuthException("No se pudo conectar con el servidor. Revisa tu conexión.")
        }
    }

    suspend fun logout() {
        tokenStore.clearToken()
    }

    private suspend fun persistSession(response: AuthResponseDto): UserDto {
        tokenStore.saveToken(response.accessToken)
        return response.usuario
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

class AuthException(message: String) : Exception(message)
