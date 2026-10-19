package com.example.proyecto_semestral.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.selection.toggleable
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyecto_semestral.ui.theme.ProyectoSemestralTheme

@Composable
fun InterestSelection(
    interests: List<String>,
    selectedInterests: Set<String>,
    onInterestChange: (interest: String, selected: Boolean) -> Unit,
    modifier: Modifier = Modifier,
    title: String = "Selección de intereses"
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = title,
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.headlineLarge.copy(
                fontSize = 36.sp,
                lineHeight = 42.sp,
                fontWeight = FontWeight.Bold
            )
        )

        Spacer(modifier = Modifier.height(30.dp))

        interests.forEachIndexed { index, interest ->
            val isSelected = interest in selectedInterests

            InterestSelectionItem(
                text = interest,
                selected = isSelected,
                onSelectedChange = { selected ->
                    onInterestChange(interest, selected)
                }
            )

            if (index != interests.lastIndex) {
                Spacer(modifier = Modifier.height(22.dp))
            }
        }
    }
}

@Composable
fun InterestSelectionItem(
    text: String,
    selected: Boolean,
    onSelectedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(24.dp)

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(96.dp)
                .then(
                    if (selected) {
                        Modifier.background(
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = shape
                        )
                    } else {
                        Modifier.border(
                            width = 1.5.dp,
                            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.35f),
                            shape = shape
                        )
                    }
                )
                .clip(shape)
                .toggleable(
                    value = selected,
                    role = Role.Checkbox,
                    onValueChange = onSelectedChange
                )
                .padding(horizontal = 30.dp),
            horizontalArrangement = Arrangement.spacedBy(34.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            InterestCheckbox(checked = selected)

            Text(
                text = text,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontSize = 28.sp,
                    lineHeight = 34.sp,
                    fontWeight = FontWeight.Normal
                )
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = if (selected) "Seleccionado" else "Sin seleccionar",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp),
            modifier = Modifier.padding(horizontal = 12.dp)
        )
    }
}

@Composable
private fun InterestCheckbox(
    checked: Boolean,
    modifier: Modifier = Modifier
) {
    val primaryColor = MaterialTheme.colorScheme.primary
    val outlineColor = MaterialTheme.colorScheme.outline
    val checkColor = MaterialTheme.colorScheme.onPrimary
    val shape = RoundedCornerShape(8.dp)

    Canvas(
        modifier = modifier
            .size(44.dp)
            .then(
                if (checked) {
                    Modifier.background(primaryColor, shape)
                } else {
                    Modifier.border(2.dp, outlineColor, shape)
                }
            )
    ) {
        if (checked) {
            drawLine(
                color = checkColor,
                start = Offset(size.width * 0.24f, size.height * 0.52f),
                end = Offset(size.width * 0.43f, size.height * 0.70f),
                strokeWidth = 3.5.dp.toPx(),
                cap = StrokeCap.Round
            )
            drawLine(
                color = checkColor,
                start = Offset(size.width * 0.43f, size.height * 0.70f),
                end = Offset(size.width * 0.77f, size.height * 0.31f),
                strokeWidth = 3.5.dp.toPx(),
                cap = StrokeCap.Round
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 430)
@Composable
private fun InterestSelectionPreview() {
    ProyectoSemestralTheme {
        var selectedInterests by rememberSaveable {
            mutableStateOf(setOf("Chocolatería"))
        }

        InterestSelection(
            interests = listOf("Chocolatería", "Bollería"),
            selectedInterests = selectedInterests,
            onInterestChange = { interest, selected ->
                selectedInterests = if (selected) {
                    selectedInterests + interest
                } else {
                    selectedInterests - interest
                }
            },
            modifier = Modifier.padding(24.dp)
        )
    }
}
