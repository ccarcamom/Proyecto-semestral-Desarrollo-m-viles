package com.example.proyecto_semestral.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyecto_semestral.ui.theme.ProyectoSemestralTheme

@Composable
fun EmptyState(
    title: String,
    description: String,
    buttonText: String,
    onButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(128.dp)
                .background(
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            SearchIcon(
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(58.dp)
            )
        }

        Spacer(modifier = Modifier.height(30.dp))
        Text(
            text = title,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp,
                lineHeight = 38.sp
            ),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = description,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontSize = 22.sp,
                lineHeight = 30.sp
            ),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(36.dp))
        Button(
            onClick = onButtonClick,
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 320.dp)
                .height(64.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            shape = RoundedCornerShapeCompat
        ) {
            Text(
                text = buttonText,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
        }
    }
}

private val RoundedCornerShapeCompat = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)

@Composable
private fun SearchIcon(
    color: Color,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val strokeWidth = 5.dp.toPx()
        drawCircle(
            color = color,
            radius = size.minDimension * 0.32f,
            center = Offset(size.width * 0.43f, size.height * 0.43f),
            style = Stroke(width = strokeWidth)
        )
        drawLine(
            color = color,
            start = Offset(size.width * 0.64f, size.height * 0.64f),
            end = Offset(size.width * 0.86f, size.height * 0.86f),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EmptyStatePreview() {
    ProyectoSemestralTheme {
        EmptyState(
            title = "No encontramos resultados",
            description = "Prueba con otra palabra o elimina los filtros.",
            buttonText = "Limpiar filtros",
            onButtonClick = {}
        )
    }
}
