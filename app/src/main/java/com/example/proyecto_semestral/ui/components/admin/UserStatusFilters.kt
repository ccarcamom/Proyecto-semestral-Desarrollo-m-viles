package com.example.proyecto_semestral.ui.components.admin

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.proyecto_semestral.ui.theme.ProyectoSemestralTheme

enum class UserStatusFilter(val label: String) {
    ALL("Todos"),
    ACTIVE("Activos"),
    BLOCKED("Bloqueados")
}

/** Filtros para seleccionar usuarios por su estado. */
@Composable
fun UserStatusFilters(
    selectedFilter: UserStatusFilter,
    onFilterSelected: (UserStatusFilter) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        UserStatusFilter.entries.forEach { filter ->
            val selected = filter == selectedFilter
            if (selected) {
                Button(
                    onClick = { onFilterSelected(filter) },
                    modifier = Modifier
                        .weight(1f)
                        .height(54.dp),
                    shape = MaterialTheme.shapes.large,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(filter.label, style = MaterialTheme.typography.labelLarge)
                }
            } else {
                OutlinedButton(
                    onClick = { onFilterSelected(filter) },
                    modifier = Modifier
                        .weight(1f)
                        .height(54.dp),
                    shape = MaterialTheme.shapes.large,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.Transparent,
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                ) {
                    Text(filter.label, style = MaterialTheme.typography.labelLarge)
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFEAF2EE, widthDp = 430)
@Composable
private fun UserStatusFiltersPreview() {
    ProyectoSemestralTheme {
        UserStatusFilters(
            selectedFilter = UserStatusFilter.ALL,
            onFilterSelected = {}
        )
    }
}
