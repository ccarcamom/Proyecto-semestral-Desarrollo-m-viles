package com.example.proyecto_semestral.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyecto_semestral.ui.theme.ProyectoSemestralTheme

@Composable
fun CourseLessonCard(
    lessonNumber: Int,
    title: String,
    duration: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    sectionTitle: String = "Lección del curso"
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = sectionTitle,
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.headlineSmall.copy(
                fontSize = 28.sp,
                lineHeight = 34.sp,
                fontWeight = FontWeight.Bold
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(146.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .clickable(onClick = onClick)
                .padding(horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(92.dp)
                    .background(MaterialTheme.colorScheme.primary, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "%02d".format(lessonNumber),
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.width(26.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = title,
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontSize = 27.sp,
                        lineHeight = 32.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    ClockIcon(modifier = Modifier.size(30.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = duration,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyLarge.copy(fontSize = 22.sp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(18.dp))

            CoursePlayIcon(modifier = Modifier.size(72.dp))
        }
    }
}

@Composable
private fun ClockIcon(modifier: Modifier = Modifier) {
    val color = MaterialTheme.colorScheme.onSurfaceVariant

    Canvas(modifier = modifier) {
        val stroke = 2.5.dp.toPx()
        val center = Offset(size.width / 2, size.height / 2)
        drawCircle(color = color, style = Stroke(stroke))
        drawLine(
            color = color,
            start = center,
            end = Offset(center.x, size.height * 0.27f),
            strokeWidth = stroke,
            cap = StrokeCap.Round
        )
        drawLine(
            color = color,
            start = center,
            end = Offset(size.width * 0.68f, size.height * 0.60f),
            strokeWidth = stroke,
            cap = StrokeCap.Round
        )
    }
}

@Composable
private fun CoursePlayIcon(modifier: Modifier = Modifier) {
    val color = MaterialTheme.colorScheme.primary

    Canvas(modifier = modifier) {
        val stroke = 2.5.dp.toPx()
        drawCircle(
            color = color,
            radius = size.minDimension / 2 - stroke / 2,
            style = Stroke(stroke)
        )

        val triangle = Path().apply {
            moveTo(size.width * 0.43f, size.height * 0.34f)
            lineTo(size.width * 0.70f, size.height * 0.50f)
            lineTo(size.width * 0.43f, size.height * 0.66f)
            close()
        }
        drawPath(path = triangle, color = color)
    }
}

@Preview(showBackground = true, widthDp = 760)
@Composable
private fun CourseLessonCardPreview() {
    ProyectoSemestralTheme {
        CourseLessonCard(
            lessonNumber = 1,
            title = "Conoce el chocolate",
            duration = "8 min",
            onClick = {},
            modifier = Modifier.padding(24.dp)
        )
    }
}
