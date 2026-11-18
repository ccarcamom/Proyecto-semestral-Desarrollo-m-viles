package com.example.proyecto_semestral.data.local

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.authDataStore by preferencesDataStore(name = "auth_session")

/**
 * Guarda el token de acceso cifrado con una clave AES/GCM alojada en Android Keystore.
 *
 * El token nunca se escribe en texto plano: se cifra antes de persistirlo en DataStore.
 * La clave privada permanece en el Keystore del dispositivo y no es exportable.
 */
class AuthTokenStore(context: Context) {
    private val dataStore = context.applicationContext.authDataStore

    val token: Flow<String?> = dataStore.data.map { preferences ->
        val encrypted = preferences[ENCRYPTED_ACCESS_TOKEN]?.takeIf(String::isNotBlank)
        if (encrypted != null) {
            decryptSafely(encrypted)?.takeIf(String::isNotBlank)
        } else {
            // Compatibilidad con versiones anteriores que guardaban el token sin cifrar.
            preferences[LEGACY_ACCESS_TOKEN]?.takeIf(String::isNotBlank)
        }
    }

    suspend fun getToken(): String? {
        val preferences = dataStore.data.first()
        val encrypted = preferences[ENCRYPTED_ACCESS_TOKEN]?.takeIf(String::isNotBlank)
        if (encrypted != null) {
            val decrypted = decryptSafely(encrypted)?.takeIf(String::isNotBlank)
            if (decrypted == null) {
                // La clave pudo invalidarse (restauración, cambio de bloqueo, etc.).
                clearToken()
            }
            return decrypted
        }

        val legacy = preferences[LEGACY_ACCESS_TOKEN]?.takeIf(String::isNotBlank) ?: return null
        saveToken(legacy)
        return legacy
    }

    suspend fun saveToken(token: String) {
        require(token.isNotBlank()) { "El token de acceso no puede estar vacío." }
        val encrypted = encrypt(token)
        dataStore.edit { preferences ->
            preferences[ENCRYPTED_ACCESS_TOKEN] = encrypted
            preferences.remove(LEGACY_ACCESS_TOKEN)
        }
    }

    suspend fun clearToken() {
        dataStore.edit { preferences ->
            preferences.remove(ENCRYPTED_ACCESS_TOKEN)
            preferences.remove(LEGACY_ACCESS_TOKEN)
        }
    }

    private fun encrypt(plainText: String): String {
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, getOrCreateSecretKey())
        val cipherText = cipher.doFinal(plainText.toByteArray(Charsets.UTF_8))
        val payload = cipher.iv + cipherText
        return Base64.encodeToString(payload, Base64.NO_WRAP)
    }

    private fun decrypt(payload: String): String {
        val bytes = Base64.decode(payload, Base64.NO_WRAP)
        require(bytes.size > IV_SIZE_BYTES) { "El token cifrado está incompleto." }
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(
            Cipher.DECRYPT_MODE,
            getOrCreateSecretKey(),
            GCMParameterSpec(TAG_LENGTH_BITS, bytes, 0, IV_SIZE_BYTES)
        )
        val plainText = cipher.doFinal(bytes, IV_SIZE_BYTES, bytes.size - IV_SIZE_BYTES)
        return String(plainText, Charsets.UTF_8)
    }

    private fun decryptSafely(payload: String): String? =
        runCatching { decrypt(payload) }.getOrNull()

    private fun getOrCreateSecretKey(): SecretKey = synchronized(KEY_LOCK) {
        val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE).apply { load(null) }
        val existing = (keyStore.getEntry(KEY_ALIAS, null) as? KeyStore.SecretKeyEntry)?.secretKey
        if (existing != null) return existing

        val generator = KeyGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_AES,
            ANDROID_KEYSTORE
        )
        generator.init(
            KeyGenParameterSpec.Builder(
                KEY_ALIAS,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            )
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .setRandomizedEncryptionRequired(true)
                .build()
        )
        generator.generateKey()
    }

    private companion object {
        const val ANDROID_KEYSTORE = "AndroidKeyStore"
        const val KEY_ALIAS = "master_martini_auth_token"
        const val TRANSFORMATION = "AES/GCM/NoPadding"
        const val TAG_LENGTH_BITS = 128
        const val IV_SIZE_BYTES = 12
        val KEY_LOCK = Any()

        val ENCRYPTED_ACCESS_TOKEN = stringPreferencesKey("access_token_encrypted")
        val LEGACY_ACCESS_TOKEN = stringPreferencesKey("access_token")
    }
}
