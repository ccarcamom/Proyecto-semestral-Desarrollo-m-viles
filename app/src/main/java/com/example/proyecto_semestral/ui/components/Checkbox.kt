package com.example.proyecto_semestral.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment

@Composable
fun CheckBox(valor: Boolean, onChecked: (Boolean) -> Unit, error: String?) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = valor, onCheckedChange = onChecked)
            Text("Aceptar los términos y condiciones")
        }
        error?.let {
            Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodyLarge)
        }
    }
}
