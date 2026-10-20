package com.example.proyecto_semestral.model.profile

data class ProfileUiState(
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val businessName: String = "",
    val businessType: String = DEFAULT_BUSINESS_TYPE,
    val businessSize: String = DEFAULT_BUSINESS_SIZE,
    val errors: ProfileErrors = ProfileErrors()
)

const val DEFAULT_BUSINESS_TYPE = "Sin negocio"
const val DEFAULT_BUSINESS_SIZE = "Selecciona una opción"
