package com.example.proyecto_semestral.ui.viewmodel.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.proyecto_semestral.domain.repository.AdminRepository
import com.example.proyecto_semestral.domain.repository.AdminRepositoryException
import com.example.proyecto_semestral.model.admin.AdminCategory
import com.example.proyecto_semestral.model.admin.AdminCourse
import com.example.proyecto_semestral.model.admin.AdminCourseDraft
import com.example.proyecto_semestral.model.admin.AdminLessonDraft
import com.example.proyecto_semestral.model.admin.AdminRecipe
import com.example.proyecto_semestral.model.admin.AdminRecipeDraft
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AdminUiState(
    val isLoading: Boolean = true,
    val isSaving: Boolean = false,
    val recipes: List<AdminRecipe> = emptyList(),
    val courses: List<AdminCourse> = emptyList(),
    val recipeCategories: List<AdminCategory> = emptyList(),
    val courseCategories: List<AdminCategory> = emptyList(),
    val error: String? = null,
    val message: String? = null
)

class AdminViewModel(
    private val repository: AdminRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdminUiState())
    val uiState: StateFlow<AdminUiState> = _uiState.asStateFlow()

    fun load() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null, message = null) }
            try {
                val recipes = repository.getRecipes()
                val recipeCategories = repository.getRecipeCategories()
                val courses = repository.getCourses()
                val courseCategories = repository.getCourseCategories()
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        recipes = recipes,
                        recipeCategories = recipeCategories,
                        courses = courses,
                        courseCategories = courseCategories
                    )
                }
            } catch (exception: AdminRepositoryException) {
                _uiState.update {
                    it.copy(isLoading = false, error = exception.message)
                }
            } catch (_: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = "Ocurrió un error inesperado al cargar el panel."
                    )
                }
            }
        }
    }

    fun createRecipe(draft: AdminRecipeDraft) {
        runAction("Receta creada correctamente.") {
            repository.createRecipe(draft)
        }
    }

    fun deleteRecipe(id: Long) {
        runAction("Receta eliminada.") {
            repository.deleteRecipe(id)
        }
    }

    fun createCourse(draft: AdminCourseDraft) {
        runAction("Curso creado correctamente.") {
            repository.createCourse(draft)
        }
    }

    fun changeCoursePublication(id: Long, published: Boolean) {
        runAction(if (published) "Curso publicado." else "Curso oculto del catálogo.") {
            repository.changeCoursePublication(id, published)
        }
    }

    fun deleteCourse(id: Long) {
        runAction("Curso eliminado.") {
            repository.deleteCourse(id)
        }
    }

    fun addLesson(courseId: Long, draft: AdminLessonDraft) {
        runAction("Lección agregada al curso.") {
            repository.addLesson(courseId, draft)
        }
    }

    fun clearFeedback() {
        _uiState.update { it.copy(error = null, message = null) }
    }

    private fun runAction(successMessage: String, block: suspend () -> Unit) {
        if (_uiState.value.isSaving) return
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, error = null, message = null) }
            try {
                block()
                refreshLists()
                _uiState.update { it.copy(isSaving = false, message = successMessage) }
            } catch (exception: AdminRepositoryException) {
                _uiState.update { it.copy(isSaving = false, error = exception.message) }
            } catch (_: Exception) {
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        error = "Ocurrió un error inesperado. Inténtalo nuevamente."
                    )
                }
            }
        }
    }

    private suspend fun refreshLists() {
        val recipes = repository.getRecipes()
        val courses = repository.getCourses()
        _uiState.update { it.copy(recipes = recipes, courses = courses) }
    }

    companion object {
        fun factory(repository: AdminRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    require(modelClass.isAssignableFrom(AdminViewModel::class.java))
                    return AdminViewModel(repository) as T
                }
            }
    }
}
