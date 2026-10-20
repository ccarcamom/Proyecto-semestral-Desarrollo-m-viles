package com.example.proyecto_semestral.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.proyecto_semestral.model.profile.ProfileErrors
import com.example.proyecto_semestral.model.profile.ProfileUiState

class ProfileViewModel : ViewModel() {

    var uiState by mutableStateOf(ProfileUiState())
        private set

    fun initialize(
        name: String,
        email: String,
        phone: String,
        businessName: String,
        businessType: String,
        businessSize: String,
        selectedInterests: Set<String>
    ) {
        uiState = uiState.copy(
            name = name,
            email = email,
            phone = phone,
            businessName = businessName,
            businessType = businessType,
            businessSize = businessSize,
            selectedInterests = selectedInterests
        )
    }

    fun onNameChange(value: String) {
        uiState = uiState.copy(
            name = value,
            errors = uiState.errors.copy(name = null)
        )
    }

    fun onEmailChange(value: String) {
        uiState = uiState.copy(
            email = value,
            errors = uiState.errors.copy(email = null)
        )
    }

    fun onPhoneChange(value: String) {
        uiState = uiState.copy(
            phone = value,
            errors = uiState.errors.copy(phone = null)
        )
    }

    fun onInterestChange(interest: String, selected: Boolean) {
        val updatedInterests = if (selected) {
            uiState.selectedInterests + interest
        } else {
            uiState.selectedInterests - interest
        }
        uiState = uiState.copy(selectedInterests = updatedInterests)
    }

    fun validateProfile(): Boolean {
        val errors = ProfileErrors(
            name = validateName(uiState.name),
            email = validateEmail(uiState.email),
            phone = validatePhone(uiState.phone)
        )
        uiState = uiState.copy(errors = errors)
        return errors.name == null && errors.email == null && errors.phone == null
    }

    private fun validateName(value: String): String? {
        return if (value.isNotBlank()) null else "Ingresa tu nombre."
    }

    private fun validateEmail(value: String): String? {
        return if (value.isValidEmail()) null else "Ingresa un correo válido."
    }

    private fun validatePhone(value: String): String? {
        if (value.isBlank()) return null

        val normalizedPhone = value.filterNot { character ->
            character.isWhitespace() || character == '(' || character == ')' || character == '-'
        }
        val digits = normalizedPhone.removePrefix("+")
        return if (digits.length in 8..15 && digits.all { it.isDigit() }) {
            null
        } else {
            "Ingresa un teléfono válido de 8 a 15 dígitos."
        }
    }

    private fun String.isValidEmail(): Boolean {
        return isNotBlank() && contains("@") && substringAfter("@").contains(".")
    }
}
