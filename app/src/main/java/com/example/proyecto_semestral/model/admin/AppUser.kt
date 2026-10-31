// Define los datos compartidos de un usuario de la aplicación.
package com.example.proyecto_semestral.model.admin

data class AppUser(
    val id: String,
    val name: String,
    val email: String,
    val password: String,
    val businessName: String = "",
    val businessType: String = "",
    val businessSize: String = "",
    val isBlocked: Boolean = false
) {
    val role: UserRole
        get() = roleForEmail(email)
}
