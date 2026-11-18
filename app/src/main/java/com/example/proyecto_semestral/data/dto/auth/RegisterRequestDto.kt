package com.example.proyecto_semestral.data.dto.auth

data class RegisterRequestDto(
    val nombre: String,
    val email: String,
    val password: String,
    val nombreNegocio: String? = null,
    val tipoNegocio: String? = null,
    val tamanoNegocio: String? = null
)
