package com.example.proyecto_semestral.data.dto.recipe

data class RecipeDetailDto(
    val id: Long,
    val titulo: String,
    val descripcion: String?,
    val imagenUrl: String?,
    val duracionTexto: String?,
    val categoria: RecipeCategoryDto?,
    val videoUrl: String?,
    val rendimientoTexto: String?,
    val insumosTitulo: String?,
    val insumosDescripcion: String?,
    val ingredientes: List<RecipeIngredientDto>?,
    val pasos: List<RecipeStepDto>?
)
