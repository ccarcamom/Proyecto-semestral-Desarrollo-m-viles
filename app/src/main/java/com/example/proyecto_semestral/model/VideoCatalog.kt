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
            title = "Introducción al templado de chocolate",
            duration = "8:00",
            durationSeconds = 480,
            description = "Conoce las etapas del templado y aprende a reconocer un acabado brillante y uniforme.",
            imageRes = R.drawable.chocolate,
            courseName = "Fundamentos de chocolatería",
            lessonPosition = 1,
            totalLessons = 3
        ),
        VideoContent(
            id = "leccion-templado-3",
            category = "Chocolatería",
            title = "Acabado y conservación",
            duration = "6:30",
            durationSeconds = 390,
            description = "Aplica la técnica, revisa el acabado final y conserva correctamente tus preparaciones.",
            imageRes = R.drawable.chocolate,
            courseName = "Fundamentos de chocolatería",
            lessonPosition = 3,
            totalLessons = 3
        )
    )

    fun findById(id: String): VideoContent? = videos.firstOrNull { it.id == id }
}
