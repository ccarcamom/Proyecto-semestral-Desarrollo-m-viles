package com.example.proyecto_semestral.ui.screens.contact

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyecto_semestral.ui.components.AppBottomBar
import com.example.proyecto_semestral.ui.components.AppTextField
import com.example.proyecto_semestral.ui.components.BottomBarItem
import com.example.proyecto_semestral.ui.components.BrandLogo
import com.example.proyecto_semestral.ui.components.PrimaryButton
import com.example.proyecto_semestral.ui.theme.ProyectoSemestralTheme

@Composable
fun ContactScreen(
    initialName: String = "",
    initialEmail: String = "",
    onBottomBarItemSelected: (BottomBarItem) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var name by rememberSaveable { mutableStateOf(initialName) }
    var email by rememberSaveable { mutableStateOf(initialEmail) }
    var message by rememberSaveable { mutableStateOf("") }
    var selectedTopic by rememberSaveable { mutableStateOf(contactTopics.first()) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            Column {
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    thickness = 1.dp
                )
                AppBottomBar(
                    selectedItem = BottomBarItem.Contact,
                    onItemSelected = onBottomBarItemSelected
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 22.dp)
        ) {
            Spacer(modifier = Modifier.height(14.dp))
            BrandLogo(tagline = "ESTAMOS PARA AYUDARTE")

            Spacer(modifier = Modifier.height(26.dp))
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                ChatGlyph(
                    color = MaterialTheme.colorScheme.primary,
                    bubbleColor = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.size(34.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Conversemos.",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontSize = 36.sp,
                    lineHeight = 42.sp,
                    fontWeight = FontWeight.Bold
                )
            )

            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Cuéntanos qué necesitas para dar el próximo paso en tu negocio.",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 20.sp,
                    lineHeight = 28.sp
                )
            )

            Spacer(modifier = Modifier.height(26.dp))
            ContactField(label = "Nombre") {
                AppTextField(
                    value = name,
                    onValueChange = { name = it },
                    placeholder = "Tu nombre"
                )
            }

            Spacer(modifier = Modifier.height(18.dp))
            ContactField(label = "Correo electrónico") {
                AppTextField(
                    value = email,
                    onValueChange = { email = it },
                    placeholder = "tu@correo.com"
                )
            }

            Spacer(modifier = Modifier.height(18.dp))
            ContactField(label = "¿En qué podemos ayudarte?") {
                TopicSelector(
                    selectedTopic = selectedTopic,
                    onTopicSelected = { selectedTopic = it }
                )
            }

            Spacer(modifier = Modifier.height(18.dp))
            ContactField(label = "Tu mensaje") {
                AppTextField(
                    value = message,
                    onValueChange = { message = it },
                    placeholder = "Cuéntanos tu consulta...",
                    modifier = Modifier.height(170.dp)
                )
            }

            Spacer(modifier = Modifier.height(26.dp))
            PrimaryButton(
                text = "Enviar consulta  →",
                onClick = {}
            )

            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = "Envío simulado. No se contactará a la empresa.",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun ContactField(
    label: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = label,
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.labelLarge.copy(
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        content()
    }
}

@Composable
private fun TopicSelector(
    selectedTopic: String,
    onTopicSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .clip(RoundedCornerShape(12.dp))
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.55f),
                    shape = RoundedCornerShape(12.dp)
                )
                .clickable { expanded = true }
                .padding(horizontal = 18.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = selectedTopic,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 19.sp),
                maxLines = 1,
                modifier = Modifier.weight(1f)
            )
            ChevronDownIcon(
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(22.dp)
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            contactTopics.forEach { topic ->
                DropdownMenuItem(
                    text = { Text(topic) },
                    onClick = {
                        onTopicSelected(topic)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun ChevronDownIcon(color: Color, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val stroke = 2.4.dp.toPx()
        drawLine(
            color = color,
            start = Offset(size.width * 0.22f, size.height * 0.36f),
            end = Offset(size.width * 0.5f, size.height * 0.64f),
            strokeWidth = stroke,
            cap = StrokeCap.Round
        )
        drawLine(
            color = color,
            start = Offset(size.width * 0.5f, size.height * 0.64f),
            end = Offset(size.width * 0.78f, size.height * 0.36f),
            strokeWidth = stroke,
            cap = StrokeCap.Round
        )
    }
}

@Composable
private fun ChatGlyph(
    color: Color,
    bubbleColor: Color,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val stroke = 2.dp.toPx()
        val style = Stroke(
            width = stroke,
            cap = StrokeCap.Round,
            join = StrokeJoin.Round
        )
        val w = size.width
        val h = size.height
        val radius = CornerRadius(5.dp.toPx())

        val backBubble = Path().apply {
            addRoundRect(
                RoundRect(
                    Rect(w * 0.04f, h * 0.06f, w * 0.58f, h * 0.46f),
                    radius
                )
            )
            moveTo(w * 0.16f, h * 0.46f)
            lineTo(w * 0.12f, h * 0.60f)
            lineTo(w * 0.34f, h * 0.46f)
            close()
        }
        drawPath(path = backBubble, color = bubbleColor)
        drawPath(path = backBubble, color = color, style = style)

        val frontBubble = Path().apply {
            addRoundRect(
                RoundRect(
                    Rect(w * 0.38f, h * 0.44f, w * 0.96f, h * 0.84f),
                    radius
                )
            )
            moveTo(w * 0.72f, h * 0.84f)
            lineTo(w * 0.80f, h * 0.98f)
            lineTo(w * 0.54f, h * 0.84f)
            close()
        }
        drawPath(path = frontBubble, color = bubbleColor)
        drawPath(path = frontBubble, color = color, style = style)
    }
}

private val contactTopics = listOf(
    "Consulta sobre productos",
    "Soporte de pedidos",
    "Cursos y capacitaciones",
    "Otra consulta"
)

@Preview(showBackground = true)
@Composable
private fun ContactScreenPreview() {
    ProyectoSemestralTheme {
        ContactScreen(
            initialName = "Andrea Demo",
            initialEmail = "andrea@example.com"
        )
    }
}
