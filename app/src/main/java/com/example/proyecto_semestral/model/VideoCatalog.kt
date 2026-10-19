package com.example.proyecto_semestral.model

import com.example.proyecto_semestral.R

object VideoCatalog {
    private val videos = listOf(
        VideoContent(
            id = "video-templado-chocolate",
            category = "Chocolatería",
            title = "Introducción al templado de chocolate",
            duration = "8:00",
            durationSeconds = 480,
            description = "Conoce las etapas del templado y aprende a reconocer un acabado brillante y uniforme.",
            imageRes = R.drawable.chocolate
        ),
        VideoContent(
            id = "leccion-templado-1",
            category = "Chocolatería",
            title = "Conoce el chocolate",
            duration = "8:00",
            durationSeconds = 480,
            description = "Conoce las etapas del templado y aprende a reconocer un acabado brillante y uniforme.",
            imageRes = R.drawable.chocolate,
            courseName = "Fundamentos de chocolatería",
            lessonPosition = 1,
            totalLessons = 3
        ),
        VideoContent(
            id = "leccion-templado-2",
            category = "Chocolatería",
            title = "Técnica de templado",
            duration = "12:00",
            durationSeconds = 720,
            description = "Aprende a controlar las temperaturas y movimientos necesarios para templar correctamente.",
            imageRes = R.drawable.chocolate,
            courseName = "Fundamentos de chocolatería",
            lessonPosition = 2,
            totalLessons = 3
        ),
        VideoContent(
            id = "leccion-templado-3",
            category = "Chocolatería",
            title = "Acabado y conservación",
            duration = "10:00",
            durationSeconds = 600,
            description = "Aplica la técnica, revisa el acabado final y conserva correctamente tus preparaciones.",
            imageRes = R.drawable.chocolate,
            courseName = "Fundamentos de chocolatería",
            lessonPosition = 3,
            totalLessons = 3
        ),
        VideoContent(
            id = "leccion-bolleria-1",
            category = "Bollería",
            title = "Bases de la bollería",
            duration = "10:00",
            durationSeconds = 600,
            description = "Conoce los ingredientes y procesos fundamentales de una masa de bollería.",
            imageRes = R.drawable.bolleria,
            courseName = "Introducción a la bollería",
            lessonPosition = 1,
            totalLessons = 3
        ),
        VideoContent(
            id = "leccion-bolleria-2",
            category = "Bollería",
            title = "Técnica de laminado",
            duration = "12:00",
            durationSeconds = 720,
            description = "Aprende a formar capas uniformes mediante una técnica básica de laminado.",
            imageRes = R.drawable.bolleria,
            courseName = "Introducción a la bollería",
            lessonPosition = 2,
            totalLessons = 3
        ),
        VideoContent(
            id = "leccion-bolleria-3",
            category = "Bollería",
            title = "Fermentación y horneado",
            duration = "13:00",
            durationSeconds = 780,
            description = "Controla el reposo, la fermentación y el horneado para conseguir un acabado uniforme.",
            imageRes = R.drawable.bolleria,
            courseName = "Introducción a la bollería",
            lessonPosition = 3,
            totalLessons = 3
        )
    )

    fun findById(id: String): VideoContent? = videos.firstOrNull { it.id == id }
}
