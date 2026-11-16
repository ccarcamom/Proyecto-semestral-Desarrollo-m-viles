package com.example.proyecto_semestral.ui.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proyecto_semestral.model.admin.AdminCourse
import com.example.proyecto_semestral.model.admin.AdminRecipe
import com.example.proyecto_semestral.ui.components.AppTopBar
import com.example.proyecto_semestral.ui.components.PrimaryButton
import com.example.proyecto_semestral.ui.viewmodel.admin.AdminUiState
import com.example.proyecto_semestral.ui.viewmodel.admin.AdminViewModel

private enum class AdminTab { Recipes, Courses }

@Composable
fun AdminScreen(
    viewModelFactory: ViewModelProvider.Factory,
    adminEmail: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel: AdminViewModel = viewModel(factory = viewModelFactory)
    val state by viewModel.uiState.collectAsState()

    var tab by rememberSaveable { mutableStateOf(AdminTab.Recipes) }
    var showRecipeForm by rememberSaveable { mutableStateOf(false) }
    var showCourseForm by rememberSaveable { mutableStateOf(false) }
    var courseToEdit by remember { mutableStateOf<AdminCourse?>(null) }
    var lessonCourseId by rememberSaveable { mutableStateOf<Long?>(null) }
    var lessonOrder by rememberSaveable { mutableStateOf(1) }
    var recipeToDelete by rememberSaveable { mutableStateOf<Long?>(null) }
    var courseToDelete by rememberSaveable { mutableStateOf<Long?>(null) }

    LaunchedEffect(Unit) {
        viewModel.load()
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            AppTopBar(
                title = "Panel de administración",
                onBackClick = onBack
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when {
                showRecipeForm || state.recipeForEdit != null -> AdminRecipeForm(
                    categories = state.recipeCategories,
                    isSaving = state.isSaving,
                    createdBy = adminEmail,
                    initialRecipe = state.recipeForEdit,
                    onCancel = {
                        showRecipeForm = false
                        viewModel.closeRecipeEditor()
                    },
                    onSubmit = { draft ->
                        state.recipeForEdit?.let {
                            viewModel.updateRecipe(it.id, draft)
                        } ?: viewModel.createRecipe(draft)
                        showRecipeForm = false
                        viewModel.closeRecipeEditor()
                    }
                )

                showCourseForm || courseToEdit != null -> AdminCourseForm(
                    categories = state.courseCategories,
                    isSaving = state.isSaving,
                    initialCourse = courseToEdit,
                    onCancel = {
                        showCourseForm = false
                        courseToEdit = null
                    },
                    onSubmit = { draft ->
                        courseToEdit?.let {
                            viewModel.updateCourse(it.id, draft)
                        } ?: viewModel.createCourse(draft)
                        showCourseForm = false
                        courseToEdit = null
                    }
                )

                else -> AdminHome(
                    state = state,
                    tab = tab,
                    onTabSelected = {
                        tab = it
                        viewModel.clearFeedback()
                    },
                    onNewRecipe = {
                        viewModel.clearFeedback()
                        showRecipeForm = true
                    },
                    onNewCourse = {
                        viewModel.clearFeedback()
                        showCourseForm = true
                    },
                    onDeleteRecipe = { recipeToDelete = it.id },
                    onDeleteCourse = { courseToDelete = it.id },
                    onEditRecipe = {
                        viewModel.clearFeedback()
                        viewModel.loadRecipeForEdit(it.id)
                    },
                    onEditCourse = {
                        viewModel.clearFeedback()
                        courseToEdit = it
                    },
                    onTogglePublication = { course ->
                        viewModel.changeCoursePublication(course.id, !course.published)
                    },
                    onAddLesson = { course ->
                        lessonCourseId = course.id
                        lessonOrder = course.lessonCount + 1
                    }
                )
            }
        }
    }

    if (recipeToDelete != null) {
        AlertDialog(
            onDismissRequest = { recipeToDelete = null },
            title = { Text("Eliminar receta") },
            text = { Text("La receta se eliminará del microservicio. Esta acción no se puede deshacer.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        recipeToDelete?.let(viewModel::deleteRecipe)
                        recipeToDelete = null
                    }
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(onClick = { recipeToDelete = null }) {
                    Text("Cancelar")
                }
            }
        )
    }

    if (courseToDelete != null) {
        AlertDialog(
            onDismissRequest = { courseToDelete = null },
            title = { Text("Eliminar curso") },
            text = { Text("El curso y sus lecciones se eliminarán del microservicio. Esta acción no se puede deshacer.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        courseToDelete?.let(viewModel::deleteCourse)
                        courseToDelete = null
                    }
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(onClick = { courseToDelete = null }) {
                    Text("Cancelar")
                }
            }
        )
    }

    if (lessonCourseId != null) {
        AdminLessonDialog(
            initialOrder = lessonOrder,
            isSaving = state.isSaving,
            onDismiss = { lessonCourseId = null },
            onSubmit = { draft ->
                lessonCourseId?.let { viewModel.addLesson(it, draft) }
                lessonCourseId = null
            }
        )
    }
}

