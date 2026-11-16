package com.example.proyecto_semestral.ui.viewmodel.course

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.proyecto_semestral.domain.repository.CourseRepository
import com.example.proyecto_semestral.domain.repository.CourseRepositoryException
import com.example.proyecto_semestral.model.course.CoursePage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface CourseListUiState {
    data object Loading : CourseListUiState
    data class Success(val page: CoursePage) : CourseListUiState
    data object Empty : CourseListUiState
    data class Error(val message: String) : CourseListUiState
}

class CourseListViewModel(
    private val repository: CourseRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<CourseListUiState>(CourseListUiState.Loading)
    val uiState: StateFlow<CourseListUiState> = _uiState.asStateFlow()
    private var lastQuery: String? = null

    fun loadCourses(query: String? = null, size: Int = 12) {
        lastQuery = query
        viewModelScope.launch {
            _uiState.value = CourseListUiState.Loading
            _uiState.value = try {
                val page = repository.getCourses(query = query, page = 0, size = size)
                if (page.courses.isEmpty()) CourseListUiState.Empty
                else CourseListUiState.Success(page)
            } catch (exception: CourseRepositoryException) {
                CourseListUiState.Error(exception.message.orEmpty())
            } catch (_: Exception) {
                CourseListUiState.Error("Ocurrió un error al cargar los cursos.")
            }
        }
    }

    fun retry() = loadCourses(lastQuery)

    companion object {
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
