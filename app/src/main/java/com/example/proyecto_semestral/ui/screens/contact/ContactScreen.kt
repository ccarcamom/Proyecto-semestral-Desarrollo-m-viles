package com.example.proyecto_semestral.ui.screens.contact

import android.util.Patterns
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proyecto_semestral.domain.repository.ContactDraft
import com.example.proyecto_semestral.ui.components.AppBottomBar
import com.example.proyecto_semestral.ui.components.AppTextField
import com.example.proyecto_semestral.ui.components.BottomBarItem
import com.example.proyecto_semestral.ui.components.BrandLogo
import com.example.proyecto_semestral.ui.components.NO_BUSINESS
import com.example.proyecto_semestral.ui.components.PrimaryButton
import com.example.proyecto_semestral.ui.viewmodel.contact.ContactViewModel

@Composable
fun ContactScreen(
    viewModelFactory: ViewModelProvider.Factory,
    userId: Long?,
    initialEmail: String,
    businessName: String,
    businessType: String,
    businessSize: String,
    onBottomBarItemSelected: (BottomBarItem) -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel: ContactViewModel = viewModel(factory = viewModelFactory)
    val state by viewModel.uiState.collectAsState()
    var email by rememberSaveable(initialEmail) { mutableStateOf(initialEmail) }
    var phone by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
    var emailError by rememberSaveable { mutableStateOf<String?>(null) }
    var descriptionError by rememberSaveable { mutableStateOf<String?>(null) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            AppBottomBar(
                selectedItem = BottomBarItem.Contact,
                onItemSelected = onBottomBarItemSelected
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .imePadding()
                .padding(horizontal = 22.dp)
        ) {
            Spacer(modifier = Modifier.height(14.dp))
            BrandLogo(tagline = "ESTAMOS PARA AYUDARTE")
            Spacer(modifier = Modifier.height(28.dp))
            Text(
                text = "Conversemos.",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontSize = 36.sp,
                    lineHeight = 42.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Cuéntanos qué necesitas para dar el próximo paso en tu negocio.",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 19.sp,
                    lineHeight = 27.sp
                )
            )
            Spacer(modifier = Modifier.height(18.dp))
            Text(
                text = if (businessName.isNotBlank()) {
                    "Usaremos los datos de tu negocio ($businessName) guardados en tu perfil."
                } else {
                    "Completa los datos de tu negocio en tu perfil para asociarlos a esta consulta."
                },
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(22.dp))

            ContactField("Correo electrónico *") {
                AppTextField(
                    value = email,
                    onValueChange = {
                        email = it.take(254)
                        emailError = null
                        viewModel.clearError()
                    },
                    placeholder = "tu@correo.com",
                    keyboardType = KeyboardType.Email,
                    isError = emailError != null,
                    supportingText = emailError
                )
            }
            Spacer(modifier = Modifier.height(18.dp))
            ContactField("Número de teléfono") {
                AppTextField(
                    value = phone,
                    onValueChange = { phone = it.take(30) },
                    placeholder = "+56",
                    keyboardType = KeyboardType.Phone
                )
            }
            Spacer(modifier = Modifier.height(18.dp))
            ContactField("Descripción *") {
                AppTextField(
                    value = description,
                    onValueChange = {
                        description = it.take(5000)
                        descriptionError = null
                    },
                    placeholder = "Cuéntanos tu consulta...",
                    modifier = Modifier.height(170.dp),
                    isError = descriptionError != null,
                    supportingText = descriptionError
                )
            }

            state.error?.let { error ->
                Spacer(modifier = Modifier.height(12.dp))
                Text(error, color = MaterialTheme.colorScheme.error)
            }
            Spacer(modifier = Modifier.height(26.dp))
            PrimaryButton(
                text = if (state.isSending) "Enviando…" else "Enviar consulta",
                enabled = !state.isSending,
                onClick = {
                    emailError = if (
                        email.isBlank() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()
                    ) "Ingresa un correo válido." else null
                    descriptionError = if (description.isBlank()) {
                        "Escribe tu consulta."
                    } else null

                    if (emailError == null && descriptionError == null) {
                        viewModel.send(
                            ContactDraft(
                                userId = userId,
                                email = email.trim(),
                                phone = phone.trim(),
                                businessName = businessName.ifBlank { NO_BUSINESS },
                                businessType = businessType,
                                businessSize = businessSize,
                                description = description.trim()
                            )
                        )
                    }
                },
                modifier = Modifier.height(58.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    state.receipt?.let { receipt ->
        AlertDialog(
            onDismissRequest = {},
            title = { Text("¡Consulta enviada!") },
            text = {
                Text(
                    "Recibimos tu solicitud N.º ${receipt.id}. " +
                        "Estado: ${receipt.status.ifBlank { "PENDIENTE" }}."
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.reset()
                        onBottomBarItemSelected(BottomBarItem.Home)
                    }
                ) {
                    Text("Volver al inicio")
                }
            }
        )
    }
}

@Composable
private fun ContactField(
    label: String,
    content: @Composable () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleMedium.copy(
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        content()
    }
}
