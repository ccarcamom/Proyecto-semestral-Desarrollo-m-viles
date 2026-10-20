package com.example.proyecto_semestral.ui.screens.profile

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proyecto_semestral.model.profile.DEFAULT_BUSINESS_SIZE
import com.example.proyecto_semestral.model.profile.DEFAULT_BUSINESS_TYPE
import com.example.proyecto_semestral.ui.components.AppBottomBar
import com.example.proyecto_semestral.ui.components.AppTextField
import com.example.proyecto_semestral.ui.components.BottomBarItem
import com.example.proyecto_semestral.ui.components.InterestSelection
import com.example.proyecto_semestral.ui.components.PrimaryButton
import com.example.proyecto_semestral.ui.theme.ProyectoSemestralTheme
import com.example.proyecto_semestral.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(
    initialName: String = "",
    initialEmail: String = "",
    initialPhone: String = "",
    initialBusinessName: String = "",
    initialBusinessType: String = DEFAULT_BUSINESS_TYPE,
    initialBusinessSize: String = DEFAULT_BUSINESS_SIZE,
    initialInterests: Set<String> = emptySet(),
    onBottomBarItemSelected: (BottomBarItem) -> Unit = {},
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    onSaveChanges: (
        name: String,
        email: String,
        phone: String,
        businessName: String,
        businessType: String,
        businessSize: String,
        interests: Set<String>
    ) -> Unit = { _, _, _, _, _, _, _ -> }
) {
    val profileViewModel: ProfileViewModel = viewModel()
    val uiState = profileViewModel.uiState

    LaunchedEffect(
        initialName,
        initialEmail,
        initialPhone,
        initialBusinessName,
        initialBusinessType,
        initialBusinessSize,
        initialInterests
    ) {
        profileViewModel.initialize(
            name = initialName,
            email = initialEmail,
            phone = initialPhone,
            businessName = initialBusinessName,
            businessType = initialBusinessType,
            businessSize = initialBusinessSize,
            selectedInterests = initialInterests
        )
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            ProfileTopBar(onBack = onBack)
        },
        bottomBar = {
            ProfileBottomArea(
                onSaveChanges = {
                    if (profileViewModel.validateProfile()) {
                        val profile = profileViewModel.uiState
                        onSaveChanges(
                            profile.name.trim(),
                            profile.email.trim(),
                            profile.phone.trim(),
                            profile.businessName.trim(),
                            profile.businessType,
                            profile.businessSize,
                            profile.selectedInterests
                        )
                    }
                },
                onBottomBarItemSelected = onBottomBarItemSelected
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
            Spacer(modifier = Modifier.height(22.dp))

            ProfileSummary(name = uiState.name, email = uiState.email)

            Spacer(modifier = Modifier.height(26.dp))

            SectionTitle(text = "Datos personales")

            Spacer(modifier = Modifier.height(18.dp))

            ProfileField(label = "Nombre") {
                AppTextField(
                    value = uiState.name,
                    onValueChange = profileViewModel::onNameChange,
                    placeholder = "Tu nombre",
                    isError = uiState.errors.name != null,
                    supportingText = uiState.errors.name
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            ProfileField(label = "Correo electrónico") {
                AppTextField(
                    value = uiState.email,
                    onValueChange = profileViewModel::onEmailChange,
                    placeholder = "tu@correo.com",
                    isError = uiState.errors.email != null,
                    supportingText = uiState.errors.email
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            ProfileField(label = "Teléfono (opcional)") {
                AppTextField(
                    value = uiState.phone,
                    onValueChange = profileViewModel::onPhoneChange,
                    placeholder = "+56",
                    isError = uiState.errors.phone != null,
                    supportingText = uiState.errors.phone
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            SectionTitle(text = "Mi negocio")
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Información registrada sobre tu negocio.",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 17.sp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            BusinessInfoRow(
                label = "Nombre del negocio",
                value = uiState.businessName.ifBlank { "Sin información" }
            )
            BusinessInfoRow(label = "Tipo de negocio", value = uiState.businessType)
            BusinessInfoRow(label = "Tamaño del negocio", value = uiState.businessSize)

            Spacer(modifier = Modifier.height(32.dp))

            InterestSelection(
                interests = availableInterests,
                selectedInterests = uiState.selectedInterests,
                onInterestChange = profileViewModel::onInterestChange,
                title = "Mis intereses"
            )

            Spacer(modifier = Modifier.height(28.dp))
        }
    }
}

@Composable
private fun ProfileTopBar(onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(76.dp)
                .padding(horizontal = 22.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .clickable(
                        role = Role.Button,
                        onClickLabel = "Volver",
                        onClick = onBack
                    ),
                contentAlignment = Alignment.Center
            ) {
                BackArrow(
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.size(10.dp))

            Text(
                text = "Mi perfil",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        }

        HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)
    }
}

@Composable
private fun ProfileSummary(name: String, email: String) {
    val displayName = name.ifBlank { "Usuario" }
    val initials = displayName
        .trim()
        .split(Regex("\\s+"))
        .filter { it.isNotBlank() }
        .take(2)
        .joinToString("") { it.first().uppercase() }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(86.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = initials,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        }

        Spacer(modifier = Modifier.size(20.dp))

        Column {
            Text(
                text = displayName,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontSize = 23.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = email.ifBlank { "Sin correo registrado" },
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 17.sp)
            )
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        color = MaterialTheme.colorScheme.primary,
        style = MaterialTheme.typography.headlineSmall.copy(
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold
        )
    )
}

@Composable
private fun ProfileField(
    label: String,
    content: @Composable () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleMedium.copy(
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        content()
    }
}

@Composable
private fun BusinessInfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
            modifier = Modifier.weight(1f),
            textAlign = androidx.compose.ui.text.style.TextAlign.End
        )
    }
    HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)
}

@Composable
private fun ProfileBottomArea(
    onSaveChanges: () -> Unit,
    onBottomBarItemSelected: (BottomBarItem) -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.background,
        shadowElevation = 8.dp
    ) {
        Column {
            PrimaryButton(
                text = "Guardar cambios",
                onClick = onSaveChanges,
                modifier = Modifier
                    .padding(horizontal = 22.dp, vertical = 12.dp)
                    .height(52.dp)
            )
            HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)
            AppBottomBar(
                selectedItem = BottomBarItem.Profile,
                onItemSelected = onBottomBarItemSelected
            )
        }
    }
}

@Composable
private fun BackArrow(color: Color, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val stroke = 2.5.dp.toPx()
        drawLine(
            color = color,
            start = Offset(size.width * 0.22f, size.height * 0.5f),
            end = Offset(size.width * 0.82f, size.height * 0.5f),
            strokeWidth = stroke,
            cap = StrokeCap.Round
        )
        drawLine(
            color = color,
            start = Offset(size.width * 0.22f, size.height * 0.5f),
            end = Offset(size.width * 0.47f, size.height * 0.26f),
            strokeWidth = stroke,
            cap = StrokeCap.Round
        )
        drawLine(
            color = color,
            start = Offset(size.width * 0.22f, size.height * 0.5f),
            end = Offset(size.width * 0.47f, size.height * 0.74f),
            strokeWidth = stroke,
            cap = StrokeCap.Round
        )
    }
}

private val availableInterests = listOf(
    "Chocolatería",
    "Bollería",
    "Pastelería",
    "Panadería"
)

@Preview(showBackground = true, widthDp = 430, heightDp = 900)
@Composable
private fun ProfileScreenPreview() {
    ProyectoSemestralTheme {
        ProfileScreen(
            initialName = "Andrea Demo",
            initialEmail = "andrea@example.com"
        )
    }
}
