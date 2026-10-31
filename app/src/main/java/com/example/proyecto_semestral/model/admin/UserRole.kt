package com.example.proyecto_semestral.model.admin

enum class UserRole {
    USER,
    ADMIN
}

fun roleForEmail(email: String): UserRole {
    val normalizedEmail = email.trim().lowercase()
    val parts = normalizedEmail.split('@')
    return if (parts.size == 2 && parts[0].isNotBlank() && parts[1] == ADMIN_DOMAIN) {
        UserRole.ADMIN
    } else {
        UserRole.USER
    }
}

fun String.isAdminEmail(): Boolean = roleForEmail(this) == UserRole.ADMIN

private const val ADMIN_DOMAIN = "admin.cl"
