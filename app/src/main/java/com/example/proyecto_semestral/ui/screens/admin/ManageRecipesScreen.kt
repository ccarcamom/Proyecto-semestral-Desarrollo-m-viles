package com.example.proyecto_semestral.ui.screens.admin

import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.window.Dialog
import androidx.compose.material3.Button
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proyecto_semestral.model.admin.AppRecipe
import com.example.proyecto_semestral.ui.components.admin.AdminNavigationToggle
import com.example.proyecto_semestral.ui.components.admin.AdminSection
import com.example.proyecto_semestral.ui.components.admin.RecipeForm
import com.example.proyecto_semestral.ui.theme.ProyectoSemestralTheme
import com.example.proyecto_semestral.viewmodel.admin.ManageRecipesViewModel

@Composable
fun ManageRecipesScreen(
    modifier: Modifier = Modifier,
    onNavigateToUsers: () -> Unit = {},
    onLogout: () -> Unit = {},
    manageRecipesViewModel: ManageRecipesViewModel = viewModel()
) {
    val recipes by manageRecipesViewModel.recipes.collectAsState()
    var showForm by rememberSaveable { mutableStateOf(false) }
    var recipeTitle by rememberSaveable { mutableStateOf("") }
    var titleError by rememberSaveable { mutableStateOf<String?>(null) }
    var videoLink by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
    var editingId by rememberSaveable { mutableStateOf<String?>(null) }
    var linkError by rememberSaveable { mutableStateOf<String?>(null) }
    var descriptionError by rememberSaveable { mutableStateOf<String?>(null) }

    fun clearForm() {
        showForm = false
        recipeTitle = ""
        titleError = null
        videoLink = ""
        description = ""
        editingId = null
        linkError = null
        descriptionError = null
    }

    fun submitRecipe() {
        titleError = if (recipeTitle.isBlank()) "Ingresa el título de la receta." else null
        linkError = if (videoLink.startsWith("http://") || videoLink.startsWith("https://")) null else "Ingresa un enlace válido."
        descriptionError = if (description.isBlank()) "Ingresa una descripción." else null
        if (linkError != null || descriptionError != null || titleError != null) return

        val id = editingId
        if (id == null) {
            manageRecipesViewModel.createRecipe(
                title = recipeTitle,
                videoUrl = videoLink,
                description = description
            )
        } else {
            manageRecipesViewModel.updateRecipe(
                title = recipeTitle,
                id = id,
                videoUrl = videoLink,
                description = description
            )
        }
        clearForm()
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(
                start = 20.dp,
                end = 20.dp,
                top = 12.dp,
                bottom = 28.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { AdminHeader(onLogout = onLogout) }
            item {
                Column {
                    Text(
                        text = "ADMINISTRACIÓN",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.labelLarge.copy(letterSpacing = 2.5.sp)
                    )
                    Spacer(Modifier.height(12.dp))
                    Text(
                        text = "Gestionar recetas",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontSize = 36.sp,
                            lineHeight = 42.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        text = "Crea y actualiza tus videos de recetas.",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyLarge.copy(fontSize = 19.sp)
                    )
                }
            }
            item {
                AdminNavigationToggle(
                    selectedSection = AdminSection.RECIPES,
                    onSectionSelected = { if (it == AdminSection.USERS) onNavigateToUsers() }
                )
            }
            item {
                Button(
                    onClick = { clearForm(); showForm = true },
                    modifier = Modifier.fillMaxWidth()
                ) { Text("Crear receta") }
            }
            item {
                Column(modifier = Modifier.padding(top = 10.dp)) {
                    Text(
                        text = "Recetas publicadas",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "${recipes.size} recetas",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
            items(recipes, key = { it.id }) { recipe ->
                PublishedRecipeCard(
                    recipe = recipe,
                    onEdit = {
                        recipeTitle = recipe.title
                        titleError = null
                        showForm = true
                        editingId = recipe.id
                        videoLink = recipe.videoUrl
                        description = recipe.description
                        linkError = null
                        descriptionError = null
                    }
                )
            }
            item {
                AdminInfoBanner("Al editar podrás cambiar el título, el link y la descripción.")
            }
        }
    }
    if (showForm) {
        Dialog(onDismissRequest = ::clearForm) {
            RecipeForm(
                recipeTitle = recipeTitle,
                onRecipeTitleChange = { recipeTitle = it; titleError = null },
                videoLink = videoLink,
                onVideoLinkChange = { videoLink = it; linkError = null },
                description = description,
                onDescriptionChange = { description = it.take(500); descriptionError = null },
                onSubmit = ::submitRecipe,
                title = if (editingId == null) "Nueva receta" else "Editar receta",
                submitLabel = if (editingId == null) "Crear receta" else "Guardar cambios",
                titleError = titleError,
                videoLinkError = linkError,
                descriptionError = descriptionError,
                onCancel = ::clearForm,
                showLogo = false,
                modifier = Modifier.verticalScroll(rememberScrollState())
            )
        }
    }
}

@Composable
private fun PublishedRecipeCard(recipe: AppRecipe, onEdit: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = MaterialTheme.colorScheme.surface,
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            MaterialTheme.colorScheme.outline.copy(alpha = 0.35f)
        )
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(58.dp)
                    .background(MaterialTheme.colorScheme.primaryContainer, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                PlayIcon(MaterialTheme.colorScheme.primary, Modifier.size(28.dp))
            }
            Spacer(Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = recipe.title,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = recipe.videoUrl.substringAfter("://").substringBefore('/'),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Spacer(Modifier.width(8.dp))
            OutlinedButton(
                onClick = onEdit,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.primary)
            ) {
                EditRecipeIcon(MaterialTheme.colorScheme.primary, Modifier.size(21.dp))
                Spacer(Modifier.width(7.dp))
                Text("Editar", style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}

@Composable
private fun PlayIcon(color: Color, modifier: Modifier = Modifier) {
    Canvas(modifier) {
        val path = Path().apply {
            moveTo(size.width * 0.28f, size.height * 0.15f)
            lineTo(size.width * 0.82f, size.height * 0.5f)
            lineTo(size.width * 0.28f, size.height * 0.85f)
            close()
        }
        drawPath(path, color)
    }
}

@Composable
private fun EditRecipeIcon(color: Color, modifier: Modifier = Modifier) {
    Canvas(modifier) {
        val stroke = size.minDimension * 0.1f
        val path = Path().apply {
            moveTo(size.width * 0.18f, size.height * 0.8f)
            lineTo(size.width * 0.28f, size.height * 0.52f)
            lineTo(size.width * 0.68f, size.height * 0.12f)
            lineTo(size.width * 0.88f, size.height * 0.32f)
            lineTo(size.width * 0.48f, size.height * 0.72f)
            close()
        }
        drawPath(path, color, style = Stroke(stroke, cap = StrokeCap.Round))
    }
}

@Preview(showBackground = true, widthDp = 430, heightDp = 1000)
@Composable
private fun ManageRecipesScreenPreview() {
    ProyectoSemestralTheme { ManageRecipesScreen() }
}
