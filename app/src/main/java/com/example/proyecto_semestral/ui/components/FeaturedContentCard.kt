package com.example.proyecto_semestral.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.painterResource
import com.example.proyecto_semestral.R
import com.example.proyecto_semestral.ui.theme.ProyectoSemestralTheme

@Composable
fun FeaturedContentCard(
    category: String,
    title: String,
    duration: String,
    image: Painter,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    imageContentDescription: String? = title
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(MaterialTheme.colorScheme.primary)
            .clickable(role = Role.Button, onClick = onClick)
    ) {
        Image(
            painter = image,
            contentDescription = imageContentDescription,
            modifier = Modifier
                .fillMaxWidth()
                .height(210.dp),
            contentScale = ContentScale.Crop
        )

        Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp)) {
            Text(
                text = category.uppercase(),
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.85f),
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontSize = 28.sp,
                    lineHeight = 34.sp,
                    fontWeight = FontWeight.Bold
                ),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(14.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    ClockIcon(
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.size(10.dp))
                    Text(
                        text = duration,
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                ArrowUpRightIcon(
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
private fun ClockIcon(color: Color, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val strokeWidth = 2.dp.toPx()
        drawCircle(color = color, radius = size.minDimension * 0.42f, style = Stroke(strokeWidth))
        drawLine(color, Offset(size.width * 0.5f, size.height * 0.25f), Offset(size.width * 0.5f, size.height * 0.52f), strokeWidth, cap = StrokeCap.Round)
        drawLine(color, Offset(size.width * 0.5f, size.height * 0.5f), Offset(size.width * 0.7f, size.height * 0.62f), strokeWidth, cap = StrokeCap.Round)
    }
}

@Composable
private fun ArrowUpRightIcon(color: Color, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val strokeWidth = 2.5.dp.toPx()
        drawLine(color, Offset(size.width * 0.25f, size.height * 0.75f), Offset(size.width * 0.76f, size.height * 0.24f), strokeWidth, cap = StrokeCap.Round)
        drawLine(color, Offset(size.width * 0.43f, size.height * 0.24f), Offset(size.width * 0.76f, size.height * 0.24f), strokeWidth, cap = StrokeCap.Round)
        drawLine(color, Offset(size.width * 0.76f, size.height * 0.24f), Offset(size.width * 0.76f, size.height * 0.57f), strokeWidth, cap = StrokeCap.Round)
    }
}

@Preview(showBackground = true)
@Composable
private fun FeaturedContentCardPreview() {
    ProyectoSemestralTheme {
        FeaturedContentCard(
            category = "Recetas / Galletas",
            title = "Galletas con chips de chocolate",
            duration = "35 min",
            image = painterResource(R.drawable.ic_launcher_background),
            onClick = {},
            modifier = Modifier.padding(24.dp)
        )
    }
}
