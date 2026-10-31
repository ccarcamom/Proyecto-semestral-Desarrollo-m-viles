package com.example.proyecto_semestral.ui.components.admin

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.proyecto_semestral.ui.theme.ProyectoSemestralTheme

/** Diálogo de confirmación para eliminar definitivamente un usuario. */
@Composable
fun DeleteUserDialog(
    userName: String,
    onConfirm: () -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        DeleteUserDialogContent(
            userName = userName,
            onConfirm = onConfirm,
            onCancel = onDismissRequest,
            modifier = modifier
        )
    }
}

@Composable
private fun DeleteUserDialogContent(
    userName: String,
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .padding(horizontal = 24.dp)
            .fillMaxWidth()
            .widthIn(max = 500.dp),
        shape = RoundedCornerShape(28.dp),
        color = MaterialTheme.colorScheme.surface
    ) {
        Column(modifier = Modifier.padding(32.dp)) {
            TrashIcon(
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(42.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "¿Eliminar usuario?",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Se eliminará la cuenta de $userName.\nEsta acción no se puede deshacer.",
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(26.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(onClick = onCancel) {
                    Text(
                        text = "Cancelar",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Button(
                    onClick = onConfirm,
                    modifier = Modifier.height(52.dp),
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onError
                    )
                ) {
                    Text("Eliminar", style = MaterialTheme.typography.titleMedium)
                }
            }
        }
    }
}

@Composable
private fun TrashIcon(color: Color, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val stroke = size.minDimension * 0.075f
        drawLine(
            color = color,
            start = Offset(size.width * 0.23f, size.height * 0.25f),
            end = Offset(size.width * 0.77f, size.height * 0.25f),
            strokeWidth = stroke,
            cap = StrokeCap.Round
        )
        drawLine(
            color = color,
            start = Offset(size.width * 0.4f, size.height * 0.14f),
            end = Offset(size.width * 0.6f, size.height * 0.14f),
            strokeWidth = stroke,
            cap = StrokeCap.Round
        )
        drawRoundRect(
            color = color,
            topLeft = Offset(size.width * 0.3f, size.height * 0.25f),
            size = Size(size.width * 0.4f, size.height * 0.6f),
            cornerRadius = CornerRadius(stroke),
            style = Stroke(width = stroke)
        )
        drawLine(color, Offset(size.width * 0.43f, size.height * 0.4f), Offset(size.width * 0.43f, size.height * 0.7f), stroke, StrokeCap.Round)
        drawLine(color, Offset(size.width * 0.57f, size.height * 0.4f), Offset(size.width * 0.57f, size.height * 0.7f), stroke, StrokeCap.Round)
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFEAF2EE, widthDp = 540)
@Composable
private fun DeleteUserDialogPreview() {
    ProyectoSemestralTheme {
        DeleteUserDialogContent(
            userName = "Ana Martínez",
            onConfirm = {},
            onCancel = {},
            modifier = Modifier.padding(vertical = 24.dp)
        )
    }
}
