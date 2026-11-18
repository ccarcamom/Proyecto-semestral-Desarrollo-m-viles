package com.example.proyecto_semestral.data.dto.recipe

data class RecipeSummaryDto(
    val id: Long,
    val titulo: String,
    val descripcion: String?,
    val imagenUrl: String?,
    val duracionTexto: String?,
    val categoria: RecipeCategoryDto?,
    val publicada: Boolean? = null
)
