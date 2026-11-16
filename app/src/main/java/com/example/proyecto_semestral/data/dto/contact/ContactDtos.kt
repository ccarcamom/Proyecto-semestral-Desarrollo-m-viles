package com.example.proyecto_semestral.data.dto.contact

data class CreateContactRequestDto(
    val usuarioId: Long?,
    val email: String,
    val telefono: String?,
    val nombreNegocio: String,
    val tipoNegocio: String?,
    val tamanoNegocio: String?,
    val descripcion: String
)

data class ContactResponseDto(
    val id: Long,
    val estado: String?
)
