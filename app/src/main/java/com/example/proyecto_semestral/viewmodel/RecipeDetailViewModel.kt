// Proporciona a la pantalla de detalle las recetas almacenadas en el catálogo compartido.
package com.example.proyecto_semestral.viewmodel

import androidx.lifecycle.ViewModel
import com.example.proyecto_semestral.model.admin.AppRecipe
import com.example.proyecto_semestral.repository.admin.InMemoryRecipeRepository
import kotlinx.coroutines.flow.StateFlow

class RecipeDetailViewModel : ViewModel() {
    val recipes: StateFlow<List<AppRecipe>> = InMemoryRecipeRepository.recipes
}
