package com.example.proyecto_semestral.data

data class RecipeSummaryDto(
    val id: Long,
    val titulo: String,
    val descripcion: String?,
    val imagenUrl: String?,
    val duracionTexto: String?,
    val categoria: CategoryDto?
)
