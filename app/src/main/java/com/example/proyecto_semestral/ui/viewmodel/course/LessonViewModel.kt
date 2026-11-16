package com.example.proyecto_semestral.ui.viewmodel.course

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.proyecto_semestral.domain.repository.CourseRepository
import com.example.proyecto_semestral.domain.repository.CourseRepositoryException
import com.example.proyecto_semestral.model.course.CourseDetail
import com.example.proyecto_semestral.model.course.CourseLesson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface LessonUiState {
    data object Loading : LessonUiState
    data class Success(
        val course: CourseDetail,
        val lesson: CourseLesson,
        val position: Int
    ) : LessonUiState {
        val nextLesson: CourseLesson?
            get() = course.lessons.getOrNull(position + 1)
    }
    data class Error(val message: String) : LessonUiState
}

class LessonViewModel(
    private val courseId: Long,
    private val lessonId: Long,
    private val repository: CourseRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<LessonUiState>(LessonUiState.Loading)
    val uiState: StateFlow<LessonUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _uiState.value = try {
                val course = repository.getCourse(courseId)
                val position = course.lessons.indexOfFirst { it.id == lessonId }
                if (position < 0) LessonUiState.Error("La lección ya no está disponible.")
                else LessonUiState.Success(course, course.lessons[position], position)
            } catch (exception: CourseRepositoryException) {
                LessonUiState.Error(exception.message.orEmpty())
            } catch (_: Exception) {
                LessonUiState.Error("Ocurrió un error al cargar la lección.")
            }
        }
    }

    companion object {
        fun factory(
            courseId: Long,
            lessonId: Long,
            repository: CourseRepository
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                require(modelClass.isAssignableFrom(LessonViewModel::class.java))
                return LessonViewModel(courseId, lessonId, repository) as T
            }
        }
    }
}
