package com.example.proyecto_semestral.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.ui.graphics.Color
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
    val containerColor by animateColorAsState(
        targetValue = if (selected) {
            MaterialTheme.colorScheme.surfaceVariant
        } else {
            MaterialTheme.colorScheme.background
        },
        animationSpec = tween(durationMillis = 280),
        label = "interestContainerColor"
    )
    val borderColor by animateColorAsState(
        targetValue = if (selected) {
            Color.Transparent
        } else {
            MaterialTheme.colorScheme.outline.copy(alpha = 0.35f)
        },
        animationSpec = tween(durationMillis = 280),
        label = "interestBorderColor"
    )
    val borderWidth by animateDpAsState(
        targetValue = if (selected) 0.dp else 1.5.dp,
        animationSpec = tween(durationMillis = 280),
        label = "interestBorderWidth"
    )

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(96.dp)
                .background(
                    color = containerColor,
                    shape = shape
                )
                .border(
                    width = borderWidth,
                    color = borderColor,
                    shape = shape
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
    val checkboxSize by animateDpAsState(
        targetValue = if (checked) 46.dp else 44.dp,
        animationSpec = tween(durationMillis = 220),
        label = "interestCheckboxSize"
    )
    val backgroundColor by animateColorAsState(
        targetValue = if (checked) primaryColor else Color.Transparent,
        animationSpec = tween(durationMillis = 220),
        label = "interestCheckboxBackground"
    )
    val borderColor by animateColorAsState(
        targetValue = if (checked) Color.Transparent else outlineColor,
        animationSpec = tween(durationMillis = 220),
        label = "interestCheckboxBorder"
    )
    val borderWidth by animateDpAsState(
        targetValue = if (checked) 0.dp else 2.dp,
        animationSpec = tween(durationMillis = 220),
        label = "interestCheckboxBorderWidth"
    )
    val checkProgress by animateFloatAsState(
        targetValue = if (checked) 1f else 0f,
        animationSpec = tween(durationMillis = 260),
        label = "interestCheckProgress"
    )

    Canvas(
        modifier = modifier
            .size(checkboxSize)
            .background(backgroundColor, shape)
            .border(borderWidth, borderColor, shape)
    ) {
        if (checkProgress > 0f) {
            val firstLineProgress = (checkProgress * 2f).coerceIn(0f, 1f)
            val secondLineProgress = ((checkProgress - 0.5f) * 2f).coerceIn(0f, 1f)
            val firstStart = Offset(size.width * 0.24f, size.height * 0.52f)
            val firstEnd = Offset(size.width * 0.43f, size.height * 0.70f)
            val secondEnd = Offset(size.width * 0.77f, size.height * 0.31f)

            drawLine(
                color = checkColor,
                start = firstStart,
                end = Offset(
                    x = firstStart.x + (firstEnd.x - firstStart.x) * firstLineProgress,
                    y = firstStart.y + (firstEnd.y - firstStart.y) * firstLineProgress
                ),
                strokeWidth = 3.5.dp.toPx(),
                cap = StrokeCap.Round
            )
            if (secondLineProgress > 0f) {
                drawLine(
                    color = checkColor,
                    start = firstEnd,
                    end = Offset(
                        x = firstEnd.x + (secondEnd.x - firstEnd.x) * secondLineProgress,
                        y = firstEnd.y + (secondEnd.y - firstEnd.y) * secondLineProgress
                    ),
                    strokeWidth = 3.5.dp.toPx(),
                    cap = StrokeCap.Round
                )
            }
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
