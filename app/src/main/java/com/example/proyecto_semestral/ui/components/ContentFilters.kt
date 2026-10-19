package com.example.proyecto_semestral.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyecto_semestral.ui.theme.ProyectoSemestralTheme

enum class ContentFilter {
    All,
    Recipes,
    Videos,
    Courses
}

@Composable
fun ContentFilters(
    selectedFilter: ContentFilter,
    onFilterSelected: (ContentFilter) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        FilterChip(
            text = "Todos",
            selected = selectedFilter == ContentFilter.All,
            onClick = { onFilterSelected(ContentFilter.All) }
        )
        FilterChip(
            text = "Recetas",
            selected = selectedFilter == ContentFilter.Recipes,
            onClick = { onFilterSelected(ContentFilter.Recipes) }
        )
        FilterChip(
            text = "Videos",
            selected = selectedFilter == ContentFilter.Videos,
            onClick = { onFilterSelected(ContentFilter.Videos) }
        )
        FilterChip(
            text = "Cursos",
            selected = selectedFilter == ContentFilter.Courses,
            onClick = { onFilterSelected(ContentFilter.Courses) }
        )
    }
}

@Composable
private fun FilterChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (selected) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.background
    }
    val contentColor = if (selected) {
        MaterialTheme.colorScheme.onPrimary
    } else {
        MaterialTheme.colorScheme.onBackground
    }
    val borderColor = if (selected) {
        Color.Transparent
    } else {
        MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
    }

    Text(
        text = text,
        color = contentColor,
        style = MaterialTheme.typography.titleLarge.copy(
            fontSize = 24.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
        ),
        modifier = modifier
            .clip(RoundedCornerShape(percent = 50))
            .background(backgroundColor)
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(percent = 50)
            )
            .clickable(
                role = Role.Tab,
                onClick = onClick
            )
            .padding(horizontal = 32.dp, vertical = 16.dp)
    )
}

@Preview(showBackground = true)
@Composable
private fun ContentFiltersPreview() {
    ProyectoSemestralTheme {
        var selectedFilter by rememberSaveable { mutableStateOf(ContentFilter.Recipes) }

        ContentFilters(
            selectedFilter = selectedFilter,
            onFilterSelected = { selectedFilter = it },
            modifier = Modifier.padding(24.dp)
        )
    }
}
