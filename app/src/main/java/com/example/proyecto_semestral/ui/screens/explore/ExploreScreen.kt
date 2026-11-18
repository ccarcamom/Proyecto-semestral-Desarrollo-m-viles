package com.example.proyecto_semestral.ui.screens.explore

import androidx.compose.foundation.text.BasicTextField

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proyecto_semestral.model.content.ExploreContent
import com.example.proyecto_semestral.ui.components.AppBottomBar
import com.example.proyecto_semestral.ui.components.BottomBarItem
import com.example.proyecto_semestral.ui.components.BrandLogo
import com.example.proyecto_semestral.ui.components.ContentFilter
import com.example.proyecto_semestral.ui.components.ContentFilters
import com.example.proyecto_semestral.ui.components.ExploreContentRow
import com.example.proyecto_semestral.ui.viewmodel.recipe.RecipeListUiState
import com.example.proyecto_semestral.ui.viewmodel.recipe.RecipeListViewModel
import com.example.proyecto_semestral.ui.viewmodel.course.CourseListUiState
import com.example.proyecto_semestral.ui.viewmodel.course.CourseListViewModel
import kotlinx.coroutines.delay

@Composable
fun ExploreScreen(
    viewModelFactory: ViewModelProvider.Factory,
    courseViewModelFactory: ViewModelProvider.Factory,
    onBottomBarItemSelected: (BottomBarItem) -> Unit,
    selectedFilter: ContentFilter,
    searchQuery: String,
    onFilterSelected: (ContentFilter) -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onRecipeClick: (Long) -> Unit,
    onCourseClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel: RecipeListViewModel = viewModel(factory = viewModelFactory)
    val recipeState by viewModel.uiState.collectAsState()
    val recipeCategories by viewModel.categories.collectAsState()
    val courseViewModel: CourseListViewModel = viewModel(factory = courseViewModelFactory)
    val courseState by courseViewModel.uiState.collectAsState()
    val courseCategories by courseViewModel.categories.collectAsState()

    var selectedRecipeCategoryId by rememberSaveable { mutableStateOf<Long?>(null) }
    var selectedCourseCategoryId by rememberSaveable { mutableStateOf<Long?>(null) }

    LaunchedEffect(Unit) {
        viewModel.loadCategories()
        courseViewModel.loadCategories()
    }

    LaunchedEffect(searchQuery, selectedRecipeCategoryId) {
        delay(300)
        viewModel.loadRecipes(
            query = searchQuery,
            categoryId = selectedRecipeCategoryId
        )
    }

    LaunchedEffect(searchQuery, selectedCourseCategoryId) {
        delay(300)
        courseViewModel.loadCourses(
            query = searchQuery,
            categoryId = selectedCourseCategoryId
        )
    }

    val courseItems = (courseState as? CourseListUiState.Success)
        ?.page
        ?.courses
        .orEmpty()
        .map { course ->
            ExploreContent(
                eyebrow = "CURSO",
                title = course.title,
                metadata = buildList {
                    if (course.categoryName.isNotBlank()) add(course.categoryName)
                    add("${course.lessonCount} lecciones")
                    if (course.durationSeconds > 0) add("${course.durationSeconds / 60} min")
                }.joinToString(" · "),
                type = ContentFilter.Courses,
                imageUrl = course.imageUrl,
                id = course.id.toString()
            )
        }
    val recipeItems = (recipeState as? RecipeListUiState.Success)
        ?.page
        ?.recipes
        .orEmpty()
        .map { recipe ->
            ExploreContent(
                eyebrow = "RECETA",
                title = recipe.title,
                metadata = listOfNotNull(
                    recipe.category?.name,
                    recipe.duration.takeIf(String::isNotBlank)
                ).joinToString(" · "),
                type = ContentFilter.Recipes,
                imageUrl = recipe.imageUrl,
                id = recipe.id.toString()
            )
        }
    val visibleItems = when (selectedFilter) {
        ContentFilter.All -> recipeItems + courseItems
        ContentFilter.Recipes -> recipeItems
        ContentFilter.Courses -> courseItems
    }
    val recipesAreVisible = selectedFilter != ContentFilter.Courses
    val coursesAreVisible = selectedFilter != ContentFilter.Recipes

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            AppBottomBar(
                selectedItem = BottomBarItem.Explore,
                onItemSelected = onBottomBarItemSelected
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 22.dp)
        ) {
            Spacer(modifier = Modifier.height(14.dp))
            BrandLogo(tagline = "TU BIBLIOTECA")

            Spacer(modifier = Modifier.height(34.dp))
            Text(
                text = "Explora e inspírate.",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontSize = 32.sp,
                    lineHeight = 38.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Conocimiento para llevar a la práctica.",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 19.sp)
            )

            Spacer(modifier = Modifier.height(24.dp))
            ExploreSearchField(
                value = searchQuery,
                onValueChange = onSearchQueryChange
            )

            Spacer(modifier = Modifier.height(18.dp))
            ContentFilters(
                selectedFilter = selectedFilter,
                onFilterSelected = onFilterSelected
            )

            if (recipesAreVisible && recipeCategories.isNotEmpty()) {
                Spacer(modifier = Modifier.height(18.dp))
                CategoryFilterChips(
                    title = "Categoría de receta",
                    categories = recipeCategories.map { it.id to it.name },
                    selectedId = selectedRecipeCategoryId,
                    onSelect = { selectedRecipeCategoryId = it }
                )
            }

            if (coursesAreVisible && courseCategories.isNotEmpty()) {
                Spacer(modifier = Modifier.height(18.dp))
                CategoryFilterChips(
                    title = "Categoría de curso",
                    categories = courseCategories.map { it.id to it.name },
                    selectedId = selectedCourseCategoryId,
                    onSelect = { selectedCourseCategoryId = it }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
            if (recipesAreVisible) {
                when (val state = recipeState) {
                    RecipeListUiState.Loading -> LoadingRecipesState()
                    is RecipeListUiState.Error -> RecipeErrorState(
                        message = state.message,
                        onRetry = viewModel::retry
                    )
                    RecipeListUiState.Empty,
                    is RecipeListUiState.Success -> Unit
                }
            }
            if (coursesAreVisible) {
                when (val state = courseState) {
                    CourseListUiState.Loading -> LoadingContentState("Cargando cursos…")
                    is CourseListUiState.Error -> ContentErrorState(
                        message = state.message,
                        onRetry = courseViewModel::retry
                    )
                    CourseListUiState.Empty,
                    is CourseListUiState.Success -> Unit
                }
            }

            if (visibleItems.isEmpty()) {
                val isWaitingForRecipes =
                    recipesAreVisible && recipeState is RecipeListUiState.Loading
                val isWaitingForCourses =
                    coursesAreVisible && courseState is CourseListUiState.Loading
                val alreadyShowsError =
                    (recipesAreVisible && recipeState is RecipeListUiState.Error) ||
                        (coursesAreVisible && courseState is CourseListUiState.Error)
                if (!isWaitingForRecipes && !isWaitingForCourses && !alreadyShowsError) {
                    EmptyExploreState()
                }
            } else {
                Text(
                    text = "${visibleItems.size} contenidos",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp)
                )

                Spacer(modifier = Modifier.height(14.dp))
                visibleItems.forEachIndexed { index, item ->
                    ExploreContentRow(
                        item = item,
                        onClick = {
                            when (item.type) {
                                ContentFilter.Recipes -> item.id.toLongOrNull()?.let(onRecipeClick)
                                ContentFilter.Courses -> item.id.toLongOrNull()?.let(onCourseClick)
                                ContentFilter.All -> item.id.toLongOrNull()?.let { id ->
                                    if (item.eyebrow.equals("CURSO", ignoreCase = true)) {
                                        onCourseClick(id)
                                    } else {
                                        onRecipeClick(id)
                                    }
                                }
                            }
                        }
                    )
                    if (index != visibleItems.lastIndex) {
                        HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)
                    }
                }
            }

            if (recipesAreVisible) {
                val recipeSuccess = recipeState as? RecipeListUiState.Success
                if (recipeSuccess != null &&
                    (recipeSuccess.hasMore || recipeSuccess.isLoadingMore)
                ) {
                    Spacer(modifier = Modifier.height(18.dp))
                    Button(
                        onClick = viewModel::loadMore,
                        enabled = !recipeSuccess.isLoadingMore,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            if (recipeSuccess.isLoadingMore) {
                                "Cargando recetas…"
                            } else {
                                "Cargar más recetas"
                            }
                        )
                    }
                }
            }

            if (coursesAreVisible) {
                val courseSuccess = courseState as? CourseListUiState.Success
                if (courseSuccess != null &&
                    (courseSuccess.hasMore || courseSuccess.isLoadingMore)
                ) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = courseViewModel::loadMore,
                        enabled = !courseSuccess.isLoadingMore,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            if (courseSuccess.isLoadingMore) {
                                "Cargando cursos…"
                            } else {
                                "Cargar más cursos"
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun CategoryFilterChips(
    title: String,
    categories: List<Pair<Long, String>>,
    selectedId: Long?,
    onSelect: (Long?) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = title,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.labelLarge
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CategoryChip(
                text = "Todas",
                selected = selectedId == null,
                onClick = { onSelect(null) }
            )
            categories.forEach { (id, name) ->
                CategoryChip(
                    text = name,
                    selected = selectedId == id,
                    onClick = { onSelect(id) }
                )
            }
        }
    }
}

