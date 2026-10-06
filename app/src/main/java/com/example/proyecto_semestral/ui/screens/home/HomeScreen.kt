package com.example.proyecto_semestral.ui.screens.home

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyecto_semestral.R
import com.example.proyecto_semestral.ui.components.AppBottomBar
import com.example.proyecto_semestral.ui.components.BottomBarItem
import com.example.proyecto_semestral.ui.components.BrandLogo
import com.example.proyecto_semestral.ui.components.ContentCard
import com.example.proyecto_semestral.ui.components.FeaturedContentCard
import com.example.proyecto_semestral.ui.theme.ProyectoSemestralTheme

@Composable
fun HomeScreen(
    onLogout: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var selectedBottomItem by rememberSaveable { mutableStateOf(BottomBarItem.Home) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            AppBottomBar(
                selectedItem = selectedBottomItem,
                onItemSelected = { selectedBottomItem = it }
            )
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
            BrandLogo()

            Spacer(modifier = Modifier.height(26.dp))
            Text(
                text = "UN POCO DE INSPIRACIÓN, CADA DÍA",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.5.sp
                )
            )

            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = "Hola, Andrea.",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontSize = 38.sp,
                    lineHeight = 44.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "¿Qué te gustaría crear hoy?",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 22.sp)
            )

            Spacer(modifier = Modifier.height(24.dp))
            HomeSearchField()

            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                HomeShortcut("Recetas", ShortcutIcon.Book, Modifier.weight(1f))
                HomeShortcut("Videos", ShortcutIcon.Play, Modifier.weight(1f))
                HomeShortcut("Cursos", ShortcutIcon.Course, Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(28.dp))
            Text(
                text = "Elegido para ti",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.height(14.dp))
            FeaturedContentCard(
                category = "Recetas / Galletas",
                title = "Galletas con chips de chocolate",
                duration = "35 min",
                image = painterResource(R.drawable.ic_launcher_background),
                onClick = { selectedBottomItem = BottomBarItem.Explore }
            )

            Spacer(modifier = Modifier.height(40.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Aprende algo nuevo",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    text = "Ver cursos",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.clickable { selectedBottomItem = BottomBarItem.Explore }
                )
            }

            Spacer(modifier = Modifier.height(18.dp))
            ContentCard(
                eyebrow = "Curso",
                title = "Fundamentos de chocolatería",
                metadata = "Chocolatería · 30 min",
                image = painterResource(R.drawable.ic_launcher_background),
                onClick = { selectedBottomItem = BottomBarItem.Explore }
            )
            Spacer(modifier = Modifier.height(14.dp))
            ContentCard(
                eyebrow = "Curso",
                title = "Introducción a la bollería",
                metadata = "Bollería · 35 min",
                image = painterResource(R.drawable.ic_launcher_background),
                onClick = { selectedBottomItem = BottomBarItem.Explore }
            )
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun HomeSearchField() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(58.dp)
            .background(
                color = MaterialTheme.colorScheme.background,
                shape = RoundedCornerShape(18.dp)
            )
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(18.dp)
            )
            .clickable { }
            .padding(horizontal = 18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SearchGlyph(
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(14.dp))
        Text(
            text = "Busca una receta o una técnica",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp)
        )
    }
}

@Composable
private fun HomeShortcut(
    label: String,
    icon: ShortcutIcon,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .height(100.dp)
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(22.dp)
            )
            .clickable { }
            .padding(vertical = 14.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        ShortcutGlyph(
            icon = icon,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = label,
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
        )
    }
}

private enum class ShortcutIcon { Book, Play, Course }

@Composable
private fun ShortcutGlyph(icon: ShortcutIcon, color: Color, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val stroke = 2.dp.toPx()
        when (icon) {
            ShortcutIcon.Book -> {
                drawRoundRect(color, Offset(size.width * .12f, size.height * .18f), androidx.compose.ui.geometry.Size(size.width * .76f, size.height * .68f), style = Stroke(stroke))
                drawLine(color, Offset(size.width * .5f, size.height * .2f), Offset(size.width * .5f, size.height * .84f), stroke)
            }
            ShortcutIcon.Play -> {
                drawCircle(color, size.minDimension * .38f, style = Stroke(stroke))
                val path = androidx.compose.ui.graphics.Path().apply {
                    moveTo(size.width * .44f, size.height * .3f)
                    lineTo(size.width * .68f, size.height * .5f)
                    lineTo(size.width * .44f, size.height * .7f)
                    close()
                }
                drawPath(path, color)
            }
            ShortcutIcon.Course -> {
                val path = androidx.compose.ui.graphics.Path().apply {
                    moveTo(size.width * .08f, size.height * .35f)
                    lineTo(size.width * .5f, size.height * .15f)
                    lineTo(size.width * .92f, size.height * .35f)
                    lineTo(size.width * .5f, size.height * .55f)
                    close()
                }
                drawPath(path, color, style = Stroke(stroke))
                drawLine(color, Offset(size.width * .25f, size.height * .47f), Offset(size.width * .25f, size.height * .72f), stroke)
            }
        }
    }
}

@Composable
private fun SearchGlyph(color: Color, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val stroke = 2.dp.toPx()
        drawCircle(color, size.minDimension * .32f, Offset(size.width * .42f, size.height * .42f), style = Stroke(stroke))
        drawLine(color, Offset(size.width * .64f, size.height * .64f), Offset(size.width * .9f, size.height * .9f), stroke, cap = StrokeCap.Round)
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    ProyectoSemestralTheme {
        HomeScreen()
    }
}
