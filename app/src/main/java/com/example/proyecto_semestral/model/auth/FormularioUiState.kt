package com.example.proyecto_semestral.model.auth

data class FormularioUiState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val businessName: String = "",
    val businessType: String = "Sin negocio",
    val businessSize: String = "Selecciona una opción",
    val passwordVisible: Boolean = false,
    val confirmPasswordVisible: Boolean = false,
    val errors: FormularioErrores = FormularioErrores()
)
