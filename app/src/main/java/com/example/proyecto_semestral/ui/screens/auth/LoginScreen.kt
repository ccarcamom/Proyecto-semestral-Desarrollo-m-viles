package com.example.proyecto_semestral.ui.screens.auth

import android.util.Patterns
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.proyecto_semestral.ui.components.BrandLogo
import com.example.proyecto_semestral.ui.components.PrimaryButton

@Composable
fun LoginScreen(
    isLoading: Boolean,
    serverError: String?,
    onLogin: (email: String, password: String) -> Unit,
    onRegisterClick: () -> Unit,
    onInputChanged: () -> Unit,
    onGuestClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var emailError by rememberSaveable { mutableStateOf<String?>(null) }
    var passwordError by rememberSaveable { mutableStateOf<String?>(null) }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    fun submit() {
        emailError = when {
            email.isBlank() -> "Ingresa tu correo."
            !Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches() -> "Ingresa un correo válido."
            else -> null
        }
        passwordError = if (password.isBlank()) "Ingresa tu contraseña." else null
        if (emailError == null && passwordError == null) {
            onLogin(email, password)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .imePadding()
            .padding(horizontal = 24.dp, vertical = 32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BrandLogo()
        Spacer(Modifier.height(24.dp))
        Text(
            text = "Inicia sesión",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
        )
        Text(
            text = "Continúa aprendiendo y creando.",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(Modifier.height(24.dp))
        AuthTextField(
            value = email,
            onValueChange = {
                email = it
                emailError = null
                onInputChanged()
            },
            label = "Correo electrónico",
            error = emailError,
            keyboardType = KeyboardType.Email
        )
        Spacer(Modifier.height(12.dp))
        AuthTextField(
            value = password,
            onValueChange = {
                password = it
                passwordError = null
                onInputChanged()
            },
            label = "Contraseña",
            error = passwordError,
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done,
            isPassword = true,
            passwordVisible = passwordVisible,
            onPasswordVisibilityChange = { passwordVisible = !passwordVisible }
        )
        if (serverError != null) {
            Spacer(Modifier.height(12.dp))
            AuthErrorMessage(serverError)
        }
        Spacer(Modifier.height(20.dp))
        PrimaryButton(
            text = if (isLoading) "Iniciando sesión…" else "Iniciar sesión",
            onClick = ::submit,
            enabled = !isLoading
        )
        if (isLoading) {
            Spacer(Modifier.height(12.dp))
            CircularProgressIndicator()
        }
        AuthFooter(
            prompt = "¿Aún no tienes una cuenta?",
            action = "Crear cuenta",
            onClick = onRegisterClick
        )
        AuthFooter(
            prompt = "¿Solo quieres explorar?",
            action = "Continuar como invitado",
            onClick = onGuestClick
        )
    }
}
