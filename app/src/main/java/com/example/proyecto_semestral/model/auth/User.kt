package com.example.proyecto_semestral.model.auth

data class User(
    val id: Long,
    val name: String,
    val email: String,
    val role: String,
    val isActive: Boolean,
    val createdAt: String
)

val User.canAccessAdminPanel: Boolean
    get() = role.equals("ADMIN", ignoreCase = true)
