package com.example.proyecto_semestral.ui.screens.explore

import androidx.compose.foundation.text.BasicTextField

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import com.example.proyecto_semestral.R
import com.example.proyecto_semestral.model.content.ExploreContent
import com.example.proyecto_semestral.ui.components.AppBottomBar
import com.example.proyecto_semestral.ui.components.BottomBarItem
import com.example.proyecto_semestral.ui.components.BrandLogo
import com.example.proyecto_semestral.ui.components.ContentFilter
import com.example.proyecto_semestral.ui.components.ContentFilters
import com.example.proyecto_semestral.ui.components.ExploreContentRow
import com.example.proyecto_semestral.ui.viewmodel.recipe.RecipeListUiState
import com.example.proyecto_semestral.ui.viewmodel.recipe.RecipeListViewModel
import kotlinx.coroutines.delay

@Composable
fun ExploreScreen(
    viewModelFactory: ViewModelProvider.Factory,
    onBottomBarItemSelected: (BottomBarItem) -> Unit,
    selectedFilter: ContentFilter,
    searchQuery: String,
    onFilterSelected: (ContentFilter) -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onRecipeClick: (Long) -> Unit,
    onCourseClick: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val viewModel: RecipeListViewModel = viewModel(factory = viewModelFactory)
    val recipeState by viewModel.uiState.collectAsState()

    LaunchedEffect(searchQuery) {
        delay(300)
        viewModel.loadRecipes(query = searchQuery)
    }

    val courseItems = exploreItems.filter { item ->
        searchQuery.isBlank() ||
            item.title.contains(searchQuery, ignoreCase = true) ||
            item.metadata.contains(searchQuery, ignoreCase = true) ||
            item.eyebrow.contains(searchQuery, ignoreCase = true)
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

            if (visibleItems.isEmpty()) {
                val isWaitingForRecipes =
                    recipesAreVisible && recipeState is RecipeListUiState.Loading
                val alreadyShowsError =
                    recipesAreVisible && recipeState is RecipeListUiState.Error
                if (!isWaitingForRecipes && !alreadyShowsError) {
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
                                ContentFilter.Courses -> onCourseClick(item.id)
                                ContentFilter.All -> Unit
                            }
                        }
                    )
                    if (index != visibleItems.lastIndex) {
                        HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun LoadingRecipesState() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
        Spacer(modifier = Modifier.height(12.dp))
        Text("Cargando recetas…", color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun RecipeErrorState(message: String, onRetry: () -> Unit) {
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

private val exploreItems = listOf(
    ExploreContent("CURSO", "Fundamentos de chocolatería", "Chocolatería · 30 min", ContentFilter.Courses, R.drawable.chocolate, id = "curso-chocolateria"),
    ExploreContent("CURSO", "Introducción a la bollería", "Bollería · 35 min", ContentFilter.Courses, R.drawable.bolleria, id = "curso-bolleria")
)
