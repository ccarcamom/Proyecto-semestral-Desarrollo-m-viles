package com.example.proyecto_semestral.ui.screens.contact

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyecto_semestral.ui.components.AppBottomBar
import com.example.proyecto_semestral.ui.components.AppTextField
import com.example.proyecto_semestral.ui.components.BottomBarItem
import com.example.proyecto_semestral.ui.components.BrandLogo
import com.example.proyecto_semestral.ui.components.PrimaryButton
import com.example.proyecto_semestral.ui.components.SimulatedConsultationDialog
import com.example.proyecto_semestral.ui.theme.ProyectoSemestralTheme

@Composable
fun ContactScreen(
    initialEmail: String = "",
    onBottomBarItemSelected: (BottomBarItem) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var email by rememberSaveable(initialEmail) { mutableStateOf(initialEmail) }
    var phone by rememberSaveable { mutableStateOf("") }
    var businessName by rememberSaveable { mutableStateOf("") }
    var businessType by rememberSaveable { mutableStateOf(businessTypes.first()) }
    var businessSize by rememberSaveable { mutableStateOf(businessSizes.first()) }
    var description by rememberSaveable { mutableStateOf("") }
    var emailError by rememberSaveable { mutableStateOf<String?>(null) }
    var descriptionError by rememberSaveable { mutableStateOf<String?>(null) }
    var showConfirmation by rememberSaveable { mutableStateOf(false) }

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

            Spacer(modifier = Modifier.height(26.dp))

            ContactField(label = "Correo electrónico") {
                AppTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        emailError = null
                    },
                    placeholder = "tu@correo.com",
                    isError = emailError != null,
                    supportingText = emailError
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            ContactField(label = "Número de teléfono") {
                AppTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    placeholder = "+56"
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            ContactField(label = "Nombre del negocio") {
                AppTextField(
                    value = businessName,
                    onValueChange = { businessName = it },
                    placeholder = "Nombre de tu negocio"
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            ContactField(label = "Tipo de negocio") {
                ContactDropdown(
                    value = businessType,
                    options = businessTypes,
                    onValueChange = { businessType = it }
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            ContactField(label = "Tamaño del negocio") {
                ContactDropdown(
                    value = businessSize,
                    options = businessSizes,
                    onValueChange = { businessSize = it }
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            ContactField(label = "Descripción") {
                AppTextField(
                    value = description,
                    onValueChange = {
                        description = it
                        descriptionError = null
                    },
                    placeholder = "Cuéntanos tu consulta...",
                    modifier = Modifier.height(170.dp),
                    isError = descriptionError != null,
                    supportingText = descriptionError
                )
            }

            Spacer(modifier = Modifier.height(26.dp))

            PrimaryButton(
                text = "Enviar consulta",
                onClick = {
                    emailError = if (email.isValidEmail()) null else "Ingresa un correo válido."
                    descriptionError = if (description.isBlank()) "Escribe tu consulta." else null
                    if (emailError == null && descriptionError == null) {
                        showConfirmation = true
                    }
                },
                modifier = Modifier.height(58.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    if (showConfirmation) {
        SimulatedConsultationDialog(
            onDismissRequest = { showConfirmation = false },
            onConfirm = { showConfirmation = false },
            title = "¡Gracias por contactarte con nosotros!",
            message = "Pronto recibirás una respuesta."
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

@Composable
private fun ContactDropdown(
    value: String,
    options: List<String>,
    onValueChange: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .clip(RoundedCornerShape(12.dp))
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.55f),
                    shape = RoundedCornerShape(12.dp)
                )
                .clickable { expanded = true }
                .padding(horizontal = 18.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = value,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp)
            )
            DownArrow(
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(22.dp)
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onValueChange(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun DownArrow(color: Color, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val stroke = 2.4.dp.toPx()
        drawLine(
            color = color,
            start = Offset(size.width * 0.22f, size.height * 0.36f),
            end = Offset(size.width * 0.5f, size.height * 0.64f),
            strokeWidth = stroke,
            cap = StrokeCap.Round
        )
        drawLine(
            color = color,
            start = Offset(size.width * 0.5f, size.height * 0.64f),
            end = Offset(size.width * 0.78f, size.height * 0.36f),
            strokeWidth = stroke,
            cap = StrokeCap.Round
        )
    }
}

private fun String.isValidEmail(): Boolean {
    return isNotBlank() && contains("@") && substringAfter("@").contains(".")
}

private val businessTypes = listOf(
    "Sin negocio",
    "Pastelería",
    "Panadería",
    "Cafetería",
    "Restaurante",
    "Otro"
)

private val businessSizes = listOf(
    "Selecciona una opción",
    "Emprendimiento",
    "Pequeño",
    "Mediano",
    "Grande"
)

@Preview(showBackground = true, widthDp = 430, heightDp = 900)
@Composable
private fun ContactScreenPreview() {
    ProyectoSemestralTheme {
        ContactScreen(initialEmail = "andrea@example.com")
    }
}
