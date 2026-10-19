package com.example.proyecto_semestral.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyecto_semestral.ui.theme.ProyectoSemestralTheme

@Composable
fun ContactValidationForm(
    email: String,
    onEmailChange: (String) -> Unit,
    message: String,
    onMessageChange: (String) -> Unit,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier,
    emailError: String? = null,
    messageError: String? = null
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "Validación de contacto",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.headlineLarge.copy(
                fontSize = 36.sp,
                lineHeight = 42.sp,
                fontWeight = FontWeight.Bold
            )
        )

        Spacer(modifier = Modifier.height(26.dp))

        ValidationTextField(
            value = email,
            onValueChange = onEmailChange,
            label = "Correo electrónico",
            placeholder = "tu@correo.com",
            error = emailError,
            singleLine = true
        )

        Spacer(modifier = Modifier.height(26.dp))

        ValidationTextField(
            value = message,
            onValueChange = onMessageChange,
            label = "Tu mensaje",
            placeholder = "Cuéntanos tu consulta...",
            error = messageError,
            singleLine = false,
            modifier = Modifier.height(220.dp)
        )

        Spacer(modifier = Modifier.height(26.dp))

        PrimaryButton(
            text = "Enviar consulta",
            onClick = onSubmit,
            modifier = Modifier.height(64.dp)
        )
    }
}

@Composable
private fun ValidationTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    error: String?,
    singleLine: Boolean,
    modifier: Modifier = Modifier
) {
    val isError = error != null

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleLarge.copy(
                fontSize = 23.sp,
                fontWeight = FontWeight.SemiBold
            )
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = modifier
                .fillMaxWidth()
                .then(if (singleLine) Modifier.height(68.dp) else Modifier),
            placeholder = {
                Text(
                    text = placeholder,
                    style = MaterialTheme.typography.bodyLarge.copy(fontSize = 22.sp)
                )
            },
            trailingIcon = if (isError) {
                { ErrorIndicator(modifier = Modifier.size(28.dp)) }
            } else {
                null
            },
            isError = isError,
            singleLine = singleLine,
            textStyle = MaterialTheme.typography.bodyLarge.copy(fontSize = 22.sp),
            shape = MaterialTheme.shapes.medium,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                errorBorderColor = MaterialTheme.colorScheme.error,
                errorCursorColor = MaterialTheme.colorScheme.error,
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                focusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )

        if (error != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                ErrorIndicator(modifier = Modifier.size(26.dp))
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp),
                    modifier = Modifier.padding(start = 12.dp)
                )
            }
        }
    }
}

@Composable
private fun ErrorIndicator(modifier: Modifier = Modifier) {
    val errorColor = MaterialTheme.colorScheme.error

    Canvas(modifier = modifier) {
        val stroke = 2.5.dp.toPx()
        drawCircle(color = errorColor, style = Stroke(stroke))
        drawLine(
            color = errorColor,
            start = Offset(size.width / 2, size.height * 0.27f),
            end = Offset(size.width / 2, size.height * 0.58f),
            strokeWidth = stroke,
            cap = StrokeCap.Round
        )
        drawCircle(
            color = errorColor,
            radius = stroke / 2,
            center = Offset(size.width / 2, size.height * 0.75f)
        )
    }
}

@Preview(showBackground = true, widthDp = 430, heightDp = 760)
@Composable
private fun ContactValidationFormPreview() {
    ProyectoSemestralTheme {
        var email by rememberSaveable { mutableStateOf("andrea") }
        var message by rememberSaveable { mutableStateOf("") }

        ContactValidationForm(
            email = email,
            onEmailChange = { email = it },
            message = message,
            onMessageChange = { message = it },
            onSubmit = {},
            emailError = "Ingresa un correo válido.",
            messageError = "Escribe tu consulta.",
            modifier = Modifier.padding(24.dp)
        )
    }
}
