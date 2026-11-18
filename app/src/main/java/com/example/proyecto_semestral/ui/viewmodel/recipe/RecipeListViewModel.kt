package com.example.proyecto_semestral.ui.viewmodel.recipe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.proyecto_semestral.domain.repository.RecipeRepository
import com.example.proyecto_semestral.domain.repository.RecipeRepositoryException
import com.example.proyecto_semestral.model.recipe.RecipeCategory
import com.example.proyecto_semestral.model.recipe.RecipePage
import com.example.proyecto_semestral.model.recipe.RecipeSummary
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface RecipeListUiState {
    data object Loading : RecipeListUiState
    data class Success(
        val page: RecipePage,
        val hasMore: Boolean = false,
        val isLoadingMore: Boolean = false
    ) : RecipeListUiState
    data object Empty : RecipeListUiState
    data class Error(val message: String) : RecipeListUiState
}

class RecipeListViewModel(
    private val repository: RecipeRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<RecipeListUiState>(RecipeListUiState.Loading)
    val uiState: StateFlow<RecipeListUiState> = _uiState.asStateFlow()

    private val _categories = MutableStateFlow<List<RecipeCategory>>(emptyList())
    val categories: StateFlow<List<RecipeCategory>> = _categories.asStateFlow()

    private var loadJob: Job? = null
    private var lastQuery: String? = null
    private var lastCategoryId: Long? = null
    private var currentPage = 0
    private var currentRecipes: List<RecipeSummary> = emptyList()
    private var categoriesLoaded = false

    fun loadCategories() {
        if (categoriesLoaded) return
        viewModelScope.launch {
            try {
                _categories.value = repository.getCategories()
                categoriesLoaded = true
            } catch (_: Exception) {
                // Las categorías son opcionales; el catálogo sigue funcionando sin filtros.
            }
        }
    }

    fun loadRecipes(
        query: String? = null,
        categoryId: Long? = null,
        page: Int = 0
    ) {
        lastQuery = query
        lastCategoryId = categoryId
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            _uiState.value = RecipeListUiState.Loading
            try {
                val result = repository.getRecipes(query, categoryId, page)
                currentPage = result.page
                currentRecipes = result.recipes
                _uiState.value = if (result.recipes.isEmpty()) {
                    RecipeListUiState.Empty
                } else {
                    RecipeListUiState.Success(
                        page = result,
                        hasMore = !result.isLast
                    )
                }
            } catch (exception: RecipeRepositoryException) {
                _uiState.value = RecipeListUiState.Error(exception.message.orEmpty())
            } catch (_: Exception) {
                _uiState.value = RecipeListUiState.Error(
                    "Ocurrió un error inesperado al cargar las recetas."
                )
            }
        }
    }

    fun loadMore() {
        val state = _uiState.value as? RecipeListUiState.Success ?: return
        if (state.isLoadingMore || !state.hasMore) return

        val nextPage = currentPage + 1
        viewModelScope.launch {
            _uiState.value = state.copy(isLoadingMore = true)
            try {
                val result = repository.getRecipes(lastQuery, lastCategoryId, nextPage)
                currentPage = result.page
                currentRecipes = currentRecipes + result.recipes
                val accumulated = result.copy(
                    recipes = currentRecipes,
                    isFirst = result.page == 0
                )
                _uiState.value = RecipeListUiState.Success(
                    page = accumulated,
                    hasMore = !result.isLast,
                    isLoadingMore = false
                )
            } catch (_: Exception) {
                _uiState.value = state.copy(isLoadingMore = false, hasMore = false)
            }
        }
    }

    fun retry() {
        loadRecipes(lastQuery, lastCategoryId, 0)
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
