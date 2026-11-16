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
fun RegisterScreen(
    isLoading: Boolean,
    serverError: String?,
    onRegister: (name: String, email: String, password: String) -> Unit,
    onLoginClick: () -> Unit,
    onInputChanged: () -> Unit,
    modifier: Modifier = Modifier
) {
    var name by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var confirmPassword by rememberSaveable { mutableStateOf("") }
    var nameError by rememberSaveable { mutableStateOf<String?>(null) }
    var emailError by rememberSaveable { mutableStateOf<String?>(null) }
    var passwordError by rememberSaveable { mutableStateOf<String?>(null) }
    var confirmPasswordError by rememberSaveable { mutableStateOf<String?>(null) }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    fun submit() {
        nameError = when {
            name.isBlank() -> "Ingresa tu nombre."
            name.trim().length > 120 -> "El nombre no puede superar 120 caracteres."
            else -> null
        }
        emailError = when {
            email.isBlank() -> "Ingresa tu correo."
            email.trim().length > 254 -> "El correo no puede superar 254 caracteres."
            !Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches() -> "Ingresa un correo válido."
            else -> null
        }
        passwordError = when {
            password.length < 8 -> "Usa al menos 8 caracteres."
            password.length > 72 -> "La contraseña no puede superar 72 caracteres."
            else -> null
        }
        confirmPasswordError = if (confirmPassword != password) {
            "Las contraseñas no coinciden."
        } else {
            null
        }
        if (listOf(nameError, emailError, passwordError, confirmPasswordError).all { it == null }) {
            onRegister(name, email, password)
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
        Spacer(Modifier.height(20.dp))
        Text(
            text = "Crea tu cuenta",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
        )
        Spacer(Modifier.height(20.dp))
        AuthTextField(
            value = name,
            onValueChange = {
                name = it
                nameError = null
                onInputChanged()
            },
            label = "Nombre",
            error = nameError
        )
        Spacer(Modifier.height(10.dp))
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
        Spacer(Modifier.height(10.dp))
        AuthTextField(
            value = password,
            onValueChange = {
                password = it
                passwordError = null
                confirmPasswordError = null
                onInputChanged()
            },
            label = "Contraseña",
            error = passwordError,
            keyboardType = KeyboardType.Password,
            isPassword = true,
            passwordVisible = passwordVisible,
            onPasswordVisibilityChange = { passwordVisible = !passwordVisible }
        )
        Spacer(Modifier.height(10.dp))
        AuthTextField(
            value = confirmPassword,
            onValueChange = {
                confirmPassword = it
                confirmPasswordError = null
                onInputChanged()
            },
            label = "Confirmar contraseña",
            error = confirmPasswordError,
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
        Spacer(Modifier.height(18.dp))
        PrimaryButton(
            text = if (isLoading) "Creando cuenta…" else "Crear cuenta",
            onClick = ::submit,
            enabled = !isLoading
        )
        if (isLoading) {
            Spacer(Modifier.height(12.dp))
            CircularProgressIndicator()
        }
        AuthFooter(
            prompt = "¿Ya tienes una cuenta?",
            action = "Iniciar sesión",
            onClick = onLoginClick
        )
    }
}
