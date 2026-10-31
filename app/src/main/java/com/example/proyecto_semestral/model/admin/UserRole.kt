// Determina el rol usando exclusivamente la lista autorizada de administradores.
package com.example.proyecto_semestral.model.admin

enum class UserRole {
    USER,
    ADMIN
}

fun roleForEmail(email: String): UserRole {
    val normalizedEmail = email.trim().lowercase()
    return if (normalizedEmail in ADMIN_EMAILS) {
        UserRole.ADMIN
    } else {
        UserRole.USER
    }
}

fun String.isAdminEmail(): Boolean = roleForEmail(this) == UserRole.ADMIN

fun String.hasReservedAdminDomain(): Boolean {
    val normalizedEmail = trim().lowercase()
    val parts = normalizedEmail.split('@')
    return parts.size == 2 && parts[0].isNotBlank() && parts[1] == ADMIN_DOMAIN
}

val authorizedAdminEmails: Set<String>
    get() = ADMIN_EMAILS

private const val ADMIN_DOMAIN = "admin.cl"

private val ADMIN_EMAILS = setOf(
    "matias@admin.cl",
    "pedro@admin.cl",
    "cristobal@admin.cl"
)
