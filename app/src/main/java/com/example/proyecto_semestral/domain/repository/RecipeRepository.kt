package com.example.proyecto_semestral.domain.repository

import com.example.proyecto_semestral.model.recipe.RecipeDetail
import com.example.proyecto_semestral.model.recipe.RecipePage

interface RecipeRepository {
    suspend fun getRecipes(
        query: String? = null,
        categoryId: Long? = null,
        page: Int = 0
    ): RecipePage

    suspend fun getRecipe(id: Long): RecipeDetail
}

open class RecipeRepositoryException(
    message: String,
    cause: Throwable? = null
) : Exception(message, cause)

class RecipeNotFoundException(cause: Throwable? = null) :
    RecipeRepositoryException("No encontramos esta receta.", cause)
