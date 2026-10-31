package com.example.proyecto_semestral.ui.components.admin

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.proyecto_semestral.ui.theme.ProyectoSemestralTheme

/** Tarjeta con la información y las acciones administrativas de un usuario. */
@Composable
fun UserCard(
    name: String,
    email: String,
    onEdit: () -> Unit,
    onBlock: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
    initials: String = userInitials(name),
    isActive: Boolean = true
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surface,
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            MaterialTheme.colorScheme.outline.copy(alpha = 0.45f)
        ),
        shadowElevation = 1.dp
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(76.dp)
                        .background(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = initials.uppercase(),
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }

                Spacer(modifier = Modifier.width(18.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = name,
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.titleLarge,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = email,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    StatusBadge(isActive = isActive)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                UserActionButton(
                    text = "Editar",
                    onClick = onEdit,
                    icon = { color -> EditIcon(color, Modifier.size(22.dp)) },
                    modifier = Modifier.weight(1f)
                )
                UserActionButton(
                    text = if (isActive) "Bloquear" else "Desbloquear",
                    onClick = onBlock,
                    icon = { color -> BlockActionIcon(color, Modifier.size(22.dp)) },
                    modifier = Modifier.weight(1f)
                )
                TextButton(
                    onClick = onDelete,
                    modifier = Modifier
                        .size(width = 58.dp, height = 48.dp)
                        .background(
                            color = MaterialTheme.colorScheme.error.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(12.dp)
                        ),
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    DeleteActionIcon(
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(23.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun StatusBadge(isActive: Boolean) {
    val backgroundColor = if (isActive) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        MaterialTheme.colorScheme.error.copy(alpha = 0.1f)
    }
    val contentColor = if (isActive) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.error
    }

    Row(
        modifier = Modifier
            .background(backgroundColor, RoundedCornerShape(50))
            .padding(horizontal = 14.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .background(contentColor, CircleShape)
        )
        Text(
            text = if (isActive) "Activo" else "Bloqueado",
            color = contentColor,
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold)
        )
    }
}

@Composable
private fun UserActionButton(
    text: String,
    onClick: () -> Unit,
    icon: @Composable (Color) -> Unit,
    modifier: Modifier = Modifier
) {
    val color = MaterialTheme.colorScheme.primary
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.height(48.dp),
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            MaterialTheme.colorScheme.outline.copy(alpha = 0.7f)
        ),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = color)
    ) {
        icon(color)
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun EditIcon(color: Color, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val stroke = size.minDimension * 0.11f
        val path = Path().apply {
            moveTo(size.width * 0.2f, size.height * 0.78f)
            lineTo(size.width * 0.27f, size.height * 0.52f)
            lineTo(size.width * 0.68f, size.height * 0.11f)
            lineTo(size.width * 0.89f, size.height * 0.32f)
            lineTo(size.width * 0.48f, size.height * 0.73f)
            close()
        }
        drawPath(path, color = color, style = Stroke(width = stroke, join = androidx.compose.ui.graphics.StrokeJoin.Round))
        drawLine(
            color = color,
            start = Offset(size.width * 0.29f, size.height * 0.51f),
            end = Offset(size.width * 0.49f, size.height * 0.71f),
            strokeWidth = stroke,
            cap = StrokeCap.Round
        )
    }
}

@Composable
private fun BlockActionIcon(color: Color, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val stroke = size.minDimension * 0.1f
        drawCircle(color, radius = size.minDimension * 0.37f, style = Stroke(stroke))
        drawLine(
            color,
            Offset(size.width * 0.25f, size.height * 0.25f),
            Offset(size.width * 0.75f, size.height * 0.75f),
            stroke,
            StrokeCap.Round
        )
    }
}

@Composable
private fun DeleteActionIcon(color: Color, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val stroke = size.minDimension * 0.09f
        drawLine(color, Offset(size.width * 0.2f, size.height * 0.25f), Offset(size.width * 0.8f, size.height * 0.25f), stroke, StrokeCap.Round)
        drawLine(color, Offset(size.width * 0.4f, size.height * 0.13f), Offset(size.width * 0.6f, size.height * 0.13f), stroke, StrokeCap.Round)
        drawRoundRect(
            color = color,
            topLeft = Offset(size.width * 0.29f, size.height * 0.27f),
            size = Size(size.width * 0.42f, size.height * 0.58f),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(stroke),
            style = Stroke(stroke)
        )
        drawLine(color, Offset(size.width * 0.43f, size.height * 0.42f), Offset(size.width * 0.43f, size.height * 0.7f), stroke, StrokeCap.Round)
        drawLine(color, Offset(size.width * 0.57f, size.height * 0.42f), Offset(size.width * 0.57f, size.height * 0.7f), stroke, StrokeCap.Round)
    }
}

private fun userInitials(name: String): String = name
    .trim()
    .split(Regex("\\s+"))
    .filter { it.isNotEmpty() }
    .take(2)
    .joinToString("") { it.first().uppercaseChar().toString() }
    .ifEmpty { "?" }

@Preview(showBackground = true, backgroundColor = 0xFFF1F7F4, widthDp = 480)
@Composable
private fun UserCardPreview() {
    ProyectoSemestralTheme {
        UserCard(
            name = "Ana Martínez",
            email = "ana.martinez@correo.cl",
            onEdit = {},
            onBlock = {},
            onDelete = {},
            modifier = Modifier.padding(14.dp)
        )
    }
}
