package com.example.proyecto_semestral.ui.screens.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.proyecto_semestral.ui.components.AppTextField
import com.example.proyecto_semestral.ui.components.BrandLogo
import com.example.proyecto_semestral.ui.components.PasswordTextField
import com.example.proyecto_semestral.ui.components.PrimaryButton
import com.example.proyecto_semestral.ui.theme.ProyectoSemestralTheme

@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onLoginClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var name by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var confirmPassword by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    var confirmPasswordVisible by rememberSaveable { mutableStateOf(false) }
    var showErrors by rememberSaveable { mutableStateOf(false) }

    val nameError = showErrors && name.isBlank()
    val emailError = showErrors && !email.isValidEmail()
    val passwordError = showErrors && password.isBlank()
    val confirmPasswordError = showErrors && (confirmPassword.isBlank() || confirmPassword != password)

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            BrandLogo()

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = "Hagamos crecer tus ideas.",
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Registrate para acceder a recetas, tecnicas y cursos de Master Martini.",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(28.dp))

            AppTextField(
                value = name,
                onValueChange = { name = it },
                label = "Nombre",
                placeholder = "Tu nombre",
                isError = nameError,
                supportingText = if (nameError) "Ingresa tu nombre." else null
            )

            Spacer(modifier = Modifier.height(16.dp))
            AppTextField(
                value = email,
                onValueChange = { email = it },
                label = "Correo electronico",
                placeholder = "demo@mastermartini.cl",
                isError = emailError,
                supportingText = if (emailError) "Ingresa un correo valido." else null
            )

            Spacer(modifier = Modifier.height(16.dp))
            PasswordTextField(
                value = password,
                onValueChange = { password = it },
                isError = passwordError,
                supportingText = if (passwordError) "Ingresa una contraseña." else null,
                passwordVisible = passwordVisible,
                onPasswordVisibilityChange = { passwordVisible = it }
            )

            Spacer(modifier = Modifier.height(16.dp))
            PasswordTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = "Confirmar contraseña",
                isError = confirmPasswordError,
                supportingText = if (confirmPasswordError) {
                    "Las contraseñas deben coincidir."
                } else {
                    null
                },
                passwordVisible = confirmPasswordVisible,
                onPasswordVisibilityChange = { confirmPasswordVisible = it }
            )

            Spacer(modifier = Modifier.height(24.dp))
            PrimaryButton(
                text = "Crear cuenta",
                onClick = {
                    showErrors = true
                    if (
                        name.isNotBlank() &&
                        email.isValidEmail() &&
                        password.isNotBlank() &&
                        confirmPassword == password
                    ) {
                        onRegisterSuccess()
                    }
                }
            )

            Spacer(modifier = Modifier.height(12.dp))
            TextButton(
                onClick = onLoginClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Ya tengo cuenta",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}

private fun String.isValidEmail(): Boolean {
    return isNotBlank() && contains("@") && substringAfter("@").contains(".")
}

@Preview(showBackground = true)
@Composable
private fun RegisterScreenPreview() {
    ProyectoSemestralTheme {
        RegisterScreen(
            onRegisterSuccess = {},
            onLoginClick = {}
        )
    }
}
