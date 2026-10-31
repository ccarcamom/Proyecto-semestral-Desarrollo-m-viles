// Verifica que el panel admin observe y gestione solamente cuentas de usuario.
package com.example.proyecto_semestral.viewmodel.admin

import com.example.proyecto_semestral.repository.admin.InMemoryUserRepository
import com.example.proyecto_semestral.ui.components.admin.UserStatusFilter
import java.util.UUID
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ManageUsersViewModelTest {

    @Test
    fun registeredUsersAppearAndAdminAccountsAreExcluded() {
        val suffix = UUID.randomUUID().toString()
        val user = requireNotNull(
            InMemoryUserRepository.register("Usuario", "usuario-$suffix@correo.cl", "1234")
        )
        val admin = requireNotNull(
            InMemoryUserRepository.register("Admin", "admin-$suffix@admin.cl", "1234")
        )

        try {
            val visibleUsers = ManageUsersViewModel().filteredUsers(InMemoryUserRepository.users.value)

            assertTrue(visibleUsers.any { it.id == user.id })
            assertFalse(visibleUsers.any { it.id == admin.id })
        } finally {
            InMemoryUserRepository.delete(user.id)
            InMemoryUserRepository.delete(admin.id)
        }
    }

    @Test
    fun blockedFilterOnlyReturnsBlockedUsers() {
        val suffix = UUID.randomUUID().toString()
        val active = requireNotNull(
            InMemoryUserRepository.register("Activo", "activo-$suffix@correo.cl", "1234")
        )
        val blocked = requireNotNull(
            InMemoryUserRepository.register("Bloqueado", "bloqueado-$suffix@correo.cl", "1234")
        )
        InMemoryUserRepository.setBlocked(blocked.id, true)

        try {
            val viewModel = ManageUsersViewModel()
            viewModel.onFilterSelected(UserStatusFilter.BLOCKED)
            val visibleUsers = viewModel.filteredUsers(InMemoryUserRepository.users.value)

            assertTrue(visibleUsers.any { it.id == blocked.id })
            assertFalse(visibleUsers.any { it.id == active.id })
        } finally {
            InMemoryUserRepository.delete(active.id)
            InMemoryUserRepository.delete(blocked.id)
        }
    }
}
