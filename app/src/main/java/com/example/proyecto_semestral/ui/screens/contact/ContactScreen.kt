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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
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
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proyecto_semestral.domain.repository.ContactDraft
import com.example.proyecto_semestral.ui.components.AppTopBar
import com.example.proyecto_semestral.ui.components.PrimaryButton
import com.example.proyecto_semestral.ui.viewmodel.contact.ContactViewModel

@Composable
fun ContactScreen(
    viewModelFactory: ViewModelProvider.Factory,
    userId: Long?,
    initialEmail: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel: ContactViewModel = viewModel(factory = viewModelFactory)
    val state by viewModel.uiState.collectAsState()
    var email by rememberSaveable { mutableStateOf(initialEmail) }
    var phone by rememberSaveable { mutableStateOf("") }
    var businessName by rememberSaveable { mutableStateOf("") }
    var businessType by rememberSaveable { mutableStateOf("") }
    var businessSize by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
    var validationError by rememberSaveable { mutableStateOf<String?>(null) }

    Column(modifier = modifier.fillMaxSize()) {
        AppTopBar(title = "Contacto", onBackClick = onBack)
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .imePadding()
                .padding(horizontal = 22.dp, vertical = 12.dp)
        ) {
            Text(
                text = if (state.sent) "Consulta enviada" else "Cuéntanos sobre tu negocio",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = if (state.sent) {
                    "Recibimos tu mensaje. Nuestro equipo podrá revisarlo y contactarte."
                } else {
                    "Completa el formulario y nos pondremos en contacto contigo."
                },
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            if (!state.sent) {
                Spacer(Modifier.height(20.dp))
                ContactField(email, { email = it; viewModel.clearError() }, "Correo *", KeyboardType.Email)
                ContactField(phone, { phone = it }, "Teléfono", KeyboardType.Phone)
                ContactField(businessName, { businessName = it }, "Nombre del negocio *")
                ContactField(businessType, { businessType = it }, "Tipo de negocio")
                ContactField(businessSize, { businessSize = it }, "Tamaño del negocio")
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("¿Cómo podemos ayudarte? *") },
                    minLines = 5,
                    modifier = Modifier.fillMaxWidth()
                )
                val error = validationError ?: state.error
                if (error != null) {
                    Spacer(Modifier.height(10.dp))
                    Text(error, color = MaterialTheme.colorScheme.error)
                }
                Spacer(Modifier.height(18.dp))
                PrimaryButton(
                    text = if (state.isSending) "Enviando…" else "Enviar consulta",
                    enabled = !state.isSending,
                    onClick = {
                        validationError = when {
                            email.isBlank() || !Patterns.EMAIL_ADDRESS.matcher(email).matches() ->
                                "Ingresa un correo válido."
                            businessName.isBlank() -> "El nombre del negocio es obligatorio."
                            description.isBlank() -> "Describe cómo podemos ayudarte."
                            else -> null
                        }
                        if (validationError == null) {
                            viewModel.send(
                                ContactDraft(
                                    userId = userId,
                                    email = email,
                                    phone = phone,
                                    businessName = businessName,
                                    businessType = businessType,
                                    businessSize = businessSize,
                                    description = description
                                )
                            )
                        }
                    }
                )
            } else {
                Spacer(Modifier.height(24.dp))
                PrimaryButton(text = "Volver al inicio", onClick = onBack)
            }
            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun ContactField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        modifier = Modifier.fillMaxWidth()
    )
    Spacer(Modifier.height(12.dp))
}
