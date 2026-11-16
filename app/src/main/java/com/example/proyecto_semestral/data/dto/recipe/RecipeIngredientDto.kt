package com.example.proyecto_semestral.data.dto.recipe

data class RecipeIngredientDto(
    val id: Long,
    val nombre: String,
    val cantidadTexto: String?,
    val orden: Int?
)
