package com.example.proyecto_semestral.ui.screens.video

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyecto_semestral.model.content.VideoCatalog
import com.example.proyecto_semestral.model.content.VideoContent
import com.example.proyecto_semestral.ui.components.AppTopBar
import com.example.proyecto_semestral.ui.components.PrimaryButton
import com.example.proyecto_semestral.ui.components.VideoPlayerPlaceholder
import com.example.proyecto_semestral.ui.theme.ProyectoSemestralTheme

@Composable
fun VideoScreen(
    videoId: String,
    onBack: () -> Unit,
    onNextLesson: () -> Unit,
    onReturnToCourse: () -> Unit,
    modifier: Modifier = Modifier
) {
    val video = VideoCatalog.findById(videoId)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        AppTopBar(
            title = "Video técnico",
            onBackClick = onBack
        )

        if (video == null) {
            MissingVideoState(
                onBack = onBack,
                modifier = Modifier.weight(1f)
            )
        } else {
            VideoContent(
                video = video,
                onNextLesson = onNextLesson,
                onReturnToCourse = onReturnToCourse,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun VideoContent(
    video: VideoContent,
    onNextLesson: () -> Unit,
    onReturnToCourse: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 20.dp)
    ) {
        VideoPlayerPlaceholder(
            videoId = video.id,
            imageRes = video.imageRes,
            duration = video.duration
        )

        Spacer(modifier = Modifier.height(42.dp))
        Text(
            text = video.category.uppercase(),
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.8.sp
            )
        )
        Spacer(modifier = Modifier.height(14.dp))
        Text(
            text = video.title,
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.headlineMedium.copy(
                fontSize = 38.sp,
                lineHeight = 44.sp,
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(modifier = Modifier.height(14.dp))
        Text(
            text = video.description,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontSize = 22.sp,
                lineHeight = 34.sp
            )
        )

        video.courseName?.let { courseName ->
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "$courseName · Lección ${video.lessonPosition} de ${video.totalLessons}",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
        }

        Spacer(modifier = Modifier.height(28.dp))
        DemoNotice()

        if (video.isLesson) {
            Spacer(modifier = Modifier.height(28.dp))
            if (video.isLastLesson) {
                PrimaryButton(
                    text = "Volver al curso",
                    onClick = onReturnToCourse
                )
            } else {
                PrimaryButton(
                    text = "Siguiente lección",
                    onClick = onNextLesson
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))
        Text(
            text = "Lleva la técnica a tu negocio",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.headlineMedium.copy(
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Observa los pasos, revisa los ingredientes y vuelve a consultar el contenido cuando lo necesites.",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontSize = 21.sp,
                lineHeight = 32.sp
            )
        )
    }
}

@Composable
private fun DemoNotice() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = RoundedCornerShape(22.dp)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Text(
                text = "Vista previa del reproductor",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Los controles simulan la reproducción. El video técnico se incorporará cuando esté disponible el material autorizado.",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 19.sp, lineHeight = 29.sp)
            )
        }
    }
}

@Composable
private fun MissingVideoState(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "No encontramos este video.",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "El contenido puede haber sido movido o ya no estar disponible.",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onBack) {
            Text(text = "Volver")
        }
    }
}

@Preview(showBackground = true, widthDp = 400, heightDp = 900)
@Composable
private fun VideoScreenPreview() {
    ProyectoSemestralTheme {
        VideoScreen(
            videoId = "video-templado-chocolate",
            onBack = {},
            onNextLesson = {},
            onReturnToCourse = {}
        )
    }
}

@Preview(showBackground = true, widthDp = 400, heightDp = 900)
@Composable
private fun VideoLessonScreenPreview() {
    ProyectoSemestralTheme {
        VideoScreen(
            videoId = "leccion-templado-3",
            onBack = {},
            onNextLesson = {},
            onReturnToCourse = {}
        )
    }
}
