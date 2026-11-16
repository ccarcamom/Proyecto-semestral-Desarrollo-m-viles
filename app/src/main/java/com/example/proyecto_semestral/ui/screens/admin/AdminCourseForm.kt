package com.example.proyecto_semestral.ui.screens.admin

import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import com.example.proyecto_semestral.model.admin.AdminCategory
import com.example.proyecto_semestral.model.admin.AdminCourseDraft
import com.example.proyecto_semestral.model.admin.AdminCourse
import com.example.proyecto_semestral.model.admin.AdminCourseImage
import com.example.proyecto_semestral.ui.components.PrimaryButton
import coil.compose.rememberAsyncImagePainter
import java.io.ByteArrayOutputStream

@Composable
fun AdminCourseForm(
    categories: List<AdminCategory>,
    isSaving: Boolean,
    initialCourse: AdminCourse? = null,
    onCancel: () -> Unit,
    onSubmit: (AdminCourseDraft) -> Unit,
    modifier: Modifier = Modifier
) {
    var title by rememberSaveable(initialCourse?.id) { mutableStateOf(initialCourse?.title.orEmpty()) }
    var description by rememberSaveable(initialCourse?.id) { mutableStateOf(initialCourse?.description.orEmpty()) }
    var publishNow by rememberSaveable(initialCourse?.id) { mutableStateOf(initialCourse?.published ?: false) }
    var categoryId by rememberSaveable(initialCourse?.id) { mutableStateOf(initialCourse?.categoryId) }
    var validationError by rememberSaveable { mutableStateOf<String?>(null) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var selectedImage by remember { mutableStateOf<AdminCourseImage?>(null) }
    val context = LocalContext.current
    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri == null) return@rememberLauncherForActivityResult
        runCatching {
            AdminCourseImage(
                bytes = compressCourseImage(context.contentResolver, uri),
                fileName = "curso.jpg",
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

    val objectives = remember(initialCourse?.id) {
        mutableStateListOf<String>().apply {
            addAll(initialCourse?.objectives.orEmpty())
            if (isEmpty()) add("")
        }
    }

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
                imageUrl = "",
                image = selectedImage,
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
            text = if (initialCourse == null) "Nuevo curso" else "Editar curso",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
        )
        Text(
            text = if (initialCourse == null) {
                "Se guardará en el microservicio de cursos."
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
        TextButton(
            onClick = {
                imagePicker.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                if (selectedImageUri == null && initialCourse?.imageUrl == null) {
                    "Seleccionar imagen de la galería"
                } else {
                    "Cambiar imagen"
                }
            )
        }
        val preview = selectedImageUri ?: initialCourse?.imageUrl
        preview?.let {
            Image(
                painter = rememberAsyncImagePainter(it),
                contentDescription = "Vista previa del curso",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(190.dp),
                contentScale = ContentScale.Crop
            )
        }
        if (selectedImageUri != null) {
            AdminRowAction(
                text = "Quitar nueva imagen",
                onClick = {
                    selectedImageUri = null
                    selectedImage = null
                }
            )
        } else if (initialCourse?.imageUrl != null) {
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
            text = if (isSaving) {
                "Guardando…"
            } else if (initialCourse == null) {
                "Crear curso"
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

private fun compressCourseImage(
    resolver: android.content.ContentResolver,
    uri: Uri
): ByteArray {
    val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
    resolver.openInputStream(uri)?.use { BitmapFactory.decodeStream(it, null, bounds) }
        ?: error("No se pudo leer la imagen")

    var sampleSize = 1
    while (bounds.outWidth / sampleSize > 1600 || bounds.outHeight / sampleSize > 1600) {
        sampleSize *= 2
    }

    val options = BitmapFactory.Options().apply { inSampleSize = sampleSize }
    val decoded = resolver.openInputStream(uri)?.use {
        BitmapFactory.decodeStream(it, null, options)
    } ?: error("El formato de la imagen no es compatible")

    val maxSide = maxOf(decoded.width, decoded.height)
    val scaled = if (maxSide > 1600) {
        val ratio = 1600f / maxSide
        Bitmap.createScaledBitmap(
            decoded,
            (decoded.width * ratio).toInt(),
            (decoded.height * ratio).toInt(),
            true
        )
    } else {
        decoded
    }

    return ByteArrayOutputStream().use { output ->
        check(scaled.compress(Bitmap.CompressFormat.JPEG, 85, output))
        if (scaled !== decoded) scaled.recycle()
        decoded.recycle()
        output.toByteArray()
    }
}
