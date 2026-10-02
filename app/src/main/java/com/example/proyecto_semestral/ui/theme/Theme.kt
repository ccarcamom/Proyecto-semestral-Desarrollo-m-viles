package com.example.proyecto_semestral.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val MasterMartiniLightColorScheme = lightColorScheme(
    primary = MasterMartiniGreen,
    onPrimary = MasterMartiniWhite,
    primaryContainer = MasterMartiniGreenContainer,
    onPrimaryContainer = MasterMartiniGreen,
    secondary = MasterMartiniSecondary,
    onSecondary = MasterMartiniWhite,
    secondaryContainer = MasterMartiniSurfaceVariant,
    onSecondaryContainer = MasterMartiniTextPrimary,
    background = MasterMartiniBackground,
    onBackground = MasterMartiniTextPrimary,
    surface = MasterMartiniWhite,
    onSurface = MasterMartiniTextPrimary,
    surfaceVariant = MasterMartiniSurfaceVariant,
    onSurfaceVariant = MasterMartiniTextSecondary,
    outline = MasterMartiniOutline,
    error = MasterMartiniError,
    onError = MasterMartiniWhite
)

@Composable
fun ProyectoSemestralTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = MasterMartiniLightColorScheme,
        typography = AppTypography,
        shapes = AppShapes,
        content = content
    )
}
