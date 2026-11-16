package com.example.proyecto_semestral.ui.screens.home

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.proyecto_semestral.ui.components.AppBottomBar
import com.example.proyecto_semestral.ui.components.BottomBarItem
import com.example.proyecto_semestral.ui.components.BrandLogo
import com.example.proyecto_semestral.ui.viewmodel.recipe.RecipeListUiState
import com.example.proyecto_semestral.ui.viewmodel.recipe.RecipeListViewModel
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.material3.CircularProgressIndicator

@Composable
fun HomeScreen(
    viewModelFactory: ViewModelProvider.Factory,
    onBottomBarItemSelected: (BottomBarItem) -> Unit = {},
    onSearch: (String) -> Unit = {},
    onRecipesClick: () -> Unit = {},
    onCoursesClick: () -> Unit = {},
    onRecipeClick: (Long) -> Unit = {},
    onLogoutClick: () -> Unit = {},
    isAdmin: Boolean = false,
    onAdminClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var homeSearchQuery by rememberSaveable { mutableStateOf("") }
    val recipeViewModel: RecipeListViewModel = viewModel(factory = viewModelFactory)
    val recipeState by recipeViewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        recipeViewModel.loadRecipes(page = 0)
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            AppBottomBar(
                selectedItem = BottomBarItem.Home,
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
            BrandLogo()
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (isAdmin) {
                    TextButton(onClick = onAdminClick) {
                        Text("Panel admin")
                    }
                }
                TextButton(onClick = onLogoutClick) {
                    Text("Cerrar sesión")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "UN POCO DE INSPIRACIÓN, CADA DÍA",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.5.sp
                )
            )

            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = "Inspírate y aprende.",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontSize = 38.sp,
                    lineHeight = 44.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "¿Qué te gustaría crear hoy?",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 22.sp)
            )

            Spacer(modifier = Modifier.height(24.dp))
            HomeSearchField(
                value = homeSearchQuery,
                onValueChange = { homeSearchQuery = it },
                onSearch = { onSearch(homeSearchQuery) }
            )

            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                HomeShortcut(
                    label = "Recetas",
                    icon = ShortcutIcon.Book,
                    onClick = onRecipesClick,
                    modifier = Modifier.weight(1f)
                )
                HomeShortcut(
                    label = "Cursos",
                    icon = ShortcutIcon.Course,
                    onClick = onCoursesClick,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(28.dp))
            Text(
                text = "Recetas recientes",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.height(14.dp))
            when (val state = recipeState) {
                RecipeListUiState.Loading -> Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }

                RecipeListUiState.Empty -> Text(
                    text = "Aún no hay recetas publicadas.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyLarge
                )

                is RecipeListUiState.Error -> Text(
                    text = state.message,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )

                is RecipeListUiState.Success -> state.page.recipes.take(3).forEach { recipe ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onRecipeClick(recipe.id) }
                            .padding(vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            model = recipe.imageUrl,
                            contentDescription = recipe.title,
                            modifier = Modifier
                                .size(88.dp)
                                .clip(RoundedCornerShape(14.dp)),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.width(14.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = recipe.title,
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Text(
                                text = listOfNotNull(
                                    recipe.category?.name,
                                    recipe.duration.takeIf(String::isNotBlank)
                                ).joinToString(" · "),
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }

}

@Composable
private fun HomeSearchField(
    value: String,
    onValueChange: (String) -> Unit,
    onSearch: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(58.dp)
            .background(
                color = MaterialTheme.colorScheme.background,
                shape = RoundedCornerShape(18.dp)
            )
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(18.dp)
            )
            .padding(horizontal = 18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SearchGlyph(
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .size(24.dp)
                .clickable(onClick = onSearch)
        )
        Spacer(modifier = Modifier.width(14.dp))
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 18.sp
            ),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = { onSearch() }),
            modifier = Modifier.weight(1f),
            decorationBox = { innerTextField ->
                if (value.isBlank()) {
                    Text(
                        text = "Busca una receta o una técnica",
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
private fun HomeShortcut(
    label: String,
    icon: ShortcutIcon,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .height(100.dp)
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(22.dp)
            )
            .clickable(onClick = onClick)
            .padding(vertical = 14.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        ShortcutGlyph(
            icon = icon,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = label,
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
        )
    }
}

private enum class ShortcutIcon { Book, Course }

@Composable
private fun ShortcutGlyph(icon: ShortcutIcon, color: Color, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val stroke = 2.dp.toPx()
        when (icon) {
            ShortcutIcon.Book -> {
                drawRoundRect(color, Offset(size.width * .12f, size.height * .18f), androidx.compose.ui.geometry.Size(size.width * .76f, size.height * .68f), style = Stroke(stroke))
                drawLine(color, Offset(size.width * .5f, size.height * .2f), Offset(size.width * .5f, size.height * .84f), stroke)
            }
            ShortcutIcon.Course -> {
                val path = androidx.compose.ui.graphics.Path().apply {
                    moveTo(size.width * .08f, size.height * .35f)
                    lineTo(size.width * .5f, size.height * .15f)
                    lineTo(size.width * .92f, size.height * .35f)
                    lineTo(size.width * .5f, size.height * .55f)
                    close()
                }
                drawPath(path, color, style = Stroke(stroke))
                drawLine(color, Offset(size.width * .25f, size.height * .47f), Offset(size.width * .25f, size.height * .72f), stroke)
            }
        }
    }
}

@Composable
private fun SearchGlyph(color: Color, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val stroke = 2.dp.toPx()
        drawCircle(color, size.minDimension * .32f, Offset(size.width * .42f, size.height * .42f), style = Stroke(stroke))
        drawLine(color, Offset(size.width * .64f, size.height * .64f), Offset(size.width * .9f, size.height * .9f), stroke, cap = StrokeCap.Round)
    }
}

