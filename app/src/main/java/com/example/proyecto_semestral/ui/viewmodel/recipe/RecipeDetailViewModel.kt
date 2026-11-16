package com.example.proyecto_semestral.ui.viewmodel.recipe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.proyecto_semestral.domain.repository.RecipeNotFoundException
import com.example.proyecto_semestral.domain.repository.RecipeRepository
import com.example.proyecto_semestral.domain.repository.RecipeRepositoryException
import com.example.proyecto_semestral.model.recipe.RecipeDetail
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface RecipeDetailUiState {
    data object Loading : RecipeDetailUiState
    data class Success(val recipe: RecipeDetail) : RecipeDetailUiState
    data object Empty : RecipeDetailUiState
    data class Error(val message: String) : RecipeDetailUiState
}

class RecipeDetailViewModel(
    private val recipeId: Long,
    private val repository: RecipeRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<RecipeDetailUiState>(RecipeDetailUiState.Loading)
    val uiState: StateFlow<RecipeDetailUiState> = _uiState.asStateFlow()

    init {
        loadRecipe()
    }

    fun loadRecipe() {
        if (recipeId <= 0) {
            _uiState.value = RecipeDetailUiState.Empty
            return
        }
        viewModelScope.launch {
            _uiState.value = RecipeDetailUiState.Loading
            _uiState.value = try {
                RecipeDetailUiState.Success(repository.getRecipe(recipeId))
            } catch (_: RecipeNotFoundException) {
                RecipeDetailUiState.Empty
            } catch (exception: RecipeRepositoryException) {
                RecipeDetailUiState.Error(exception.message.orEmpty())
            } catch (_: Exception) {
                RecipeDetailUiState.Error("Ocurrió un error inesperado al cargar la receta.")
            }
        }
    }

    companion object {
        fun factory(
            recipeId: Long,
            repository: RecipeRepository
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                require(modelClass.isAssignableFrom(RecipeDetailViewModel::class.java))
                return RecipeDetailViewModel(recipeId, repository) as T
            }
        }
    }
}
