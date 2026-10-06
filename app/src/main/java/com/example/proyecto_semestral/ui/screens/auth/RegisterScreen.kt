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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proyecto_semestral.ui.components.AppTextField
import com.example.proyecto_semestral.ui.components.BrandLogo
import com.example.proyecto_semestral.ui.components.PasswordTextField
import com.example.proyecto_semestral.ui.components.PrimaryButton
import com.example.proyecto_semestral.ui.theme.ProyectoSemestralTheme
import com.example.proyecto_semestral.viewmodel.FormularioViewModel

@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onLoginClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val formularioViewModel: FormularioViewModel = viewModel(key = "register_formulario")
    val uiState = formularioViewModel.uiState

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
                value = uiState.name,
                onValueChange = formularioViewModel::onNameChange,
                label = "Nombre",
                placeholder = "Tu nombre",
                isError = uiState.errors.name != null,
                supportingText = uiState.errors.name
            )

            Spacer(modifier = Modifier.height(16.dp))
            AppTextField(
                value = uiState.email,
                onValueChange = formularioViewModel::onEmailChange,
                label = "Correo electronico",
                placeholder = "demo@mastermartini.cl",
                isError = uiState.errors.email != null,
                supportingText = uiState.errors.email
            )

            Spacer(modifier = Modifier.height(16.dp))
            PasswordTextField(
                value = uiState.password,
                onValueChange = formularioViewModel::onPasswordChange,
                isError = uiState.errors.password != null,
                supportingText = uiState.errors.password,
                passwordVisible = uiState.passwordVisible,
                onPasswordVisibilityChange = formularioViewModel::onPasswordVisibilityChange
            )

            Spacer(modifier = Modifier.height(16.dp))
            PasswordTextField(
                value = uiState.confirmPassword,
                onValueChange = formularioViewModel::onConfirmPasswordChange,
                label = "Confirmar contraseña",
                isError = uiState.errors.confirmPassword != null,
                supportingText = uiState.errors.confirmPassword,
                passwordVisible = uiState.confirmPasswordVisible,
                onPasswordVisibilityChange = formularioViewModel::onConfirmPasswordVisibilityChange
            )

            Spacer(modifier = Modifier.height(24.dp))
            PrimaryButton(
                text = "Crear cuenta",
                onClick = {
                    if (formularioViewModel.validateRegister()) {
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
