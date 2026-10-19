package com.example.proyecto_semestral.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.proyecto_semestral.ui.theme.ProyectoSemestralTheme

@Composable
fun SimulatedConsultationDialog(
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier,
    title: String = "Consulta simulada",
    message: String = "Esta es una demostración. No se envió ningún mensaje a la empresa.",
    confirmLabel: String = "Entendido"
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        SimulatedConsultationDialogContent(
            onConfirm = onConfirm,
            title = title,
            message = message,
            confirmLabel = confirmLabel,
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        )
    }
}

@Composable
private fun SimulatedConsultationDialogContent(
    onConfirm: () -> Unit,
    title: String,
    message: String,
    confirmLabel: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(28.dp),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 14.dp
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            SuccessIcon()

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = title,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontSize = 30.sp,
                    lineHeight = 36.sp,
                    fontWeight = FontWeight.Bold
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = message,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 19.sp,
                    lineHeight = 28.sp
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(26.dp))

            PrimaryButton(
                text = confirmLabel,
                onClick = onConfirm,
                modifier = Modifier.height(58.dp)
            )
        }
    }
}

@Composable
private fun SuccessIcon(modifier: Modifier = Modifier) {
    val checkColor = MaterialTheme.colorScheme.primary

    Canvas(
        modifier = modifier
            .size(92.dp)
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = CircleShape
            )
    ) {
        val stroke = 6.dp.toPx()
        drawLine(
            color = checkColor,
            start = Offset(size.width * 0.28f, size.height * 0.52f),
            end = Offset(size.width * 0.44f, size.height * 0.68f),
            strokeWidth = stroke,
            cap = StrokeCap.Round
        )
        drawLine(
            color = checkColor,
            start = Offset(size.width * 0.44f, size.height * 0.68f),
            end = Offset(size.width * 0.73f, size.height * 0.34f),
            strokeWidth = stroke,
            cap = StrokeCap.Round
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF4F6F5, widthDp = 430)
@Composable
private fun SimulatedConsultationDialogPreview() {
    ProyectoSemestralTheme {
        SimulatedConsultationDialogContent(
            onConfirm = {},
            title = "Consulta simulada",
            message = "Esta es una demostración. No se envió ningún mensaje a la empresa.",
            confirmLabel = "Entendido",
            modifier = Modifier.padding(24.dp)
        )
    }
}
