package com.example.proyecto_semestral.ui.components.admin

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.proyecto_semestral.ui.theme.ProyectoSemestralTheme

/** Campo de búsqueda para las listas del panel administrativo. */
@Composable
fun AdminSearchField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Buscar por nombre o correo",
    enabled: Boolean = true
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp),
        enabled = enabled,
        singleLine = true,
        leadingIcon = {
            SearchIcon(
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(28.dp)
            )
        },
        placeholder = {
            Text(placeholder, style = MaterialTheme.typography.bodyLarge)
        },
        textStyle = MaterialTheme.typography.bodyLarge,
        shape = RoundedCornerShape(14.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
            cursorColor = MaterialTheme.colorScheme.primary,
            focusedTextColor = MaterialTheme.colorScheme.onSurface,
            unfocusedTextColor = MaterialTheme.colorScheme.onSurface
        )
    )
}

@Composable
private fun SearchIcon(color: Color, modifier: Modifier = Modifier) {
    Canvas(modifier) {
        val stroke = size.minDimension * 0.09f
        drawCircle(
            color = color,
            radius = size.minDimension * 0.27f,
            center = Offset(size.width * 0.42f, size.height * 0.42f),
            style = Stroke(stroke)
        )
        drawLine(
            color,
            Offset(size.width * 0.62f, size.height * 0.62f),
            Offset(size.width * 0.84f, size.height * 0.84f),
            stroke,
            StrokeCap.Round
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFEAF2EE, widthDp = 430)
@Composable
private fun AdminSearchFieldPreview() {
    ProyectoSemestralTheme {
        var query by rememberSaveable { mutableStateOf("") }
        AdminSearchField(value = query, onValueChange = { query = it })
    }
}
