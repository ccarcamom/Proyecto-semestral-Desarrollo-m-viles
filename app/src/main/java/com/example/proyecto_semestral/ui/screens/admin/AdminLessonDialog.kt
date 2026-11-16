package com.example.proyecto_semestral.ui.screens.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.proyecto_semestral.model.admin.AdminLessonDraft

@Composable
fun AdminLessonDialog(
    initialOrder: Int,
    isSaving: Boolean,
    onDismiss: () -> Unit,
    onSubmit: (AdminLessonDraft) -> Unit
) {
    var title by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
    var videoUrl by rememberSaveable { mutableStateOf("") }
    var order by rememberSaveable { mutableStateOf(initialOrder.toString()) }
    var duration by rememberSaveable { mutableStateOf("") }
    var validationError by rememberSaveable { mutableStateOf<String?>(null) }

    fun submit() {
        val parsedOrder = order.toIntOrNull()
        val parsedDuration = duration.toIntOrNull()
        validationError = when {
            title.isBlank() -> "El título es obligatorio."
            videoUrl.isBlank() -> "La URL del video es obligatoria."
            parsedOrder == null || parsedOrder <= 0 -> "El orden debe ser mayor que cero."
            parsedDuration == null || parsedDuration <= 0 -> "La duración debe ser mayor que cero."
            else -> null
        }
        if (validationError != null) return

        onSubmit(
            AdminLessonDraft(
                title = title,
                description = description,
                order = parsedOrder ?: initialOrder,
                videoUrl = videoUrl,
                durationSeconds = parsedDuration ?: 0
            )
        )
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Agregar lección") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                AdminTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = "Título *"
                )
                Spacer(Modifier.height(10.dp))
                AdminTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = "Descripción",
                    singleLine = false
                )
                Spacer(Modifier.height(10.dp))
                AdminTextField(
                    value = videoUrl,
                    onValueChange = { videoUrl = it },
                    label = "URL del video *"
                )
                Spacer(Modifier.height(10.dp))
                AdminTextField(
                    value = order,
                    onValueChange = { order = it },
                    label = "Orden *",
                    keyboardType = KeyboardType.Number
                )
                Spacer(Modifier.height(10.dp))
                AdminTextField(
                    value = duration,
                    onValueChange = { duration = it },
                    label = "Duración en segundos *",
                    keyboardType = KeyboardType.Number
                )
                if (validationError != null) {
                    Spacer(Modifier.height(10.dp))
                    Text(
                        text = validationError.orEmpty(),
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = ::submit, enabled = !isSaving) {
                Text(if (isSaving) "Guardando…" else "Agregar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
