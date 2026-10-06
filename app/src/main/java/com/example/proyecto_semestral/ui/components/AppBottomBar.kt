package com.example.proyecto_semestral.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
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
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.proyecto_semestral.ui.theme.ProyectoSemestralTheme

enum class BottomBarItem {
    Home,
    Explore,
    Contact,
    Profile
}

@Composable
fun AppBottomBar(
    selectedItem: BottomBarItem,
    onItemSelected: (BottomBarItem) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .navigationBarsPadding()
            .height(108.dp)
            .padding(horizontal = 18.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        BottomBarButton(
            item = BottomBarItem.Home,
            label = "Inicio",
            selected = selectedItem == BottomBarItem.Home,
            onClick = { onItemSelected(BottomBarItem.Home) },
            modifier = Modifier.weight(1f)
        )
        BottomBarButton(
            item = BottomBarItem.Explore,
            label = "Explorar",
            selected = selectedItem == BottomBarItem.Explore,
            onClick = { onItemSelected(BottomBarItem.Explore) },
            modifier = Modifier.weight(1f)
        )
        BottomBarButton(
            item = BottomBarItem.Contact,
            label = "Contacto",
            selected = selectedItem == BottomBarItem.Contact,
            onClick = { onItemSelected(BottomBarItem.Contact) },
            modifier = Modifier.weight(1f)
        )
        BottomBarButton(
            item = BottomBarItem.Profile,
            label = "Mi perfil",
            selected = selectedItem == BottomBarItem.Profile,
            onClick = { onItemSelected(BottomBarItem.Profile) },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun BottomBarButton(
    item: BottomBarItem,
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val selectedColor = MaterialTheme.colorScheme.primary
    val unselectedColor = MaterialTheme.colorScheme.onSurfaceVariant
    val iconColor by animateColorAsState(
        targetValue = if (selected) selectedColor else unselectedColor,
        animationSpec = tween(durationMillis = 240),
        label = "bottomBarIconColor"
    )
    val textColor by animateColorAsState(
        targetValue = if (selected) selectedColor else unselectedColor,
        animationSpec = tween(durationMillis = 240),
        label = "bottomBarTextColor"
    )
    val indicatorColor by animateColorAsState(
        targetValue = if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.12f) else Color.Transparent,
        animationSpec = tween(durationMillis = 240),
        label = "bottomBarIndicatorColor"
    )
    val indicatorWidth by animateDpAsState(
        targetValue = if (selected) 118.dp else 48.dp,
        animationSpec = tween(durationMillis = 240),
        label = "bottomBarIndicatorWidth"
    )

    Column(
        modifier = modifier
            .clickable(
                role = Role.Tab,
                onClick = onClick
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(width = indicatorWidth, height = 52.dp)
                .background(
                    color = indicatorColor,
                    shape = RoundedCornerShape(percent = 50)
                ),
            contentAlignment = Alignment.Center
        ) {
            BottomBarIcon(
                item = item,
                color = iconColor,
                selected = selected,
                modifier = Modifier.size(32.dp)
            )
        }

        Text(
            text = label,
            color = textColor,
            style = MaterialTheme.typography.labelLarge.copy(
                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal
            )
        )
    }
}

@Composable
private fun BottomBarIcon(
    item: BottomBarItem,
    color: Color,
    selected: Boolean,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val strokeWidth = 3.dp.toPx()
        when (item) {
            BottomBarItem.Home -> {
                val roof = Path().apply {
                    moveTo(size.width * 0.18f, size.height * 0.48f)
                    lineTo(size.width * 0.5f, size.height * 0.2f)
                    lineTo(size.width * 0.82f, size.height * 0.48f)
                    lineTo(size.width * 0.82f, size.height * 0.86f)
                    lineTo(size.width * 0.6f, size.height * 0.86f)
                    lineTo(size.width * 0.6f, size.height * 0.62f)
                    lineTo(size.width * 0.4f, size.height * 0.62f)
                    lineTo(size.width * 0.4f, size.height * 0.86f)
                    lineTo(size.width * 0.18f, size.height * 0.86f)
                    close()
                }
                if (selected) {
                    drawPath(path = roof, color = color)
                } else {
                    drawPath(path = roof, color = color, style = Stroke(width = strokeWidth, join = androidx.compose.ui.graphics.StrokeJoin.Round))
                }
            }

            BottomBarItem.Explore -> {
                drawCircle(
                    color = color,
                    radius = size.minDimension * 0.3f,
                    center = Offset(size.width * 0.43f, size.height * 0.43f),
                    style = Stroke(width = strokeWidth)
                )
                drawLine(
                    color = color,
                    start = Offset(size.width * 0.64f, size.height * 0.64f),
                    end = Offset(size.width * 0.86f, size.height * 0.86f),
                    strokeWidth = strokeWidth,
                    cap = StrokeCap.Round
                )
            }

            BottomBarItem.Contact -> {
                drawRoundRect(
                    color = color,
                    topLeft = Offset(size.width * 0.14f, size.height * 0.24f),
                    size = Size(size.width * 0.72f, size.height * 0.52f),
                    cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx()),
                    style = Stroke(width = strokeWidth)
                )
                drawLine(
                    color = color,
                    start = Offset(size.width * 0.18f, size.height * 0.3f),
                    end = Offset(size.width * 0.5f, size.height * 0.52f),
                    strokeWidth = strokeWidth,
                    cap = StrokeCap.Round
                )
                drawLine(
                    color = color,
                    start = Offset(size.width * 0.82f, size.height * 0.3f),
                    end = Offset(size.width * 0.5f, size.height * 0.52f),
                    strokeWidth = strokeWidth,
                    cap = StrokeCap.Round
                )
            }

            BottomBarItem.Profile -> {
                drawCircle(
                    color = color,
                    radius = size.minDimension * 0.18f,
                    center = Offset(size.width * 0.5f, size.height * 0.32f),
                    style = if (selected) androidx.compose.ui.graphics.drawscope.Fill else Stroke(width = strokeWidth)
                )
                drawRoundRect(
                    color = color,
                    topLeft = Offset(size.width * 0.22f, size.height * 0.58f),
                    size = Size(size.width * 0.56f, size.height * 0.24f),
                    cornerRadius = CornerRadius(18.dp.toPx(), 18.dp.toPx()),
                    style = if (selected) androidx.compose.ui.graphics.drawscope.Fill else Stroke(width = strokeWidth)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AppBottomBarPreview() {
    ProyectoSemestralTheme {
        var selectedItem by rememberSaveable { mutableStateOf(BottomBarItem.Home) }

        AppBottomBar(
            selectedItem = selectedItem,
            onItemSelected = { selectedItem = it }
        )
    }
}
