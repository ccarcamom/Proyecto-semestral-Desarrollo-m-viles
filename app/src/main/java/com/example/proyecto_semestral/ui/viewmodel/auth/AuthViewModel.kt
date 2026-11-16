package com.example.proyecto_semestral.ui.viewmodel.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.proyecto_semestral.domain.repository.AuthException
import com.example.proyecto_semestral.domain.repository.AuthRepository
import com.example.proyecto_semestral.model.auth.User
import com.example.proyecto_semestral.model.auth.canAccessAdminPanel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AuthUiState(
    val isRestoring: Boolean = true,
    val isLoading: Boolean = false,
    val user: User? = null,
    val error: String? = null
) {
    val isAuthenticated: Boolean
        get() = user != null

    val isAdmin: Boolean
        get() = user?.canAccessAdminPanel == true
}

class AuthViewModel(
    private val repository: AuthRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    init {
        observeInvalidatedSession()
        restoreSession()
    }

    fun login(email: String, password: String) {
        executeAuthentication {
            repository.login(email, password)
        }
    }

    fun register(name: String, email: String, password: String) {
        executeAuthentication {
            repository.register(name, email, password)
        }
    }

    fun logout() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            repository.logout()
            _uiState.value = AuthUiState(isRestoring = false)
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    private fun restoreSession() {
        viewModelScope.launch {
            _uiState.update { it.copy(isRestoring = true, error = null) }
            try {
                val user = repository.restoreSession()
                _uiState.value = AuthUiState(isRestoring = false, user = user)
            } catch (exception: AuthException) {
                _uiState.value = AuthUiState(
                    isRestoring = false,
                    error = exception.message
                )
            } catch (_: Exception) {
                _uiState.value = AuthUiState(
                    isRestoring = false,
                    error = "No se pudo restaurar la sesión."
                )
            }
        }
    }

    private fun executeAuthentication(block: suspend () -> User) {
        if (_uiState.value.isLoading) return
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val user = block()
                _uiState.value = AuthUiState(isRestoring = false, user = user)
            } catch (exception: AuthException) {
                _uiState.update {
                    it.copy(isRestoring = false, isLoading = false, error = exception.message)
                }
            } catch (_: Exception) {
                _uiState.update {
                    it.copy(
                        isRestoring = false,
                        isLoading = false,
                        error = "Ocurrió un error inesperado. Inténtalo nuevamente."
                    )
                }
            }
        }
    }

    private fun observeInvalidatedSession() {
        viewModelScope.launch {
            repository.hasStoredSession.collect { hasStoredSession ->
                if (!hasStoredSession && _uiState.value.user != null) {
                    _uiState.value = AuthUiState(
                        isRestoring = false,
                        error = "Tu sesión expiró. Inicia sesión nuevamente."
                    )
                }
            }
        }
    }

    companion object {
        fun factory(repository: AuthRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    require(modelClass.isAssignableFrom(AuthViewModel::class.java))
                    return AuthViewModel(repository) as T
                }
            }
    }
}
