package com.example.proyecto_semestral.ui.viewmodel.auth

import com.example.proyecto_semestral.domain.repository.AuthException
import com.example.proyecto_semestral.domain.repository.AuthRepository
import com.example.proyecto_semestral.model.auth.User
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AuthViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `restores an existing session on creation`() = runTest {
        val repository = FakeAuthRepository().apply {
            restoredUser = TEST_USER
            session.value = true
        }

        val viewModel = AuthViewModel(repository)
        advanceUntilIdle()

        assertFalse(viewModel.uiState.value.isRestoring)
        assertTrue(viewModel.uiState.value.isAuthenticated)
        assertEquals(TEST_USER, viewModel.uiState.value.user)
    }

    @Test
    fun `login exposes authenticated user`() = runTest {
        val repository = FakeAuthRepository()
        val viewModel = AuthViewModel(repository)
        advanceUntilIdle()

        viewModel.login("  ADA@EXAMPLE.COM ", "secret")
        advanceUntilIdle()

        assertEquals(TEST_USER, viewModel.uiState.value.user)
        assertFalse(viewModel.uiState.value.isLoading)
        assertEquals("  ADA@EXAMPLE.COM ", repository.loginEmail)
    }

    @Test
    fun `authentication error is exposed without authenticating`() = runTest {
        val repository = FakeAuthRepository().apply {
            loginError = AuthException("Credenciales inválidas")
        }
        val viewModel = AuthViewModel(repository)
        advanceUntilIdle()

        viewModel.login("ada@example.com", "wrong")
        advanceUntilIdle()

        assertFalse(viewModel.uiState.value.isAuthenticated)
        assertFalse(viewModel.uiState.value.isLoading)
        assertEquals("Credenciales inválidas", viewModel.uiState.value.error)
    }

    private class FakeAuthRepository : AuthRepository {
        val session = MutableStateFlow(false)
        var restoredUser: User? = null
        var loginError: AuthException? = null
        var loginEmail: String? = null

        override val hasStoredSession: Flow<Boolean> = session

        override suspend fun register(name: String, email: String, password: String): User =
            TEST_USER

        override suspend fun login(email: String, password: String): User {
            loginEmail = email
            loginError?.let { throw it }
            session.value = true
            return TEST_USER
        }

        override suspend fun restoreSession(): User? = restoredUser

        override suspend fun logout() {
            session.value = false
        }
    }

    private companion object {
        val TEST_USER = User(
            id = 7L,
            name = "Ada",
            email = "ada@example.com",
            role = "USER",
            isActive = true,
            createdAt = "2026-11-15T10:00:00Z"
        )
    }
}
