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
import androidx.compose.foundation.verticalScroll
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
import com.example.proyecto_semestral.model.admin.AdminCategory
import com.example.proyecto_semestral.model.admin.AdminContactQuery
import com.example.proyecto_semestral.model.admin.AdminCourse
import com.example.proyecto_semestral.model.admin.AdminLesson
import com.example.proyecto_semestral.model.admin.AdminRecipe
import com.example.proyecto_semestral.ui.components.AppTopBar
import com.example.proyecto_semestral.ui.components.PrimaryButton
import com.example.proyecto_semestral.ui.viewmodel.admin.AdminUiState
import com.example.proyecto_semestral.ui.viewmodel.admin.AdminViewModel

private enum class AdminTab { Recipes, Courses, Queries }

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
    var lessonsCourseId by rememberSaveable { mutableStateOf<Long?>(null) }
    var lessonDialogCourseId by rememberSaveable { mutableStateOf<Long?>(null) }
    var lessonToEdit by remember { mutableStateOf<AdminLesson?>(null) }
    var lessonOrder by rememberSaveable { mutableStateOf(1) }
    var showCategoriesDialog by rememberSaveable { mutableStateOf(false) }
    var recipeToDelete by rememberSaveable { mutableStateOf<Long?>(null) }
    var courseToDelete by rememberSaveable { mutableStateOf<Long?>(null) }
    var lessonToDelete by remember { mutableStateOf<Pair<Long, AdminLesson>?>(null) }
    var queryToDelete by rememberSaveable { mutableStateOf<Long?>(null) }

    LaunchedEffect(Unit) {
        viewModel.load()
    }

    val lessonsCourse = state.courses.firstOrNull { it.id == lessonsCourseId }

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
                    onManageLessons = { lessonsCourseId = it.id },
                    onAddLesson = { course ->
                        lessonDialogCourseId = course.id
                        lessonToEdit = null
                        lessonOrder = course.lessonCount + 1
                    },
                    onManageCategories = { showCategoriesDialog = true },
                    onToggleQueryStatus = { query ->
                        val nextStatus = when (query.status) {
                            "PENDIENTE" -> "EN_PROCESO"
                            "EN_PROCESO" -> "ATENDIDA"
                            else -> "PENDIENTE"
                        }
                        viewModel.updateContactQueryStatus(query.id, nextStatus)
                    },
                    onDeleteQuery = { queryToDelete = it.id }
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

    if (lessonToDelete != null) {
        val (courseId, lesson) = lessonToDelete!!
        AlertDialog(
            onDismissRequest = { lessonToDelete = null },
            title = { Text("Eliminar lección") },
            text = { Text("Se eliminará \"${lesson.title}\" del curso.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteLesson(courseId, lesson.id)
                        lessonToDelete = null
                    }
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(onClick = { lessonToDelete = null }) {
                    Text("Cancelar")
                }
            }
        )
    }

    if (queryToDelete != null) {
        AlertDialog(
            onDismissRequest = { queryToDelete = null },
            title = { Text("Eliminar consulta") },
            text = { Text("La consulta de contacto se eliminará definitivamente.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        queryToDelete?.let(viewModel::deleteContactQuery)
                        queryToDelete = null
                    }
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(onClick = { queryToDelete = null }) {
                    Text("Cancelar")
                }
            }
        )
    }

    if (lessonDialogCourseId != null) {
        AdminLessonDialog(
            initialOrder = lessonOrder,
            isSaving = state.isSaving,
            initialLesson = lessonToEdit,
            onDismiss = {
                lessonDialogCourseId = null
                lessonToEdit = null
            },
            onSubmit = { draft ->
                val courseId = lessonDialogCourseId
                if (courseId != null) {
                    val editing = lessonToEdit
                    if (editing == null) {
                        viewModel.addLesson(courseId, draft)
                    } else {
                        viewModel.updateLesson(courseId, editing.id, draft)
                    }
                }
                lessonDialogCourseId = null
                lessonToEdit = null
            }
        )
    }

    if (lessonsCourse != null) {
        AdminLessonsDialog(
            course = lessonsCourse,
            onDismiss = { lessonsCourseId = null },
            onAdd = {
                lessonDialogCourseId = lessonsCourse.id
                lessonToEdit = null
                lessonOrder = lessonsCourse.lessonCount + 1
            },
            onEdit = { lesson ->
                lessonDialogCourseId = lessonsCourse.id
                lessonToEdit = lesson
            },
            onDelete = { lesson ->
                lessonToDelete = lessonsCourse.id to lesson
            }
        )
    }

    if (showCategoriesDialog) {
        AdminCategoriesDialog(
            categories = state.courseCategories,
            isSaving = state.isSaving,
            onDismiss = { showCategoriesDialog = false },
            onCreate = viewModel::createCourseCategory,
            onRename = viewModel::updateCourseCategory,
            onDelete = viewModel::deleteCourseCategory
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
    onManageLessons: (AdminCourse) -> Unit,
    onAddLesson: (AdminCourse) -> Unit,
    onManageCategories: () -> Unit,
    onToggleQueryStatus: (AdminContactQuery) -> Unit,
    onDeleteQuery: (AdminContactQuery) -> Unit
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
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
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
            AdminTabChip(
                label = "Consultas",
                selected = tab == AdminTab.Queries,
                onClick = { onTabSelected(AdminTab.Queries) }
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
                if (tab != AdminTab.Queries) {
                    item {
                        PrimaryButton(
                            text = if (tab == AdminTab.Recipes) "Nueva receta" else "Nuevo curso",
                            onClick = if (tab == AdminTab.Recipes) onNewRecipe else onNewCourse,
                            enabled = !state.isSaving && !state.isLoadingEditor
                        )
                    }
                }

                when (tab) {
                    AdminTab.Recipes -> {
                        if (state.recipes.isEmpty()) {
                            item {
                                Text(
                                    text = "Todavía no hay recetas en el microservicio.",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                        items(items = state.recipes, key = { "recipe-${it.id}" }) { recipe ->
                            AdminRecipeRow(
                                recipe = recipe,
                                onEdit = { onEditRecipe(recipe) },
                                onDelete = { onDeleteRecipe(recipe) }
                            )
                        }
                    }

                    AdminTab.Courses -> {
                        item {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Categorías de cursos",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                AdminRowAction(
                                    text = "Gestionar",
                                    onClick = onManageCategories
                                )
                            }
                        }
                        if (state.courses.isEmpty()) {
                            item {
                                Text(
                                    text = "Todavía no hay cursos en el microservicio.",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                        items(items = state.courses, key = { "course-${it.id}" }) { course ->
                            AdminCourseRow(
                                course = course,
                                onEdit = { onEditCourse(course) },
                                onTogglePublication = { onTogglePublication(course) },
                                onManageLessons = { onManageLessons(course) },
                                onAddLesson = { onAddLesson(course) },
                                onDelete = { onDeleteCourse(course) }
                            )
                        }
                    }

                    AdminTab.Queries -> {
                        if (state.contactQueries.isEmpty()) {
                            item {
                                Text(
                                    text = "No hay consultas de contacto registradas.",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                        items(items = state.contactQueries, key = { "query-${it.id}" }) { query ->
                            AdminContactQueryRow(
                                query = query,
                                onToggleStatus = { onToggleQueryStatus(query) },
                                onDelete = { onDeleteQuery(query) }
                            )
                        }
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
        Text(
            text = if (recipe.published) "Publicada" else "Borrador (no visible en la app)",
            color = if (recipe.published) {
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
            AdminRowAction(text = "Eliminar", onClick = onDelete)
        }
    }
}

@Composable
private fun AdminCourseRow(
    course: AdminCourse,
    onEdit: () -> Unit,
    onTogglePublication: () -> Unit,
    onManageLessons: () -> Unit,
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
            AdminRowAction(text = "Lecciones", onClick = onManageLessons)
            AdminRowAction(text = "Agregar lección", onClick = onAddLesson)
            AdminRowAction(text = "Eliminar", onClick = onDelete)
        }
    }
}

@Composable
private fun AdminContactQueryRow(
    query: AdminContactQuery,
    onToggleStatus: () -> Unit,
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
            text = query.email,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
        )
        Text(
            text = listOfNotNull(
                query.businessName.takeIf(String::isNotBlank),
                query.businessType.takeIf(String::isNotBlank),
                query.businessSize.takeIf(String::isNotBlank)
            ).joinToString(" · "),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodySmall
        )
        if (query.phone.isNotBlank()) {
            Text(
                text = "Teléfono: ${query.phone}",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodySmall
            )
        }
        Spacer(Modifier.height(6.dp))
        Text(
            text = query.description,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = statusLabel(query.status),
            color = if (query.status == "ATENDIDA") {
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
            AdminRowAction(
                text = when (query.status) {
                    "PENDIENTE" -> "Marcar en proceso"
                    "EN_PROCESO" -> "Marcar atendida"
                    else -> "Reabrir"
                },
                onClick = onToggleStatus
            )
            AdminRowAction(text = "Eliminar", onClick = onDelete)
        }
    }
}

private fun statusLabel(status: String): String = when (status) {
    "ATENDIDA" -> "Atendida"
    "EN_PROCESO" -> "En proceso"
    else -> "Pendiente"
}

@Composable
private fun AdminLessonsDialog(
    course: AdminCourse,
    onDismiss: () -> Unit,
    onAdd: () -> Unit,
    onEdit: (AdminLesson) -> Unit,
    onDelete: (AdminLesson) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Lecciones de ${course.title}") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                if (course.lessons.isEmpty()) {
                    Text(
                        text = "Este curso todavía no tiene lecciones.",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                course.lessons.forEach { lesson ->
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "${lesson.order}. ${lesson.title}",
                            color = MaterialTheme.colorScheme.onBackground,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Text(
                            text = "${lesson.durationSeconds} s",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.bodySmall
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                            AdminRowAction(text = "Editar", onClick = { onEdit(lesson) })
                            AdminRowAction(text = "Eliminar", onClick = { onDelete(lesson) })
                        }
                        Spacer(Modifier.height(10.dp))
                    }
                }
                AdminRowAction(text = "+ Agregar lección", onClick = onAdd)
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Cerrar")
            }
        }
    )
}

@Composable
private fun AdminCategoriesDialog(
    categories: List<AdminCategory>,
    isSaving: Boolean,
    onDismiss: () -> Unit,
    onCreate: (String) -> Unit,
    onRename: (Long, String) -> Unit,
    onDelete: (Long) -> Unit
) {
    var newName by rememberSaveable { mutableStateOf("") }
    var draftNames by remember(categories) {
        mutableStateOf(categories.associate { it.id to it.name })
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Categorías de cursos") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                AdminTextField(
                    value = newName,
                    onValueChange = { newName = it },
                    label = "Nueva categoría"
                )
                AdminRowAction(
                    text = "Agregar categoría",
                    onClick = {
                        if (newName.isNotBlank()) {
                            onCreate(newName)
                            newName = ""
                        }
                    }
                )
                Spacer(Modifier.height(10.dp))
                categories.forEach { category ->
                    val currentName = draftNames[category.id] ?: category.name
                    Column(modifier = Modifier.fillMaxWidth()) {
                        AdminTextField(
                            value = currentName,
                            onValueChange = { value ->
                                draftNames = draftNames + (category.id to value)
                            },
                            label = "Categoría ${category.id}"
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                            AdminRowAction(
                                text = "Guardar nombre",
                                onClick = {
                                    if (currentName.isNotBlank() &&
                                        currentName != category.name
                                    ) {
                                        onRename(category.id, currentName)
                                    }
                                }
                            )
                            AdminRowAction(
                                text = "Eliminar",
                                onClick = { onDelete(category.id) }
                            )
                        }
                        Spacer(Modifier.height(10.dp))
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss, enabled = !isSaving) {
                Text(if (isSaving) "Guardando…" else "Cerrar")
            }
        }
    )
}
