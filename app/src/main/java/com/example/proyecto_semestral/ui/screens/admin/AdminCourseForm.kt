package com.example.proyecto_semestral.ui.screens.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.proyecto_semestral.model.admin.AdminCategory
import com.example.proyecto_semestral.model.admin.AdminCourseDraft
import com.example.proyecto_semestral.ui.components.PrimaryButton

@Composable
fun AdminCourseForm(
    categories: List<AdminCategory>,
    isSaving: Boolean,
    onCancel: () -> Unit,
    onSubmit: (AdminCourseDraft) -> Unit,
    modifier: Modifier = Modifier
) {
    var title by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
    var imageUrl by rememberSaveable { mutableStateOf("") }
    var publishNow by rememberSaveable { mutableStateOf(false) }
    var categoryId by rememberSaveable { mutableStateOf<Long?>(null) }
    var validationError by rememberSaveable { mutableStateOf<String?>(null) }

    val objectives = remember { mutableStateListOf("") }

    fun submit() {
        validationError = when {
            title.isBlank() -> "El título del curso es obligatorio."
            description.isBlank() -> "La descripción del curso es obligatoria."
            categoryId == null -> "Selecciona una categoría."
            else -> null
        }
        val selectedCategory = categoryId ?: return
        if (validationError != null) return

        onSubmit(
            AdminCourseDraft(
                title = title,
                description = description,
                categoryId = selectedCategory,
                imageUrl = imageUrl,
                objectives = objectives.toList(),
                publishNow = publishNow
            )
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .imePadding()
            .padding(horizontal = 22.dp, vertical = 16.dp)
    ) {
        Text(
            text = "Nuevo curso",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
        )
        Text(
            text = "Se guardará en el microservicio de cursos.",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(Modifier.height(20.dp))
        AdminTextField(
            value = title,
            onValueChange = { title = it },
            label = "Título del curso *"
        )
        Spacer(Modifier.height(12.dp))
        AdminTextField(
            value = description,
            onValueChange = { description = it },
            label = "Descripción *",
            singleLine = false
        )
        Spacer(Modifier.height(16.dp))
        AdminCategorySelector(
            categories = categories,
            selectedId = categoryId,
            onSelect = { categoryId = it }
        )
        Spacer(Modifier.height(16.dp))
        AdminTextField(
            value = imageUrl,
            onValueChange = { imageUrl = it },
            label = "URL de la imagen"
        )

        Spacer(Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Publicar de inmediato",
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "Si lo dejas apagado, el curso queda como borrador.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Switch(checked = publishNow, onCheckedChange = { publishNow = it })
        }

        Spacer(Modifier.height(12.dp))
        HorizontalDivider()
        Spacer(Modifier.height(16.dp))

        AdminSectionTitle("Objetivos de aprendizaje")
        objectives.forEachIndexed { index, objective ->
            Column(modifier = Modifier.fillMaxWidth()) {
                AdminTextField(
                    value = objective,
                    onValueChange = { objectives[index] = it },
                    label = "Objetivo ${index + 1}"
                )
                if (objectives.size > 1) {
                    AdminRowAction(
                        text = "Quitar objetivo",
                        onClick = { objectives.removeAt(index) }
                    )
                }
                Spacer(Modifier.height(10.dp))
            }
        }
        AdminRowAction(
            text = "+ Agregar objetivo",
            onClick = { objectives.add("") }
        )

        Text(
            text = "Las lecciones se agregan después de crear el curso.",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(top = 8.dp)
        )

        if (validationError != null) {
            Spacer(Modifier.height(12.dp))
            Text(
                text = validationError.orEmpty(),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(Modifier.height(20.dp))
        PrimaryButton(
            text = if (isSaving) "Guardando…" else "Crear curso",
            onClick = ::submit,
            enabled = !isSaving
        )
        TextButton(
            onClick = onCancel,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cancelar")
        }
        Spacer(Modifier.height(20.dp))
    }
}
