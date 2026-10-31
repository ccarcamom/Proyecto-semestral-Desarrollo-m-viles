// Conecta el panel de usuarios con el repositorio reactivo almacenado en memoria.
package com.example.proyecto_semestral.viewmodel.admin

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.proyecto_semestral.model.admin.AppUser
import com.example.proyecto_semestral.model.admin.UserRole
import com.example.proyecto_semestral.repository.admin.InMemoryUserRepository
import com.example.proyecto_semestral.ui.components.admin.UserStatusFilter
import kotlinx.coroutines.flow.StateFlow

class ManageUsersViewModel : ViewModel() {

    val users: StateFlow<List<AppUser>> = InMemoryUserRepository.users

    var query by mutableStateOf("")
        private set

    var selectedFilter by mutableStateOf(UserStatusFilter.ALL)
        private set

    fun onQueryChange(value: String) {
        query = value
    }

    fun onFilterSelected(filter: UserStatusFilter) {
        selectedFilter = filter
    }

    fun filteredUsers(allUsers: List<AppUser>): List<AppUser> {
        return allUsers.filter { user ->
            val isManageableUser = user.role == UserRole.USER
            val matchesQuery = query.isBlank() ||
                user.name.contains(query, ignoreCase = true) ||
                user.email.contains(query, ignoreCase = true)
            val matchesStatus = when (selectedFilter) {
                UserStatusFilter.ALL -> true
                UserStatusFilter.ACTIVE -> !user.isBlocked
                UserStatusFilter.BLOCKED -> user.isBlocked
            }
            isManageableUser && matchesQuery && matchesStatus
        }
    }

    fun updateUser(id: String, name: String, email: String): Boolean {
        return InMemoryUserRepository.updateProfile(id, name, email)
    }

    fun blockUser(id: String) {
        InMemoryUserRepository.setBlocked(id, true)
    }

    fun unblockUser(id: String) {
        InMemoryUserRepository.setBlocked(id, false)
    }

    fun deleteUser(id: String) {
        InMemoryUserRepository.delete(id)
    }
}
