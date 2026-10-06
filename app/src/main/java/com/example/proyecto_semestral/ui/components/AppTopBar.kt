package com.example.proyecto_semestral.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.proyecto_semestral.ui.theme.ProyectoSemestralTheme

@Composable
fun AppTopBar(
    title: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .height(104.dp)
            .padding(horizontal = 22.dp)
    ) {
        BackButton(
            onClick = onBackClick,
            modifier = Modifier.align(Alignment.CenterStart)
        )

        Text(
            text = title,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Normal),
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
private fun BackButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val arrowColor = MaterialTheme.colorScheme.primary

    Box(
        modifier = modifier
            .size(54.dp)
            .clip(CircleShape)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = CircleShape
            )
            .clickable(
                role = Role.Button,
                onClickLabel = "Volver atras",
                onClick = onClick
            )
            .semantics { contentDescription = "Volver atras" },
        contentAlignment = Alignment.Center
    ) {
        BackArrow(
            color = arrowColor,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
private fun BackArrow(
    color: Color,
    modifier: Modifier = Modifier
) {
    val strokeWidth = with(LocalDensity.current) { 2.dp.toPx() }

    Canvas(modifier = modifier) {
        drawLine(
            color = color,
            start = Offset(size.width * 0.25f, size.height * 0.5f),
            end = Offset(size.width * 0.78f, size.height * 0.5f),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round
        )
        drawLine(
            color = color,
            start = Offset(size.width * 0.25f, size.height * 0.5f),
            end = Offset(size.width * 0.48f, size.height * 0.28f),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round
        )
        drawLine(
            color = color,
            start = Offset(size.width * 0.25f, size.height * 0.5f),
            end = Offset(size.width * 0.48f, size.height * 0.72f),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AppTopBarRecipePreview() {
    ProyectoSemestralTheme {
        AppTopBar(
            title = "Receta",
            onBackClick = {}
        )
    }
}