@Composable
private fun CategoryChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Text(
        text = text,
        color = if (selected) {
            MaterialTheme.colorScheme.onPrimary
        } else {
            MaterialTheme.colorScheme.onSurfaceVariant
        },
        style = MaterialTheme.typography.labelLarge,
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
            .padding(horizontal = 14.dp, vertical = 8.dp)
    )
}

@Composable
private fun LoadingRecipesState() = LoadingContentState("Cargando recetas…")

@Composable
private fun LoadingContentState(label: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
        Spacer(modifier = Modifier.height(12.dp))
        Text(label, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun RecipeErrorState(message: String, onRetry: () -> Unit) =
    ContentErrorState(message, onRetry)

@Composable
private fun ContentErrorState(message: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 36.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = message,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onRetry) {
            Text("Reintentar")
        }
    }
}

@Composable
private fun EmptyExploreState() {
    Text(
        text = "No encontramos contenidos para tu búsqueda.",
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 48.dp),
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        textAlign = androidx.compose.ui.text.style.TextAlign.Center
    )
}

@Composable
private fun ExploreSearchField(
    value: String,
    onValueChange: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(58.dp)
            .clip(RoundedCornerShape(16.dp))
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.55f),
                shape = RoundedCornerShape(16.dp)
            )
            .clickable { }
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SearchIcon(
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 18.sp
            ),
            modifier = Modifier.weight(1f),
            decorationBox = { innerTextField ->
                if (value.isBlank()) {
                    Text(
                        text = "Buscar recetas, videos o cursos",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp)
                    )
                }
                innerTextField()
            }
        )
    }
}

@Composable
private fun SearchIcon(color: Color, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val stroke = 2.dp.toPx()
        drawCircle(color, size.minDimension * 0.32f, Offset(size.width * 0.42f, size.height * 0.42f), style = Stroke(stroke))
        drawLine(color, Offset(size.width * 0.64f, size.height * 0.64f), Offset(size.width * 0.9f, size.height * 0.9f), stroke, cap = StrokeCap.Round)
    }
}
