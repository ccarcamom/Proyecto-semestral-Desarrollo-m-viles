package com.example.proyecto_semestral.ui.screens.course

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyecto_semestral.R
import com.example.proyecto_semestral.ui.components.AppTopBar
import com.example.proyecto_semestral.ui.components.EmptyState
import com.example.proyecto_semestral.ui.components.LessonItem
import com.example.proyecto_semestral.ui.components.PrimaryButton
import com.example.proyecto_semestral.ui.theme.ProyectoSemestralTheme

@Composable
fun CourseDetailScreen(
    courseId: String,
    onBack: () -> Unit,
    onLessonClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val course = courses.firstOrNull { it.id == courseId }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        AppTopBar(
            title = "Detalle del curso",
            onBackClick = onBack
        )

        if (course == null) {
            EmptyState(
                title = "Curso no disponible",
                description = "No encontramos la información de este curso.",
                buttonText = "Volver",
                onButtonClick = onBack,
                modifier = Modifier
                    .weight(1f)
                    .padding(24.dp)
            )
        } else {
            CourseContent(
                course = course,
                onLessonClick = onLessonClick,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun CourseContent(
    course: CourseDetail,
    onLessonClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        Image(
            painter = painterResource(course.imageRes),
            contentDescription = course.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(230.dp)
                .clip(RoundedCornerShape(22.dp))
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "CURSO  ·  ${course.category.uppercase()}",
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
            text = "◷  ${course.duration}  ·  ${course.lessons.size} lecciones",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyLarge.copy(fontSize = 19.sp)
        )

        Spacer(modifier = Modifier.height(18.dp))

        Text(
            text = course.description,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontSize = 20.sp,
                lineHeight = 29.sp
            )
        )

        Spacer(modifier = Modifier.height(28.dp))
        SectionTitle(text = "Lo que aprenderás")
        Spacer(modifier = Modifier.height(16.dp))

        course.learningGoals.forEach { goal ->
            LearningGoal(text = goal)
            Spacer(modifier = Modifier.height(12.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        PrimaryButton(
            text = "▶   Comenzar curso",
            onClick = { onLessonClick(course.lessons.first().videoId) },
            modifier = Modifier.height(64.dp)
        )

        Spacer(modifier = Modifier.height(30.dp))
        SectionTitle(text = "Contenido del curso")
        Spacer(modifier = Modifier.height(14.dp))

        course.lessons.forEach { lesson ->
            LessonItem(
                lessonNumber = lesson.number,
                title = lesson.title,
                duration = lesson.duration,
                onClick = { onLessonClick(lesson.videoId) },
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(horizontal = 14.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        Text(
            text = "Contenido de demostración. Reproducción simulada.",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

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
            Text(
                text = "✓",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = text,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.bodyLarge.copy(fontSize = 19.sp)
        )
    }
}

private data class CourseDetail(
    val id: String,
    val category: String,
    val title: String,
    val duration: String,
    val description: String,
    val learningGoals: List<String>,
    @DrawableRes val imageRes: Int,
    val lessons: List<CourseLesson>
)

private data class CourseLesson(
    val number: Int,
    val title: String,
    val duration: String,
    val videoId: String
)

private val courses = listOf(
    CourseDetail(
        id = "curso-chocolateria",
        category = "Chocolatería",
        title = "Fundamentos de chocolatería",
        duration = "30 min",
        description = "Aprende las bases para trabajar el chocolate con confianza.",
        learningGoals = listOf(
            "Reconocer tipos de chocolate",
            "Controlar el templado",
            "Conservar tus preparaciones"
        ),
        imageRes = R.drawable.chocolate,
        lessons = listOf(
            CourseLesson(1, "Conoce el chocolate", "8 min", "leccion-templado-1"),
            CourseLesson(2, "Técnica de templado", "12 min", "leccion-templado-2"),
            CourseLesson(3, "Acabado y conservación", "10 min", "leccion-templado-3")
        )
    ),
    CourseDetail(
        id = "curso-bolleria",
        category = "Bollería",
        title = "Introducción a la bollería",
        duration = "35 min",
        description = "Conoce las técnicas esenciales para preparar masas laminadas y piezas de bollería.",
        learningGoals = listOf(
            "Preparar una masa base",
            "Aplicar la técnica de laminado",
            "Controlar fermentación y horneado"
        ),
        imageRes = R.drawable.bolleria,
        lessons = listOf(
            CourseLesson(1, "Bases de la bollería", "10 min", "leccion-bolleria-1"),
            CourseLesson(2, "Técnica de laminado", "12 min", "leccion-bolleria-2"),
            CourseLesson(3, "Fermentación y horneado", "13 min", "leccion-bolleria-3")
        )
    )
)

@Preview(showBackground = true, widthDp = 430, heightDp = 900)
@Composable
private fun CourseDetailScreenPreview() {
    ProyectoSemestralTheme {
        CourseDetailScreen(
            courseId = "curso-chocolateria",
            onBack = {},
            onLessonClick = {}
        )
    }
}
