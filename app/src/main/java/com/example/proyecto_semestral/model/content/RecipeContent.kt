package com.example.proyecto_semestral.model.content

import androidx.annotation.DrawableRes

data class RecipeContent(
    val id: String,
    val title: String,
    val description: String,
    val videoUrl: String,
    val category: String,
    val duration: String,
    val yield: String,
    @DrawableRes val imageRes: Int,
    val ingredients: List<Ingredient>,
    val steps: List<String>,
    val suppliesTitle: String,
    val suppliesDescription: String
)
