package com.example.proyecto_semestral.ui.viewmodel.course

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.proyecto_semestral.domain.repository.CourseRepository
import com.example.proyecto_semestral.domain.repository.CourseRepositoryException
import com.example.proyecto_semestral.model.course.CourseDetail
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface CourseDetailUiState {
    data object Loading : CourseDetailUiState
    data class Success(val course: CourseDetail) : CourseDetailUiState
    data class Error(val message: String) : CourseDetailUiState
}

class CourseDetailViewModel(
    private val courseId: Long,
    private val repository: CourseRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<CourseDetailUiState>(CourseDetailUiState.Loading)
    val uiState: StateFlow<CourseDetailUiState> = _uiState.asStateFlow()

    init {
        load()
    }

    fun load() {
        viewModelScope.launch {
            _uiState.value = CourseDetailUiState.Loading
            _uiState.value = try {
                CourseDetailUiState.Success(repository.getCourse(courseId))
            } catch (exception: CourseRepositoryException) {
                CourseDetailUiState.Error(exception.message.orEmpty())
            } catch (_: Exception) {
                CourseDetailUiState.Error("Ocurrió un error al cargar el curso.")
            }
        }
    }

    companion object {
        fun factory(
            courseId: Long,
            repository: CourseRepository
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                require(modelClass.isAssignableFrom(CourseDetailViewModel::class.java))
                return CourseDetailViewModel(courseId, repository) as T
            }
        }
    }
}
