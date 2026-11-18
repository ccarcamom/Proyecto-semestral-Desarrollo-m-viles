package com.example.proyecto_semestral.ui.viewmodel.course

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.proyecto_semestral.domain.repository.CourseRepository
import com.example.proyecto_semestral.domain.repository.CourseRepositoryException
import com.example.proyecto_semestral.model.course.CourseCategory
import com.example.proyecto_semestral.model.course.CoursePage
import com.example.proyecto_semestral.model.course.CourseSummary
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface CourseListUiState {
    data object Loading : CourseListUiState
    data class Success(
        val page: CoursePage,
        val hasMore: Boolean = false,
        val isLoadingMore: Boolean = false
    ) : CourseListUiState
    data object Empty : CourseListUiState
    data class Error(val message: String) : CourseListUiState
}

class CourseListViewModel(
    private val repository: CourseRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<CourseListUiState>(CourseListUiState.Loading)
    val uiState: StateFlow<CourseListUiState> = _uiState.asStateFlow()

    private val _categories = MutableStateFlow<List<CourseCategory>>(emptyList())
    val categories: StateFlow<List<CourseCategory>> = _categories.asStateFlow()

    private var loadJob: Job? = null
    private var lastQuery: String? = null
    private var lastCategoryId: Long? = null
    private var lastSize: Int = DEFAULT_SIZE
    private var currentPage = 0
    private var currentCourses: List<CourseSummary> = emptyList()
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

    fun loadCourses(
        query: String? = null,
        categoryId: Long? = null,
        page: Int = 0,
        size: Int = DEFAULT_SIZE
    ) {
        lastQuery = query
        lastCategoryId = categoryId
        lastSize = size
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            _uiState.value = CourseListUiState.Loading
            try {
                val result = repository.getCourses(
                    query = query,
                    categoryId = categoryId,
                    page = page,
                    size = size
                )
                currentPage = result.page
                currentCourses = result.courses
                _uiState.value = if (result.courses.isEmpty()) {
                    CourseListUiState.Empty
                } else {
                    CourseListUiState.Success(
                        page = result,
                        hasMore = result.page + 1 < result.totalPages
                    )
                }
            } catch (exception: CourseRepositoryException) {
                _uiState.value = CourseListUiState.Error(exception.message.orEmpty())
            } catch (_: Exception) {
                _uiState.value = CourseListUiState.Error(
                    "Ocurrió un error al cargar los cursos."
                )
            }
        }
    }

    fun loadMore() {
        val state = _uiState.value as? CourseListUiState.Success ?: return
        if (state.isLoadingMore || !state.hasMore) return

        val nextPage = currentPage + 1
        viewModelScope.launch {
            _uiState.value = state.copy(isLoadingMore = true)
            try {
                val result = repository.getCourses(
                    query = lastQuery,
                    categoryId = lastCategoryId,
                    page = nextPage,
                    size = lastSize
                )
                currentPage = result.page
                currentCourses = currentCourses + result.courses
                _uiState.value = CourseListUiState.Success(
                    page = result.copy(courses = currentCourses),
                    hasMore = result.page + 1 < result.totalPages,
                    isLoadingMore = false
                )
            } catch (_: Exception) {
                _uiState.value = state.copy(isLoadingMore = false, hasMore = false)
            }
        }
    }

    fun retry() = loadCourses(lastQuery, lastCategoryId, 0, lastSize)

    companion object {
        private const val DEFAULT_SIZE = 12

        fun factory(repository: CourseRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    require(modelClass.isAssignableFrom(CourseListViewModel::class.java))
                    return CourseListViewModel(repository) as T
                }
            }
    }
}
