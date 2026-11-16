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
import com.example.proyecto_semestral.model.admin.AdminIngredientDraft
import com.example.proyecto_semestral.model.admin.AdminRecipeDraft
import com.example.proyecto_semestral.model.admin.AdminStepDraft
import com.example.proyecto_semestral.ui.components.PrimaryButton

@Composable
fun AdminRecipeForm(
    categories: List<AdminCategory>,
    isSaving: Boolean,
    createdBy: String,
    onCancel: () -> Unit,
    onSubmit: (AdminRecipeDraft) -> Unit,
    modifier: Modifier = Modifier
) {
    var title by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
    var duration by rememberSaveable { mutableStateOf("") }
    var yield by rememberSaveable { mutableStateOf("") }
    var videoUrl by rememberSaveable { mutableStateOf("") }
    var imageUrl by rememberSaveable { mutableStateOf("") }
    var published by rememberSaveable { mutableStateOf(true) }
    var categoryId by rememberSaveable { mutableStateOf<Long?>(null) }
    var validationError by rememberSaveable { mutableStateOf<String?>(null) }

    val ingredients = remember { mutableStateListOf(AdminIngredientDraft("", "")) }
    val steps = remember { mutableStateListOf("") }

    fun submit() {
        validationError = when {
            title.isBlank() -> "El título es obligatorio."
            categoryId == null -> "Selecciona una categoría."
            else -> null
        }
        val selectedCategory = categoryId ?: return
        if (validationError != null) return

        onSubmit(
            AdminRecipeDraft(
                title = title,
                description = description,
                categoryId = selectedCategory,
                duration = duration,
                yield = yield,
                videoUrl = videoUrl,
                imageUrl = imageUrl,
                published = published,
                createdBy = createdBy,
                ingredients = ingredients.toList(),
                steps = steps.map { AdminStepDraft(description = it) }
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
            text = "Nueva receta",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
        )
        Text(
            text = "Se guardará en el microservicio de recetas.",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(Modifier.height(20.dp))
        AdminTextField(
            value = title,
            onValueChange = { title = it },
            label = "Título *"
        )
        Spacer(Modifier.height(12.dp))
        AdminTextField(
            value = description,
            onValueChange = { description = it },
            label = "Descripción",
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
            value = duration,
            onValueChange = { duration = it },
            label = "Duración (ej: 35 min)"
        )
        Spacer(Modifier.height(12.dp))
        AdminTextField(
            value = yield,
            onValueChange = { yield = it },
            label = "Rendimiento (ej: 12 porciones)"
        )
        Spacer(Modifier.height(12.dp))
        AdminTextField(
            value = videoUrl,
            onValueChange = { videoUrl = it },
            label = "URL del video (YouTube)"
        )
        Spacer(Modifier.height(12.dp))
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
            Text(
                text = "Publicar de inmediato",
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodyLarge
            )
            Switch(checked = published, onCheckedChange = { published = it })
        }

        Spacer(Modifier.height(12.dp))
        HorizontalDivider()
        Spacer(Modifier.height(16.dp))

        AdminSectionTitle("Ingredientes")
        ingredients.forEachIndexed { index, ingredient ->
            Column(modifier = Modifier.fillMaxWidth()) {
                AdminTextField(
                    value = ingredient.name,
                    onValueChange = { ingredients[index] = ingredient.copy(name = it) },
                    label = "Ingrediente ${index + 1}"
                )
                Spacer(Modifier.height(8.dp))
                AdminTextField(
                    value = ingredient.amount,
                    onValueChange = { ingredients[index] = ingredient.copy(amount = it) },
                    label = "Cantidad (ej: 200 g)"
                )
                if (ingredients.size > 1) {
                    AdminRowAction(
                        text = "Quitar ingrediente",
                        onClick = { ingredients.removeAt(index) }
                    )
                }
                Spacer(Modifier.height(10.dp))
            }
        }
        AdminRowAction(
            text = "+ Agregar ingrediente",
            onClick = { ingredients.add(AdminIngredientDraft("", "")) }
        )

        Spacer(Modifier.height(16.dp))
        AdminSectionTitle("Pasos de preparación")
        steps.forEachIndexed { index, step ->
            Column(modifier = Modifier.fillMaxWidth()) {
                AdminTextField(
                    value = step,
                    onValueChange = { steps[index] = it },
                    label = "Paso ${index + 1}",
                    singleLine = false
                )
                if (steps.size > 1) {
                    AdminRowAction(
                        text = "Quitar paso",
                        onClick = { steps.removeAt(index) }
                    )
                }
                Spacer(Modifier.height(10.dp))
            }
        }
        AdminRowAction(
            text = "+ Agregar paso",
            onClick = { steps.add("") }
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
            text = if (isSaving) "Guardando…" else "Crear receta",
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
