package com.example.proyecto_semestral.ui.screens.profile

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.proyecto_semestral.model.auth.User
import com.example.proyecto_semestral.ui.components.AppBottomBar
import com.example.proyecto_semestral.ui.components.AppTextField
import com.example.proyecto_semestral.ui.components.BUSINESS_SIZE_PLACEHOLDER
import com.example.proyecto_semestral.ui.components.BottomBarItem
import com.example.proyecto_semestral.ui.components.BrandLogo
import com.example.proyecto_semestral.ui.components.NO_BUSINESS
import com.example.proyecto_semestral.ui.components.PrimaryButton
import com.example.proyecto_semestral.ui.components.SelectionField
import com.example.proyecto_semestral.ui.components.businessSizeForDisplay
import com.example.proyecto_semestral.ui.components.businessSizeOptions
import com.example.proyecto_semestral.ui.components.businessTypeForDisplay
import com.example.proyecto_semestral.ui.components.businessTypeOptions
import com.example.proyecto_semestral.ui.components.compressImage
import com.example.proyecto_semestral.ui.components.toBusinessValue

@Composable
fun ProfileScreen(
    user: User?,
    isSavingProfile: Boolean,
    profileMessage: String?,
    error: String?,
    onSaveProfile: (
        name: String,
        businessName: String,
        businessType: String,
        businessSize: String
    ) -> Unit,
    onPhotoSelected: (ByteArray) -> Unit,
    onClearFeedback: () -> Unit,
    onLoginClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onBottomBarItemSelected: (BottomBarItem) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            AppBottomBar(
                selectedItem = BottomBarItem.Profile,
                onItemSelected = onBottomBarItemSelected
            )
        }
    ) { innerPadding ->
        if (user == null) {
            GuestProfileContent(
                onLoginClick = onLoginClick,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 22.dp)
            )
        } else {
            AuthenticatedProfileContent(
                user = user,
                isSavingProfile = isSavingProfile,
                profileMessage = profileMessage,
                error = error,
                onSaveProfile = onSaveProfile,
                onPhotoSelected = onPhotoSelected,
                onClearFeedback = onClearFeedback,
                onLogoutClick = onLogoutClick,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 22.dp)
            )
        }
    }
}

@Composable
private fun GuestProfileContent(
    onLoginClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(14.dp))
        BrandLogo()
        Spacer(Modifier.height(48.dp))
        ProfileAvatar(initial = null, photoUrl = null)
        Spacer(Modifier.height(24.dp))
        Text(
            text = "Tu perfil",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
        )
        Spacer(Modifier.height(10.dp))
        Text(
            text = "Inicia sesión para ver tus datos, personalizar tu foto y " +
                "administrar la información de tu negocio.",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(Modifier.height(28.dp))
        PrimaryButton(
            text = "Iniciar sesión",
            onClick = onLoginClick,
            modifier = Modifier.height(56.dp)
        )
        Spacer(Modifier.height(24.dp))
    }
}

