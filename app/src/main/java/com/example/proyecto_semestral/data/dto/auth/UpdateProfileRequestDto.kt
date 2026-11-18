package com.example.proyecto_semestral.data.dto.auth

data class UpdateProfileRequestDto(
    val nombre: String,
    val nombreNegocio: String? = null,
    val tipoNegocio: String? = null,
    val tamanoNegocio: String? = null
)
