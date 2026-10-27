package com.example.proyecto_semestral.navigation

import android.content.Context
import android.content.SharedPreferences
import com.example.proyecto_semestral.model.profile.DEFAULT_BUSINESS_SIZE
import com.example.proyecto_semestral.model.profile.DEFAULT_BUSINESS_TYPE

class RouterGuard(context: Context) {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        PREFS_NAME,
        Context.MODE_PRIVATE
    )

    fun canAccessDashboard(): Boolean {
        val isLoggedIn = sharedPreferences.getBoolean(KEY_IS_LOGGED, false)
        val userName = getUserName()

        if (isLoggedIn && userName.isBlank()) {
            logout()
            return false
        }

        return isLoggedIn && userName.isNotBlank()
    }

    fun saveSession(isLoggedIn: Boolean) {
        sharedPreferences.edit().putBoolean(KEY_IS_LOGGED, isLoggedIn).apply()
    }

    fun saveSession(isLoggedIn: Boolean, userName: String) {
        sharedPreferences.edit()
            .putBoolean(KEY_IS_LOGGED, isLoggedIn)
            .putString(KEY_USER_NAME, userName)
            .apply()
    }

    fun saveSession(isLoggedIn: Boolean, userName: String, userEmail: String) {
        sharedPreferences.edit()
            .putBoolean(KEY_IS_LOGGED, isLoggedIn)
            .putString(KEY_USER_NAME, userName)
            .putString(KEY_USER_EMAIL, userEmail)
            .apply()
    }

    fun getUserName(): String {
        return sharedPreferences.getString(KEY_USER_NAME, null).orEmpty()
    }

    fun getUserEmail(): String {
        return sharedPreferences.getString(KEY_USER_EMAIL, null).orEmpty()
    }

    fun saveProfile(
        name: String,
        email: String,
        profileImageUri: String,
        phone: String,
        businessName: String,
        businessType: String,
        businessSize: String,
        interests: Set<String>
    ) {
        sharedPreferences.edit()
            .putString(KEY_USER_NAME, name)
            .putString(KEY_USER_EMAIL, email)
            .putString(KEY_PROFILE_IMAGE_URI, profileImageUri)
            .putString(KEY_USER_PHONE, phone)
            .putString(KEY_BUSINESS_NAME, businessName)
            .putString(KEY_BUSINESS_TYPE, businessType)
            .putString(KEY_BUSINESS_SIZE, businessSize)
            .putStringSet(KEY_USER_INTERESTS, interests)
            .apply()
    }

    fun getUserPhone(): String {
        return sharedPreferences.getString(KEY_USER_PHONE, null).orEmpty()
    }

    fun getProfileImageUri(): String {
        return sharedPreferences.getString(KEY_PROFILE_IMAGE_URI, null).orEmpty()
    }

    fun getBusinessName(): String {
        return sharedPreferences.getString(KEY_BUSINESS_NAME, null).orEmpty()
    }

    fun getBusinessType(): String {
        return sharedPreferences.getString(KEY_BUSINESS_TYPE, DEFAULT_BUSINESS_TYPE)
            ?: DEFAULT_BUSINESS_TYPE
    }

    fun getBusinessSize(): String {
        return sharedPreferences.getString(KEY_BUSINESS_SIZE, DEFAULT_BUSINESS_SIZE)
            ?: DEFAULT_BUSINESS_SIZE
    }

    fun getUserInterests(): Set<String> {
        return sharedPreferences.getStringSet(KEY_USER_INTERESTS, emptySet())
            ?.toSet()
            .orEmpty()
    }

    fun logout() {
        sharedPreferences.edit().clear().apply()
    }

    private companion object {
        const val PREFS_NAME = "MikrotikPrefs"
        const val KEY_IS_LOGGED = "is_logged_in"
        const val KEY_USER_NAME = "user_name"
        const val KEY_USER_EMAIL = "user_email"
        const val KEY_PROFILE_IMAGE_URI = "profile_image_uri"
        const val KEY_USER_PHONE = "user_phone"
        const val KEY_BUSINESS_NAME = "business_name"
        const val KEY_BUSINESS_TYPE = "business_type"
        const val KEY_BUSINESS_SIZE = "business_size"
        const val KEY_USER_INTERESTS = "user_interests"
    }
}