@Composable
private fun AuthenticatedProfileContent(
    user: User,
    isSavingProfile: Boolean,
    profileMessage: String?,
    error: String?,
    onSaveProfile: (
        name: String,
        businessName: String,
        businessType: String,
        businessSize: String
    ) -> Unit,
    onPhotoSelected: (ByteArray) -> Unit,
    onClearFeedback: () -> Unit,
    onLogoutClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var editing by rememberSaveable(user.id) { mutableStateOf(false) }
    var name by rememberSaveable(user.id, user.name) { mutableStateOf(user.name) }
    var businessName by rememberSaveable(user.id, user.businessName) {
        mutableStateOf(user.businessName)
    }
    var businessType by rememberSaveable(user.id, user.businessType) {
        mutableStateOf(user.businessType.businessTypeForDisplay())
    }
    var businessSize by rememberSaveable(user.id, user.businessSize) {
        mutableStateOf(
            user.businessSize.ifBlank { BUSINESS_SIZE_PLACEHOLDER }
        )
    }
    var nameError by rememberSaveable { mutableStateOf<String?>(null) }
    var photoError by rememberSaveable { mutableStateOf<String?>(null) }

    LaunchedEffect(profileMessage) {
        if (profileMessage != null) {
            editing = false
        }
    }

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri == null) return@rememberLauncherForActivityResult
        photoError = null
        onClearFeedback()
        runCatching {
            compressImage(
                resolver = context.contentResolver,
                uri = uri,
                maxSide = 1024,
                jpegQuality = 85
            )
        }.onSuccess(onPhotoSelected)
            .onFailure { photoError = "No se pudo leer la imagen seleccionada." }
    }

    fun cancelEditing() {
        name = user.name
        businessName = user.businessName
        businessType = user.businessType.businessTypeForDisplay()
        businessSize = user.businessSize.ifBlank { BUSINESS_SIZE_PLACEHOLDER }
        nameError = null
        editing = false
        onClearFeedback()
    }

    fun save() {
        nameError = when {
            name.isBlank() -> "Ingresa tu nombre."
            name.trim().length > 120 -> "El nombre no puede superar 120 caracteres."
            else -> null
        }
        if (nameError != null) return

        onSaveProfile(
            name.trim(),
            businessName.trim(),
            businessType.toBusinessValue().orEmpty(),
            businessSize.toBusinessValue().orEmpty()
        )
    }

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .imePadding()
    ) {
        Spacer(Modifier.height(14.dp))
        BrandLogo()

        Spacer(Modifier.height(30.dp))
        Text(
            text = "Mi perfil",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
        )
        Spacer(Modifier.height(22.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            ProfileAvatar(
                initial = user.name.trim().firstOrNull()?.uppercaseChar(),
                photoUrl = user.profilePhotoUrl
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = user.name,
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = user.email,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(8.dp))
                RoleBadge(role = user.role)
            }
        }
        Spacer(Modifier.height(14.dp))
        OutlinedButton(
            onClick = {
                photoError = null
                onClearFeedback()
                imagePicker.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            },
            enabled = !isSavingProfile,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (isSavingProfile) "Guardando…" else "Cambiar foto de perfil")
        }
        photoError?.let { message ->
            Spacer(Modifier.height(6.dp))
            Text(
                text = message,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(Modifier.height(24.dp))
        HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.35f))
        Spacer(Modifier.height(20.dp))

        SectionTitle("Datos personales")
        Spacer(Modifier.height(12.dp))
        if (editing) {
            ProfileField("Nombre") {
                AppTextField(
                    value = name,
                    onValueChange = {
                        name = it.take(120)
                        nameError = null
                        onClearFeedback()
                    },
                    placeholder = "Tu nombre",
                    isError = nameError != null,
                    supportingText = nameError
                )
            }
            Spacer(Modifier.height(12.dp))
            ProfileField("Correo electrónico") {
                AppTextField(
                    value = user.email,
                    onValueChange = {},
                    enabled = false
                )
            }
        } else {
            InfoRow(label = "Nombre", value = user.name)
            Spacer(Modifier.height(10.dp))
            InfoRow(label = "Correo electrónico", value = user.email)
        }

        Spacer(Modifier.height(24.dp))
        HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.35f))
        Spacer(Modifier.height(20.dp))

        SectionTitle("Datos del negocio")
        Spacer(Modifier.height(12.dp))
        if (editing) {
            ProfileField("Nombre del negocio") {
                AppTextField(
                    value = businessName,
                    onValueChange = {
                        businessName = it.take(150)
                        onClearFeedback()
                    },
                    placeholder = "Nombre de tu negocio"
                )
            }
            Spacer(Modifier.height(12.dp))
            ProfileField("Tipo de negocio") {
                SelectionField(
                    value = businessType,
                    options = businessTypeOptions,
                    onValueChange = {
                        businessType = it
                        onClearFeedback()
                    }
                )
            }
            Spacer(Modifier.height(12.dp))
            ProfileField("Tamaño del negocio") {
                SelectionField(
                    value = businessSize,
                    options = businessSizeOptions,
                    onValueChange = {
                        businessSize = it
                        onClearFeedback()
                    }
                )
            }
        } else {
            InfoRow(
                label = "Nombre del negocio",
                value = user.businessName.ifBlank { NO_BUSINESS }
            )
            Spacer(Modifier.height(10.dp))
            InfoRow(
                label = "Tipo de negocio",
                value = user.businessType.businessTypeForDisplay()
            )
            Spacer(Modifier.height(10.dp))
            InfoRow(
                label = "Tamaño del negocio",
                value = user.businessSize.businessSizeForDisplay()
            )
        }

        profileMessage?.let { message ->
            Spacer(Modifier.height(16.dp))
            Text(
                text = message,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodyMedium
            )
        }
        error?.let { message ->
            Spacer(Modifier.height(16.dp))
            Text(
                text = message,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(Modifier.height(24.dp))
        if (editing) {
            PrimaryButton(
                text = if (isSavingProfile) "Guardando…" else "Guardar cambios",
                onClick = ::save,
                enabled = !isSavingProfile,
                modifier = Modifier.height(56.dp)
            )
            TextButton(
                onClick = ::cancelEditing,
                enabled = !isSavingProfile,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cancelar")
            }
        } else {
            PrimaryButton(
                text = "Editar perfil",
                onClick = {
                    onClearFeedback()
                    editing = true
                },
                modifier = Modifier.height(56.dp)
            )
        }
        Spacer(Modifier.height(8.dp))
        TextButton(
            onClick = onLogoutClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cerrar sesión")
        }
        Spacer(Modifier.height(24.dp))
    }
}

@Composable
private fun ProfileAvatar(
    initial: Char?,
    photoUrl: String?
) {
    Box(
        modifier = Modifier
            .size(112.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primaryContainer)
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.35f),
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        if (!photoUrl.isNullOrBlank()) {
            AsyncImage(
                model = photoUrl,
                contentDescription = "Foto de perfil",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else if (initial != null) {
            Text(
                text = initial.toString(),
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Bold)
            )
        } else {
            Text(
                text = "?",
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                style = MaterialTheme.typography.displaySmall.copy(
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}

@Composable
private fun RoleBadge(role: String) {
    val label = if (role.equals("ADMIN", ignoreCase = true)) "Administrador" else "Cliente"
    Text(
        text = label,
        color = MaterialTheme.colorScheme.onPrimaryContainer,
        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(horizontal = 12.dp, vertical = 5.dp)
    )
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        color = MaterialTheme.colorScheme.primary,
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
    )
}

@Composable
private fun InfoRow(label: String, value: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodySmall
        )
        Spacer(Modifier.height(2.dp))
        Text(
            text = value,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium)
        )
    }
}

@Composable
private fun ProfileField(label: String, content: @Composable () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(Modifier.height(6.dp))
        content()
    }
}
