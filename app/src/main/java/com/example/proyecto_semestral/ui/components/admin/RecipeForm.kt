package com.example.proyecto_semestral.ui.components.admin

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.proyecto_semestral.R
import com.example.proyecto_semestral.ui.theme.ProyectoSemestralTheme

/** Formulario reutilizable para crear y editar recetas basadas en video. */
@Composable
fun RecipeForm(
    recipeTitle: String,
    onRecipeTitleChange: (String) -> Unit,
    videoLink: String,
    onVideoLinkChange: (String) -> Unit,
    description: String,
    onDescriptionChange: (String) -> Unit,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier,
    title: String = "Nueva receta",
    submitLabel: String = "Crear receta",
    videoLinkError: String? = null,
    descriptionError: String? = null,
    supportingMessage: String? = "Todos los campos son obligatorios.",
    titleError: String? = null,
    onCancel: (() -> Unit)? = null,
    submitEnabled: Boolean = true,
    showLogo: Boolean = true,
    additionalFields: @Composable () -> Unit = {}
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 1.dp,
        shadowElevation = 1.dp
    ) {
        Column(modifier = Modifier.padding(28.dp)) {
            if (showLogo) {
                Image(
                    painter = painterResource(R.drawable.logo),
                    contentDescription = "Logo Master Martini",
                    modifier = Modifier
                        .size(width = 180.dp, height = 90.dp)
                        .align(Alignment.CenterHorizontally),
                    contentScale = ContentScale.Fit
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            Text(
                text = title,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = recipeTitle,
                onValueChange = onRecipeTitleChange,
                label = { Text("Título de la receta") },
                singleLine = true,
                isError = titleError != null,
                supportingText = titleError?.let { message -> { Text(message) } },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            VideoLinkTextField(
                value = videoLink,
                onValueChange = onVideoLinkChange,
                errorMessage = videoLinkError
            )
            Spacer(modifier = Modifier.height(22.dp))

            Text(
                text = "Descripción del video",
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = description,
                onValueChange = onDescriptionChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp),
                placeholder = {
                    Text(
                        "Describe el contenido del video",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                isError = descriptionError != null,
                singleLine = false,
                textStyle = MaterialTheme.typography.bodyLarge,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                    errorBorderColor = MaterialTheme.colorScheme.error,
                    cursorColor = MaterialTheme.colorScheme.primary
                )
            )
            if (descriptionError != null) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = descriptionError,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            } else if (supportingMessage != null) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = supportingMessage,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            additionalFields()
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = onSubmit,
                enabled = submitEnabled,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text(submitLabel, style = MaterialTheme.typography.titleMedium)
            }

            if (onCancel != null) {
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedButton(
                    onClick = onCancel,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text("Cancelar", style = MaterialTheme.typography.titleMedium)
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFEAF2EE, widthDp = 430)
@Composable
private fun NewRecipeFormPreview() {
    ProyectoSemestralTheme {
        var link by rememberSaveable { mutableStateOf("") }
        var description by rememberSaveable { mutableStateOf("") }
        RecipeForm(
            recipeTitle = "",
            onRecipeTitleChange = {},
            videoLink = link,
            onVideoLinkChange = { link = it },
            description = description,
            onDescriptionChange = { description = it },
            onSubmit = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFEAF2EE, widthDp = 430)
@Composable
private fun EditRecipeFormPreview() {
    ProyectoSemestralTheme {
        RecipeForm(
            recipeTitle = "Galletas con chips de chocolate",
            onRecipeTitleChange = {},
            videoLink = "https://youtu.be/ejemplo",
            onVideoLinkChange = {},
            description = "Aprende a preparar galletas con chips de chocolate, paso a paso.",
            onDescriptionChange = {},
            onSubmit = {},
            title = "Editar receta",
            submitLabel = "Guardar cambios",
            supportingMessage = null,
            onCancel = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}
