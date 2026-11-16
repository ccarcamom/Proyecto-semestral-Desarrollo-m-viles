package com.example.proyecto_semestral.ui.screens.admin

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.proyecto_semestral.model.admin.AdminCategory
import com.example.proyecto_semestral.model.admin.AdminIngredientDraft
import com.example.proyecto_semestral.model.admin.AdminRecipeDraft
import com.example.proyecto_semestral.model.admin.AdminRecipeImage
import com.example.proyecto_semestral.model.admin.AdminRecipe
import com.example.proyecto_semestral.model.admin.AdminStepDraft
import com.example.proyecto_semestral.ui.components.PrimaryButton

@Composable
fun AdminRecipeForm(
    categories: List<AdminCategory>,
    isSaving: Boolean,
    createdBy: String,
    initialRecipe: AdminRecipe? = null,
    onCancel: () -> Unit,
    onSubmit: (AdminRecipeDraft) -> Unit,
    modifier: Modifier = Modifier
) {
    var title by rememberSaveable(initialRecipe?.id) { mutableStateOf(initialRecipe?.title.orEmpty()) }
    var description by rememberSaveable(initialRecipe?.id) { mutableStateOf(initialRecipe?.description.orEmpty()) }
    var duration by rememberSaveable(initialRecipe?.id) { mutableStateOf(initialRecipe?.duration.orEmpty()) }
    var yield by rememberSaveable(initialRecipe?.id) { mutableStateOf(initialRecipe?.yield.orEmpty()) }
    var videoUrl by rememberSaveable(initialRecipe?.id) { mutableStateOf(initialRecipe?.videoUrl.orEmpty()) }
    var published by rememberSaveable(initialRecipe?.id) { mutableStateOf(initialRecipe?.published ?: true) }
    var categoryId by rememberSaveable(initialRecipe?.id) { mutableStateOf(initialRecipe?.categoryId) }
    var validationError by rememberSaveable { mutableStateOf<String?>(null) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var selectedImage by remember { mutableStateOf<AdminRecipeImage?>(null) }
    val context = LocalContext.current
    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri == null) return@rememberLauncherForActivityResult
        runCatching {
            val bytes = compressAdminImage(context.contentResolver, uri)
            AdminRecipeImage(
                bytes = bytes,
                fileName = "receta.jpg",
                contentType = "image/jpeg"
            )
        }.onSuccess { image ->
            selectedImageUri = uri
            selectedImage = image
            validationError = null
        }.onFailure {
            validationError = "No se pudo leer la imagen seleccionada."
        }
    }

    val ingredients = remember(initialRecipe?.id) {
        mutableStateListOf<AdminIngredientDraft>().apply {
            addAll(initialRecipe?.ingredients.orEmpty())
            if (isEmpty()) add(AdminIngredientDraft("", ""))
        }
    }
    val steps = remember(initialRecipe?.id) {
        mutableStateListOf<String>().apply {
            addAll(initialRecipe?.steps.orEmpty().map { it.description })
            if (isEmpty()) add("")
        }
    }

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
                image = selectedImage,
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
            text = if (initialRecipe == null) "Nueva receta" else "Editar receta",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
        )
        Text(
            text = if (initialRecipe == null) {
                "Se guardará en el microservicio de recetas."
            } else {
                "Modifica los datos guardados en el microservicio."
            },
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
        TextButton(
            onClick = {
                imagePicker.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (selectedImageUri == null) "Seleccionar imagen de la galería" else "Cambiar imagen")
        }
        val imagePreview = selectedImageUri ?: initialRecipe?.imageUrl
        imagePreview?.let { preview ->
            Image(
                painter = rememberAsyncImagePainter(preview),
                contentDescription = "Vista previa de la receta",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(190.dp),
                contentScale = ContentScale.Crop
            )
        }
        if (selectedImageUri != null) {
            AdminRowAction(
                text = "Quitar imagen",
                onClick = {
                    selectedImageUri = null
                    selectedImage = null
                }
            )
        }
        if (selectedImageUri == null && initialRecipe?.imageUrl != null) {
            Text(
                text = "La imagen actual se conservará si no seleccionas otra.",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodySmall
            )
        }

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
            text = if (isSaving) {
                "Guardando…"
            } else if (initialRecipe == null) {
                "Crear receta"
            } else {
                "Guardar cambios"
            },
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
