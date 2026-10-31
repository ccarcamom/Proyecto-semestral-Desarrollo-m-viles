// Conecta el panel de recetas con el catálogo compartido almacenado en memoria.
package com.example.proyecto_semestral.viewmodel.admin

import androidx.lifecycle.ViewModel
import com.example.proyecto_semestral.model.admin.AppRecipe
import com.example.proyecto_semestral.model.content.Ingredient
import com.example.proyecto_semestral.repository.admin.InMemoryRecipeRepository
import kotlinx.coroutines.flow.StateFlow

class ManageRecipesViewModel : ViewModel() {

    val recipes: StateFlow<List<AppRecipe>> = InMemoryRecipeRepository.recipes

    fun createRecipe(title: String, videoUrl: String, description: String,
        ingredients: List<Ingredient>, steps: List<String>, suppliesTitle: String,
        suppliesDescription: String, imageUri: String?): AppRecipe {
        return InMemoryRecipeRepository.create(
            title = title.trim(),
            description = description,
            videoUrl = videoUrl,
            ingredients = ingredients, steps = steps,
            suppliesTitle = suppliesTitle.trim(), suppliesDescription = suppliesDescription.trim(),
            imageUri = imageUri
        )
    }

    fun updateRecipe(id: String, title: String, videoUrl: String, description: String,
        ingredients: List<Ingredient>, steps: List<String>, suppliesTitle: String,
        suppliesDescription: String, imageUri: String?): Boolean {
        val currentRecipe = InMemoryRecipeRepository.findById(id) ?: return false
        return InMemoryRecipeRepository.update(
            currentRecipe.copy(
                title = title.trim(),
                description = description.trim(),
                videoUrl = videoUrl.trim(),
                ingredients = ingredients, steps = steps,
                suppliesTitle = suppliesTitle.trim(), suppliesDescription = suppliesDescription.trim(),
                imageUri = imageUri
            )
        )
    }

}
