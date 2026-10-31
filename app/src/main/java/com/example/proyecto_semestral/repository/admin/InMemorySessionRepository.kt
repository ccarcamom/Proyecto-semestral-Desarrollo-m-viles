// Conserva en memoria la sesión del usuario mientras la aplicación está activa.
package com.example.proyecto_semestral.repository.admin

import com.example.proyecto_semestral.model.admin.AppUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object InMemorySessionRepository {

    private val _currentUserId = MutableStateFlow<String?>(null)
    val currentUserId: StateFlow<String?> = _currentUserId.asStateFlow()

    val currentUser: AppUser?
        get() = _currentUserId.value?.let(InMemoryUserRepository::findById)

    fun start(user: AppUser) {
        _currentUserId.value = user.id
    }

    fun clear() {
        _currentUserId.value = null
    }
}
