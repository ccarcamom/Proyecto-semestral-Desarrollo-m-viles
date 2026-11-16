package com.example.proyecto_semestral.ui.screens.course

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.proyecto_semestral.model.course.CourseDetail
import com.example.proyecto_semestral.ui.components.AppTopBar
import com.example.proyecto_semestral.ui.components.EmptyState
import com.example.proyecto_semestral.ui.components.LessonItem
import com.example.proyecto_semestral.ui.components.PrimaryButton
import com.example.proyecto_semestral.ui.viewmodel.course.CourseDetailUiState
import com.example.proyecto_semestral.ui.viewmodel.course.CourseDetailViewModel

@Composable
fun CourseDetailScreen(
    viewModelFactory: ViewModelProvider.Factory,
    onBack: () -> Unit,
    onLessonClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel: CourseDetailViewModel = viewModel(factory = viewModelFactory)
    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        AppTopBar(title = "Detalle del curso", onBackClick = onBack)

        when (val current = state) {
            CourseDetailUiState.Loading -> Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }

            is CourseDetailUiState.Error -> EmptyState(
                title = "Curso no disponible",
                description = current.message,
                buttonText = "Reintentar",
                onButtonClick = viewModel::load,
                modifier = Modifier
                    .weight(1f)
                    .padding(24.dp)
            )

            is CourseDetailUiState.Success -> CourseContent(
                course = current.course,
                onLessonClick = onLessonClick,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun CourseContent(
    course: CourseDetail,
    onLessonClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        AsyncImage(
            model = course.imageUrl,
            contentDescription = course.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(230.dp)
                .clip(RoundedCornerShape(22.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "CURSO  ·  ${course.categoryName.uppercase()}",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            )
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = course.title,
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.headlineLarge.copy(
                fontSize = 38.sp,
                lineHeight = 43.sp,
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "${formatDuration(course.durationSeconds)} · ${course.lessons.size} lecciones",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyLarge.copy(fontSize = 19.sp)
        )
        Spacer(modifier = Modifier.height(18.dp))
        Text(
            text = course.description,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyLarge.copy(fontSize = 20.sp, lineHeight = 29.sp)
        )

        if (course.objectives.isNotEmpty()) {
            Spacer(modifier = Modifier.height(28.dp))
            SectionTitle("Lo que aprenderás")
            Spacer(modifier = Modifier.height(16.dp))
            course.objectives.forEach { objective ->
                LearningGoal(objective)
                Spacer(modifier = Modifier.height(12.dp))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        PrimaryButton(
            text = if (course.lessons.isEmpty()) "Sin lecciones disponibles" else "▶   Comenzar curso",
            onClick = { course.lessons.firstOrNull()?.let { onLessonClick(it.id) } },
            enabled = course.lessons.isNotEmpty(),
            modifier = Modifier.height(64.dp)
        )

        Spacer(modifier = Modifier.height(30.dp))
        SectionTitle("Contenido del curso")
        Spacer(modifier = Modifier.height(14.dp))
        if (course.lessons.isEmpty()) {
            Text(
                text = "Este curso todavía no tiene lecciones.",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            course.lessons.forEachIndexed { index, lesson ->
                LessonItem(
                    lessonNumber = index + 1,
                    title = lesson.title,
                    duration = formatDuration(lesson.durationSeconds.toLong()),
                    onClick = { onLessonClick(lesson.id) },
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .padding(horizontal = 14.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
        Spacer(modifier = Modifier.height(28.dp))
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        color = MaterialTheme.colorScheme.primary,
        style = MaterialTheme.typography.headlineMedium.copy(
            fontSize = 29.sp,
            fontWeight = FontWeight.Bold
        )
    )
}

@Composable
private fun LearningGoal(text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text("✓", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = text,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.bodyLarge.copy(fontSize = 19.sp)
        )
    }
}

private fun formatDuration(seconds: Long): String {
    if (seconds <= 0) return "0 min"
    val minutes = seconds / 60
    return if (minutes < 60) "$minutes min" else "${minutes / 60} h ${minutes % 60} min"
}
