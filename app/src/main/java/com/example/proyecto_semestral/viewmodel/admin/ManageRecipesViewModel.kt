// Conecta el panel de recetas con el catálogo compartido almacenado en memoria.
package com.example.proyecto_semestral.viewmodel.admin

import androidx.lifecycle.ViewModel
import com.example.proyecto_semestral.model.admin.AppRecipe
import com.example.proyecto_semestral.repository.admin.InMemoryRecipeRepository
import kotlinx.coroutines.flow.StateFlow

class ManageRecipesViewModel : ViewModel() {

    val recipes: StateFlow<List<AppRecipe>> = InMemoryRecipeRepository.recipes

    fun createRecipe(videoUrl: String, description: String): AppRecipe {
        return InMemoryRecipeRepository.create(
            title = titleFrom(description),
            description = description,
            videoUrl = videoUrl
        )
    }

    fun updateRecipe(id: String, videoUrl: String, description: String): Boolean {
        val currentRecipe = InMemoryRecipeRepository.findById(id) ?: return false
        return InMemoryRecipeRepository.update(
            currentRecipe.copy(
                title = titleFrom(description),
                description = description.trim(),
                videoUrl = videoUrl.trim()
            )
        )
    }

    private fun titleFrom(description: String): String {
        return description
            .trim()
            .substringBefore('.')
            .take(MAX_TITLE_LENGTH)
            .ifBlank { "Nueva receta" }
    }

    private companion object {
        const val MAX_TITLE_LENGTH = 48
    }
}