@Composable
private fun AdminHome(
    state: AdminUiState,
    tab: AdminTab,
    onTabSelected: (AdminTab) -> Unit,
    onNewRecipe: () -> Unit,
    onNewCourse: () -> Unit,
    onDeleteRecipe: (AdminRecipe) -> Unit,
    onDeleteCourse: (AdminCourse) -> Unit,
    onEditRecipe: (AdminRecipe) -> Unit,
    onEditCourse: (AdminCourse) -> Unit,
    onTogglePublication: (AdminCourse) -> Unit,
    onAddLesson: (AdminCourse) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 22.dp)
    ) {
        Text(
            text = "Crea y administra el contenido publicado en la app.",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(Modifier.height(14.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AdminTabChip(
                label = "Recetas",
                selected = tab == AdminTab.Recipes,
                onClick = { onTabSelected(AdminTab.Recipes) }
            )
            AdminTabChip(
                label = "Cursos",
                selected = tab == AdminTab.Courses,
                onClick = { onTabSelected(AdminTab.Courses) }
            )
        }

        Spacer(Modifier.height(14.dp))
        AdminFeedback(message = state.message, error = state.error)
        Spacer(Modifier.height(10.dp))

        if (state.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 28.dp)
            ) {
                item {
                    PrimaryButton(
                        text = if (tab == AdminTab.Recipes) "Nueva receta" else "Nuevo curso",
                        onClick = if (tab == AdminTab.Recipes) onNewRecipe else onNewCourse,
                    enabled = !state.isSaving && !state.isLoadingEditor
                    )
                }

                if (tab == AdminTab.Recipes) {
                    if (state.recipes.isEmpty()) {
                        item {
                            Text(
                                text = "Todavía no hay recetas en el microservicio.",
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                    items(items = state.recipes, key = { it.id }) { recipe ->
                        AdminRecipeRow(
                            recipe = recipe,
                            onEdit = { onEditRecipe(recipe) },
                            onDelete = { onDeleteRecipe(recipe) }
                        )
                    }
                } else {
                    if (state.courses.isEmpty()) {
                        item {
                            Text(
                                text = "Todavía no hay cursos en el microservicio.",
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                    items(items = state.courses, key = { it.id }) { course ->
                        AdminCourseRow(
                            course = course,
                            onEdit = { onEditCourse(course) },
                            onTogglePublication = { onTogglePublication(course) },
                            onAddLesson = { onAddLesson(course) },
                            onDelete = { onDeleteCourse(course) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AdminTabChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Text(
        text = label,
        color = if (selected) {
            MaterialTheme.colorScheme.onPrimary
        } else {
            MaterialTheme.colorScheme.onSurfaceVariant
        },
        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
        modifier = Modifier
            .background(
                color = if (selected) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.surfaceVariant
                },
                shape = RoundedCornerShape(percent = 50)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 22.dp, vertical = 10.dp)
    )
}

@Composable
private fun AdminRecipeRow(
    recipe: AdminRecipe,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(18.dp)
            )
            .padding(16.dp)
    ) {
        Text(
            text = recipe.title,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
        )
        Text(
            text = listOfNotNull(
                recipe.categoryName.takeIf(String::isNotBlank),
                recipe.duration.takeIf(String::isNotBlank)
            ).joinToString(" · ").ifBlank { "Sin categoría" },
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodySmall
        )
        Spacer(Modifier.height(6.dp))
        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            AdminRowAction(text = "Editar", onClick = onEdit)
            AdminRowAction(text = "Eliminar", onClick = onDelete)
        }
    }
}

@Composable
private fun AdminCourseRow(
    course: AdminCourse,
    onEdit: () -> Unit,
    onTogglePublication: () -> Unit,
    onAddLesson: () -> Unit,
    onDelete: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(18.dp)
            )
            .padding(16.dp)
    ) {
        Text(
            text = course.title,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
        )
        Text(
            text = listOfNotNull(
                course.categoryName.takeIf(String::isNotBlank),
                "${course.lessonCount} lecciones",
                "${course.durationSeconds / 60} min"
            ).joinToString(" · "),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodySmall
        )
        Text(
            text = if (course.published) "Publicado" else "Borrador (no visible en la app)",
            color = if (course.published) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.error
            },
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
        )
        Spacer(Modifier.height(6.dp))
        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            AdminRowAction(text = "Editar", onClick = onEdit)
            AdminRowAction(
                text = if (course.published) "Ocultar" else "Publicar",
                onClick = onTogglePublication
            )
            AdminRowAction(text = "Agregar lección", onClick = onAddLesson)
            AdminRowAction(text = "Eliminar", onClick = onDelete)
        }
    }
}
