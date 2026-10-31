package com.example.proyecto_semestral.ui.components.admin

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.proyecto_semestral.ui.theme.ProyectoSemestralTheme

enum class AdminSection { USERS, RECIPES }

/** Selector principal entre la administración de usuarios y recetas. */
@Composable
fun AdminNavigationToggle(
    selectedSection: AdminSection,
    onSectionSelected: (AdminSection) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(62.dp)
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(24.dp)
            )
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.65f),
                shape = RoundedCornerShape(24.dp)
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        NavigationItem(
            text = "Usuarios",
            selected = selectedSection == AdminSection.USERS,
            onClick = { onSectionSelected(AdminSection.USERS) },
            icon = { color -> UsersIcon(color, Modifier.size(28.dp)) },
            modifier = Modifier.weight(1f)
        )
        NavigationItem(
            text = "Recetas",
            selected = selectedSection == AdminSection.RECIPES,
            onClick = { onSectionSelected(AdminSection.RECIPES) },
            icon = { color -> RecipeIcon(color, Modifier.size(28.dp)) },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun NavigationItem(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    icon: @Composable (Color) -> Unit,
    modifier: Modifier = Modifier
) {
    val background = if (selected) MaterialTheme.colorScheme.primary else Color.Transparent
    val content = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
    Row(
        modifier = modifier
            .height(62.dp)
            .background(background, RoundedCornerShape(22.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        icon(content)
        Spacer(modifier = Modifier.width(14.dp))
        Text(
            text = text,
            color = content,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
        )
    }
}

@Composable
private fun UsersIcon(color: Color, modifier: Modifier = Modifier) {
    Canvas(modifier) {
        val stroke = size.minDimension * 0.075f
        drawCircle(color, size.minDimension * 0.14f, Offset(size.width * 0.42f, size.height * 0.31f))
        drawCircle(color, size.minDimension * 0.11f, Offset(size.width * 0.69f, size.height * 0.38f), style = Stroke(stroke))
        drawArc(color, 190f, 160f, false, Offset(size.width * 0.14f, size.height * 0.44f), androidx.compose.ui.geometry.Size(size.width * 0.56f, size.height * 0.45f), style = Stroke(stroke * 2, cap = StrokeCap.Round))
        drawArc(color, 200f, 140f, false, Offset(size.width * 0.52f, size.height * 0.53f), androidx.compose.ui.geometry.Size(size.width * 0.34f, size.height * 0.3f), style = Stroke(stroke, cap = StrokeCap.Round))
    }
}

@Composable
private fun RecipeIcon(color: Color, modifier: Modifier = Modifier) {
    Canvas(modifier) {
        val stroke = size.minDimension * 0.075f
        val path = Path().apply {
            moveTo(size.width * 0.32f, size.height * 0.82f)
            lineTo(size.width * 0.3f, size.height * 0.53f)
            cubicTo(size.width * 0.08f, size.height * 0.48f, size.width * 0.14f, size.height * 0.2f, size.width * 0.36f, size.height * 0.23f)
            cubicTo(size.width * 0.42f, size.height * 0.05f, size.width * 0.61f, size.height * 0.08f, size.width * 0.64f, size.height * 0.25f)
            cubicTo(size.width * 0.88f, size.height * 0.2f, size.width * 0.94f, size.height * 0.49f, size.width * 0.7f, size.height * 0.54f)
            lineTo(size.width * 0.68f, size.height * 0.82f)
            close()
        }
        drawPath(path, color, style = Stroke(stroke, cap = StrokeCap.Round))
        drawLine(color, Offset(size.width * 0.31f, size.height * 0.66f), Offset(size.width * 0.69f, size.height * 0.66f), stroke, StrokeCap.Round)
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFEAF2EE, widthDp = 430)
@Composable
private fun AdminNavigationTogglePreview() {
    ProyectoSemestralTheme {
        AdminNavigationToggle(
            selectedSection = AdminSection.USERS,
            onSectionSelected = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}
