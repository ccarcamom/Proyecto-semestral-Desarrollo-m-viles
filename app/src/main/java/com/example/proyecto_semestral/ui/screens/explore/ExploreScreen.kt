package com.example.proyecto_semestral.ui.screens.explore

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyecto_semestral.R
import com.example.proyecto_semestral.model.ExploreContent
import com.example.proyecto_semestral.ui.components.AppBottomBar
import com.example.proyecto_semestral.ui.components.BottomBarItem
import com.example.proyecto_semestral.ui.components.BrandLogo
import com.example.proyecto_semestral.ui.components.ContentFilter
import com.example.proyecto_semestral.ui.components.ContentFilters
import com.example.proyecto_semestral.ui.components.ExploreContentRow
import com.example.proyecto_semestral.ui.theme.ProyectoSemestralTheme

@Composable
fun ExploreScreen(
    onBottomBarItemSelected: (BottomBarItem) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedFilter by rememberSaveable { mutableStateOf(ContentFilter.All) }
    var searchQuery by rememberSaveable { mutableStateOf("") }

    val visibleItems = exploreItems.filter { item ->
        val matchesFilter = when (selectedFilter) {
            ContentFilter.All -> true
            ContentFilter.Recipes -> item.type == ContentFilter.Recipes
            ContentFilter.Videos -> item.type == ContentFilter.Videos
            ContentFilter.Courses -> item.type == ContentFilter.Courses
        }
        val matchesQuery = searchQuery.isBlank() ||
            item.title.contains(searchQuery, ignoreCase = true) ||
            item.metadata.contains(searchQuery, ignoreCase = true) ||
            item.eyebrow.contains(searchQuery, ignoreCase = true)

        matchesFilter && matchesQuery
    }

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
                onValueChange = { searchQuery = it }
            )

            Spacer(modifier = Modifier.height(18.dp))
            ContentFilters(
                selectedFilter = selectedFilter,
                onFilterSelected = { selectedFilter = it }
            )

            Spacer(modifier = Modifier.height(22.dp))
            Text(
                text = "Especialidad",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(10.dp))
            SpecialtySelector()

            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "${visibleItems.size} contenidos",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp)
            )

            Spacer(modifier = Modifier.height(14.dp))
            visibleItems.forEachIndexed { index, item ->
                ExploreContentRow(
                    item = item,
                    onClick = {}
                )
                if (index != visibleItems.lastIndex) {
                    HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
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
        androidx.compose.foundation.text.BasicTextField(
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
private fun SpecialtySelector() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(62.dp)
            .clip(RoundedCornerShape(12.dp))
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.55f),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 18.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Todas las especialidades",
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.bodyLarge.copy(fontSize = 20.sp)
        )
        ChevronDownIcon(
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(22.dp)
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

@Composable
private fun ChevronDownIcon(color: Color, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val stroke = 2.4.dp.toPx()
        drawLine(color, Offset(size.width * 0.22f, size.height * 0.36f), Offset(size.width * 0.5f, size.height * 0.64f), stroke, cap = StrokeCap.Round)
        drawLine(color, Offset(size.width * 0.5f, size.height * 0.64f), Offset(size.width * 0.78f, size.height * 0.36f), stroke, cap = StrokeCap.Round)
    }
}

private val exploreItems = listOf(
    ExploreContent("RECETA", "Galletas con chips de chocolate", "Galletas · 35 min", ContentFilter.Recipes, R.drawable.galletas),
    ExploreContent("RECETA", "Empanadas de queso al horno", "Empanadas · 50 min", ContentFilter.Recipes, R.drawable.bolleria),
    ExploreContent("RECETA", "Postre de chocolate en vaso", "Chocolatería · 25 min + frío", ContentFilter.Recipes, R.drawable.chocolate),
    ExploreContent("VIDEO TÉCNICO", "Introducción al templado de chocolate", "Chocolatería · 8 min", ContentFilter.Videos, R.drawable.chocolate),
    ExploreContent("VIDEO TÉCNICO", "Técnica básica de laminado", "Masas de hoja · 10 min", ContentFilter.Videos, R.drawable.bolleria),
    ExploreContent("VIDEO TÉCNICO", "Decoración de pastelería de vitrina", "Pastelería de vitrina · 7 min", ContentFilter.Videos, R.drawable.galletas),
    ExploreContent("CURSO", "Fundamentos de chocolatería", "Chocolatería · 30 min", ContentFilter.Courses, R.drawable.chocolate),
    ExploreContent("CURSO", "Introducción a la bollería", "Bollería · 35 min", ContentFilter.Courses, R.drawable.bolleria)
)

@Preview(showBackground = true)
@Composable
private fun ExploreScreenPreview() {
    ProyectoSemestralTheme {
        ExploreScreen(onBottomBarItemSelected = {})
    }
}
