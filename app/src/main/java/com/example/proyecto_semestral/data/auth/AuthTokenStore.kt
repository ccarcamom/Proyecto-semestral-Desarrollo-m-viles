package com.example.proyecto_semestral.data.auth

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.authDataStore by preferencesDataStore(name = "auth_session")

class AuthTokenStore(context: Context) {
    private val dataStore = context.applicationContext.authDataStore

    val token: Flow<String?> = dataStore.data.map { preferences ->
        preferences[ACCESS_TOKEN]?.takeIf(String::isNotBlank)
    }

    suspend fun getToken(): String? = token.first()

    suspend fun saveToken(token: String) {
        require(token.isNotBlank()) { "El token de acceso no puede estar vacío." }
        dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN] = token
        }
    }

    suspend fun clearToken() {
        dataStore.edit { preferences ->
            preferences.remove(ACCESS_TOKEN)
        }
    }

    private companion object {
        val ACCESS_TOKEN = stringPreferencesKey("access_token")
    }
}
