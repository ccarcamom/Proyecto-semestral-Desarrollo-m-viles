package com.example.proyecto_semestral.model.content

import com.example.proyecto_semestral.R

object VideoCatalog {
    private val videos = listOf(
        VideoContent(
            id = "leccion-templado-1",
            videoUrl = "https://www.youtube.com/watch?v=K58-sTjitmU",
            category = "Chocolatería",
            title = "Conoce el chocolate",
            duration = "1:39",
            durationSeconds = 99,
            description = "Conoce las etapas del templado y aprende a reconocer un acabado brillante y uniforme.",
            imageRes = R.drawable.chocolate,
            courseName = "Fundamentos de chocolatería",
            lessonPosition = 1,
            totalLessons = 3
        ),
        VideoContent(
            id = "leccion-templado-2",
            videoUrl = "https://www.youtube.com/watch?v=0w0gw2JV1Lg",
            category = "Chocolatería",
            title = "Técnica de templado",
            duration = "4:00",
            durationSeconds = 240,
            description = "Aprende a controlar las temperaturas y movimientos necesarios para templar correctamente.",
            imageRes = R.drawable.chocolate,
            courseName = "Fundamentos de chocolatería",
            lessonPosition = 2,
            totalLessons = 3
        ),
        VideoContent(
            id = "leccion-templado-3",
            videoUrl = "https://www.youtube.com/watch?v=Rj0-J-4t5E0",
            category = "Chocolatería",
            title = "Acabado y conservación",
            duration = "0:54",
            durationSeconds = 54,
            description = "Aplica la técnica, revisa el acabado final y conserva correctamente tus preparaciones.",
            imageRes = R.drawable.chocolate,
            courseName = "Fundamentos de chocolatería",
            lessonPosition = 3,
            totalLessons = 3
        ),
        VideoContent(
            id = "leccion-bolleria-1",
            videoUrl = "https://www.youtube.com/watch?v=_s2F-6HAzw4",
            category = "Bollería",
            title = "Bases de la bollería",
            duration = "2:32",
            durationSeconds = 152,
            description = "Conoce los ingredientes y procesos fundamentales de una masa de bollería.",
            imageRes = R.drawable.bolleria,
            courseName = "Introducción a la bollería",
            lessonPosition = 1,
            totalLessons = 3
        ),
        VideoContent(
            id = "leccion-bolleria-2",
            videoUrl = "https://www.youtube.com/watch?v=E9HUrKEvocY",
            category = "Bollería",
            title = "Técnica de laminado",
            duration = "1:34",
            durationSeconds = 94,
            description = "Aprende a formar capas uniformes mediante una técnica básica de laminado.",
            imageRes = R.drawable.bolleria,
            courseName = "Introducción a la bollería",
            lessonPosition = 2,
            totalLessons = 3
        ),
        VideoContent(
            id = "leccion-bolleria-3",
            videoUrl = "https://www.youtube.com/watch?v=QFTbZTv3wjo",
            category = "Bollería",
            title = "Fermentación y horneado",
            duration = "5:57",
            durationSeconds = 357,
            description = "Controla el reposo, la fermentación y el horneado para conseguir un acabado uniforme.",
            imageRes = R.drawable.bolleria,
            courseName = "Introducción a la bollería",
            lessonPosition = 3,
            totalLessons = 3
        )
    )

    fun findById(id: String): VideoContent? = videos.firstOrNull { it.id == id }

    fun nextLesson(id: String): VideoContent? {
        val current = findById(id) ?: return null
        val position = current.lessonPosition ?: return null
        return videos.firstOrNull {
            it.courseName == current.courseName && it.lessonPosition == position + 1
        }
    }
}
