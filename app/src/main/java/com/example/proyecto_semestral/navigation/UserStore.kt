package com.example.proyecto_semestral.navigation

object UserStore {
    private val users = mutableListOf<User>()

    fun registerUser(name: String, email: String, password: String): Boolean {
        val normalizedEmail = email.normalizeEmail()
        if (users.any { it.email == normalizedEmail }) {
            return false
        }

        users.add(
            User(
                name = name.trim(),
                email = normalizedEmail,
                password = password
            )
        )
        return true
    }

    fun findUser(email: String, password: String): User? {
        val normalizedEmail = email.normalizeEmail()
        return users.firstOrNull { user ->
            user.email == normalizedEmail && user.password == password
        }
    }

    private fun String.normalizeEmail(): String {
        return trim().lowercase()
    }
}

data class User(
    val name: String,
    val email: String,
    val password: String
)
