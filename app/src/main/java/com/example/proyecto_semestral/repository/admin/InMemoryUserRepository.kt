// Gestiona en memoria el registro, acceso y mantenimiento de los usuarios.
package com.example.proyecto_semestral.repository.admin

import com.example.proyecto_semestral.model.admin.AppUser
import com.example.proyecto_semestral.model.admin.hasReservedAdminDomain
import java.util.UUID
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

object InMemoryUserRepository {

    private val _users = MutableStateFlow(predefinedAdmins)
    val users: StateFlow<List<AppUser>> = _users.asStateFlow()

    @Synchronized
    fun register(
        name: String,
        email: String,
        password: String,
        businessName: String = "",
        businessType: String = "",
        businessSize: String = ""
    ): AppUser? {
        val normalizedEmail = email.normalizeEmail()
        if (
            normalizedEmail.hasReservedAdminDomain() ||
            _users.value.any { it.email == normalizedEmail }
        ) return null

        val user = AppUser(
            id = UUID.randomUUID().toString(),
            name = name.trim(),
            email = normalizedEmail,
            password = password,
            businessName = businessName.trim(),
            businessType = businessType,
            businessSize = businessSize
        )
        _users.update { current -> current + user }
        return user
    }

    fun authenticate(email: String, password: String): LoginResult {
        val user = _users.value.firstOrNull {
            it.email == email.normalizeEmail() && it.password == password
        } ?: return LoginResult.InvalidCredentials

        return if (user.isBlocked) {
            LoginResult.Blocked
        } else {
            LoginResult.Success(user)
        }
    }

    fun findById(id: String): AppUser? = _users.value.firstOrNull { it.id == id }

    @Synchronized
    fun updateProfile(id: String, name: String, email: String): Boolean {
        val normalizedEmail = email.normalizeEmail()
        val currentUser = findById(id) ?: return false
        if (
            normalizedEmail.hasReservedAdminDomain() &&
            normalizedEmail != currentUser.email
        ) return false
        if (_users.value.any { it.id != id && it.email == normalizedEmail }) return false

        var updated = false
        _users.update { current ->
            current.map { user ->
                if (user.id == id) {
                    updated = true
                    user.copy(name = name.trim(), email = normalizedEmail)
                } else {
                    user
                }
            }
        }
        return updated
    }

    fun setBlocked(id: String, blocked: Boolean) {
        _users.update { current ->
            current.map { user ->
                if (user.id == id) user.copy(isBlocked = blocked) else user
            }
        }
    }

    fun delete(id: String) {
        _users.update { current -> current.filterNot { it.id == id } }
    }

    private fun String.normalizeEmail(): String = trim().lowercase()
}

sealed interface LoginResult {
    data class Success(val user: AppUser) : LoginResult
    data object InvalidCredentials : LoginResult
    data object Blocked : LoginResult
}

private const val ADMIN_PASSWORD = "123123"

private val predefinedAdmins = listOf(
    AppUser(
        id = "admin-matias",
        name = "Matías",
        email = "matias@admin.cl",
        password = ADMIN_PASSWORD
    ),
    AppUser(
        id = "admin-pedro",
        name = "Pedro",
        email = "pedro@admin.cl",
        password = ADMIN_PASSWORD
    ),
    AppUser(
        id = "admin-cristobal",
        name = "Cristóbal",
        email = "cristobal@admin.cl",
        password = ADMIN_PASSWORD
    )
)
