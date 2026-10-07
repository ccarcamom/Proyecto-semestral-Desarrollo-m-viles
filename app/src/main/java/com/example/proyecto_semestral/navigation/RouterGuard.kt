package com.example.proyecto_semestral.navigation

import android.content.Context
import android.content.SharedPreferences

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

    fun getUserName(): String {
        return sharedPreferences.getString(KEY_USER_NAME, null).orEmpty()
    }

    fun logout() {
        sharedPreferences.edit().clear().apply()
    }

    private companion object {
        const val PREFS_NAME = "MikrotikPrefs"
        const val KEY_IS_LOGGED = "is_logged_in"
        const val KEY_USER_NAME = "user_name"
    }
}
