package com.example.proyecto_semestral.data.dto.auth

data class AuthResponseDto(
    val accessToken: String,
    val tokenType: String,
    val expiresIn: Long,
    val usuario: UserDto
)
