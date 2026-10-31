// Expone las recetas compartidas para mostrarlas junto al resto del contenido de explorar.
package com.example.proyecto_semestral.viewmodel

import androidx.lifecycle.ViewModel
import com.example.proyecto_semestral.model.admin.AppRecipe
import com.example.proyecto_semestral.model.content.ExploreContent
import com.example.proyecto_semestral.repository.admin.InMemoryRecipeRepository
import com.example.proyecto_semestral.ui.components.ContentFilter
import kotlinx.coroutines.flow.StateFlow

class ExploreViewModel : ViewModel() {

    val recipes: StateFlow<List<AppRecipe>> = InMemoryRecipeRepository.recipes

    fun recipeItems(recipes: List<AppRecipe>): List<ExploreContent> {
        return recipes
            .filter { it.isPublished }
            .map { recipe ->
                ExploreContent(
                    eyebrow = "RECETA",
                    title = recipe.title,
                    metadata = listOf(recipe.category, recipe.duration)
                        .filter { it.isNotBlank() }
                        .joinToString(" · ")
                        .ifBlank { "Video receta" },
                    type = ContentFilter.Recipes,
                    imageRes = recipe.imageRes,
                    id = recipe.id
                )
            }
    }
}
