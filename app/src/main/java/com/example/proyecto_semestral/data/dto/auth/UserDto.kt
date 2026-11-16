package com.example.proyecto_semestral.data.dto.auth

data class UserDto(
    val id: Long,
    val nombre: String,
    val email: String,
    val rol: String,
    val activo: Boolean,
    val creadoEn: String
)
