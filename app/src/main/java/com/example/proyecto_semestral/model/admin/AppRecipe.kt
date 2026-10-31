// Define los datos de una receta administrable y visible en la aplicación.
package com.example.proyecto_semestral.model.admin

import androidx.annotation.DrawableRes
import com.example.proyecto_semestral.model.content.Ingredient

data class AppRecipe(
    val id: String,
    val title: String,
    val description: String,
    val videoUrl: String,
    val category: String = "Receta",
    val duration: String = "",
    val yield: String = "",
    @DrawableRes val imageRes: Int,
    val ingredients: List<Ingredient> = emptyList(),
    val steps: List<String> = emptyList(),
    val isPublished: Boolean = true
)
