package com.example.proyecto_semestral.ui.viewmodel.recipe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.proyecto_semestral.domain.repository.RecipeRepository
import com.example.proyecto_semestral.domain.repository.RecipeRepositoryException
import com.example.proyecto_semestral.model.recipe.RecipePage
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface RecipeListUiState {
    data object Loading : RecipeListUiState
    data class Success(val page: RecipePage) : RecipeListUiState
    data object Empty : RecipeListUiState
    data class Error(val message: String) : RecipeListUiState
}

class RecipeListViewModel(
    private val repository: RecipeRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<RecipeListUiState>(RecipeListUiState.Loading)
    val uiState: StateFlow<RecipeListUiState> = _uiState.asStateFlow()

    private var loadJob: Job? = null
    private var lastQuery: String? = null
    private var lastCategoryId: Long? = null
    private var lastPage: Int = 0

    fun loadRecipes(
        query: String? = null,
        categoryId: Long? = null,
        page: Int = 0
    ) {
        lastQuery = query
        lastCategoryId = categoryId
        lastPage = page
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            _uiState.value = RecipeListUiState.Loading
            _uiState.value = try {
                val result = repository.getRecipes(query, categoryId, page)
                if (result.recipes.isEmpty()) {
                    RecipeListUiState.Empty
                } else {
                    RecipeListUiState.Success(result)
                }
            } catch (exception: RecipeRepositoryException) {
                RecipeListUiState.Error(exception.message.orEmpty())
            } catch (_: Exception) {
                RecipeListUiState.Error("Ocurrió un error inesperado al cargar las recetas.")
            }
        }
    }

    fun retry() {
        loadRecipes(lastQuery, lastCategoryId, lastPage)
    }

    companion object {
        fun factory(repository: RecipeRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    require(modelClass.isAssignableFrom(RecipeListViewModel::class.java))
                    return RecipeListViewModel(repository) as T
                }
            }
    }
}
