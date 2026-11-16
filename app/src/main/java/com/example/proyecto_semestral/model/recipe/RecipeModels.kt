package com.example.proyecto_semestral.model.recipe

data class RecipeCategory(
    val id: Long,
    val name: String
)

data class RecipeSummary(
    val id: Long,
    val title: String,
    val description: String,
    val imageUrl: String?,
    val duration: String,
    val category: RecipeCategory?
)

data class RecipeIngredient(
    val id: Long,
    val name: String,
    val amount: String,
    val order: Int
)

data class RecipeStep(
    val id: Long,
    val number: Int,
    val description: String
)

data class RecipeDetail(
    val id: Long,
    val title: String,
    val description: String,
    val imageUrl: String?,
    val duration: String,
    val category: RecipeCategory?,
    val videoUrl: String?,
    val yield: String,
    val suppliesTitle: String,
    val suppliesDescription: String,
    val ingredients: List<RecipeIngredient>,
    val steps: List<RecipeStep>
)

data class RecipePage(
    val recipes: List<RecipeSummary>,
    val page: Int,
    val pageSize: Int,
    val totalElements: Long,
    val totalPages: Int,
    val isFirst: Boolean,
    val isLast: Boolean
)
