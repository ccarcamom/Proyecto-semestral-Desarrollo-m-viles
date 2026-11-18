package com.example.proyecto_semestral.ui.components

const val NO_BUSINESS = "Sin negocio"
const val BUSINESS_SIZE_PLACEHOLDER = "Selecciona una opción"

val businessTypeOptions = listOf(
    NO_BUSINESS,
    "Pastelería",
    "Panadería",
    "Cafetería",
    "Restaurante",
    "Otro"
)

val businessSizeOptions = listOf(
    BUSINESS_SIZE_PLACEHOLDER,
    "Emprendimiento",
    "Pequeño",
    "Mediano",
    "Grande"
)

/**
 * Normaliza un valor de negocio para enviarlo al backend.
 * Los valores vacíos o de relleno ("Sin negocio", "Selecciona una opción") se envían como null.
 */
fun String.toBusinessValue(): String? =
    trim().takeIf {
        it.isNotEmpty() && it != NO_BUSINESS && it != BUSINESS_SIZE_PLACEHOLDER
    }

fun String.businessTypeForDisplay(): String = ifBlank { NO_BUSINESS }

fun String.businessSizeForDisplay(): String = ifBlank { NO_BUSINESS }
