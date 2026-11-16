package com.example.proyecto_semestral.ui.viewmodel.contact

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.proyecto_semestral.domain.repository.ContactDraft
import com.example.proyecto_semestral.domain.repository.ContactRepository
import com.example.proyecto_semestral.domain.repository.ContactRepositoryException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ContactUiState(
    val isSending: Boolean = false,
    val sent: Boolean = false,
    val error: String? = null
)

class ContactViewModel(
    private val repository: ContactRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ContactUiState())
    val uiState: StateFlow<ContactUiState> = _uiState.asStateFlow()

    fun send(draft: ContactDraft) {
        if (_uiState.value.isSending) return
        viewModelScope.launch {
            _uiState.value = ContactUiState(isSending = true)
            _uiState.value = try {
                repository.send(draft)
                ContactUiState(sent = true)
            } catch (exception: ContactRepositoryException) {
                ContactUiState(error = exception.message)
            } catch (_: Exception) {
                ContactUiState(error = "Ocurrió un error inesperado.")
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    companion object {
        fun factory(repository: ContactRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    require(modelClass.isAssignableFrom(ContactViewModel::class.java))
                    return ContactViewModel(repository) as T
                }
            }
    }
}
