package com.example.proyecto_semestral.domain.repository

import com.example.proyecto_semestral.model.auth.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val hasStoredSession: Flow<Boolean>

    suspend fun register(
        name: String,
        email: String,
        password: String,
        businessName: String = "",
        businessType: String = "",
        businessSize: String = ""
    ): User

    suspend fun login(email: String, password: String): User

    suspend fun restoreSession(): User?

    suspend fun updateProfile(
        name: String,
        businessName: String,
        businessType: String,
        businessSize: String
    ): User

    suspend fun uploadProfilePhoto(
        imageBytes: ByteArray,
        fileName: String,
        contentType: String
    ): User

    suspend fun logout()
}
