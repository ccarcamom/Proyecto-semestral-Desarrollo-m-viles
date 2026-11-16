package com.example.proyecto_semestral.data.dto.admin

data class CreateRecipeRequestDto(
    val titulo: String,
    val descripcion: String?,
    val categoriaId: Long,
    val videoUrl: String?,
    val imagenUrl: String?,
    val duracionTexto: String?,
    val rendimientoTexto: String?,
    val insumosTitulo: String?,
    val insumosDescripcion: String?,
    val publicada: Boolean,
    val creadoPor: String,
    val ingredientes: List<CreateIngredientRequestDto>,
    val pasos: List<CreateStepRequestDto>
)

data class UpdateRecipeRequestDto(
    val titulo: String,
    val descripcion: String?,
    val categoriaId: Long,
    val videoUrl: String?,
    val imagenUrl: String?,
    val duracionTexto: String?,
    val rendimientoTexto: String?,
    val insumosTitulo: String?,
    val insumosDescripcion: String?,
    val publicada: Boolean,
    val ingredientes: List<CreateIngredientRequestDto>,
    val pasos: List<CreateStepRequestDto>
)

data class CreateIngredientRequestDto(
    val nombre: String,
    val cantidadTexto: String?,
    val orden: Int
)

data class CreateStepRequestDto(
    val numero: Int,
    val descripcion: String
)
