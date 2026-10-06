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
import com.example.proyecto_semestral.viewmodel.FormularioViewModel
import com.example.proyecto_semestral.ui.components.AppTextField
import com.example.proyecto_semestral.ui.components.BrandLogo
import com.example.proyecto_semestral.ui.components.PasswordTextField
import com.example.proyecto_semestral.ui.components.PrimaryButton
import com.example.proyecto_semestral.ui.theme.ProyectoSemestralTheme

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onRegisterClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val formularioViewModel: FormularioViewModel = viewModel(key = "login_formulario")
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

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Bienvenido a Master Martini",
                textAlign = TextAlign.Left
            )

            Text(
                text = "Tu próxima gran creación empieza aquí.",
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Recetas, técnicas y cursos para hacer crecer tu negocio",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

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

            Spacer(modifier = Modifier.height(24.dp))
            PrimaryButton(
                text = "Iniciar sesion",
                onClick = {
                    if (formularioViewModel.validateLogin()) {
                        onLoginSuccess()
                    }
                }
            )

            Spacer(modifier = Modifier.height(12.dp))
            TextButton(
                onClick = onRegisterClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Crear cuenta",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginScreenPreview() {
    ProyectoSemestralTheme {
        LoginScreen(
            onLoginSuccess = {},
            onRegisterClick = {}
        )
    }
}
