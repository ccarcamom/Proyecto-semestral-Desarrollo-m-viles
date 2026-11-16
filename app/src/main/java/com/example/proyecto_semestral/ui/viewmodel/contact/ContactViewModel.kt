package com.example.proyecto_semestral.ui.viewmodel.contact

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.proyecto_semestral.domain.repository.ContactDraft
import com.example.proyecto_semestral.domain.repository.ContactRepository
import com.example.proyecto_semestral.domain.repository.ContactRepositoryException
import com.example.proyecto_semestral.domain.repository.ContactReceipt
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ContactUiState(
    val isSending: Boolean = false,
    val receipt: ContactReceipt? = null,
    val error: String? = null
) {
    val sent: Boolean
        get() = receipt != null
}

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
                ContactUiState(receipt = repository.send(draft))
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

    fun reset() {
        _uiState.value = ContactUiState()
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
