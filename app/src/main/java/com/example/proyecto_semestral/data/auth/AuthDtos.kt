package com.example.proyecto_semestral.data.auth

data class RegisterRequestDto(
    val nombre: String,
    val email: String,
    val password: String
)

data class LoginRequestDto(
    val email: String,
    val password: String
)

data class AuthResponseDto(
    val accessToken: String,
    val tokenType: String,
    val expiresIn: Long,
    val usuario: UserDto
)

data class UserDto(
    val id: Long,
    val nombre: String,
    val email: String,
    val rol: String,
    val activo: Boolean,
    val creadoEn: String
)
