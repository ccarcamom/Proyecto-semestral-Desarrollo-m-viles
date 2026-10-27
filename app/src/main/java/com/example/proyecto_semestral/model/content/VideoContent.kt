package com.example.proyecto_semestral.model.content

import androidx.annotation.DrawableRes

data class VideoContent(
    val id: String,
    val category: String,
    val title: String,
    val duration: String,
    val durationSeconds: Int,
    val description: String,
    @DrawableRes val imageRes: Int,
    val courseName: String? = null,
    val lessonPosition: Int? = null,
    val totalLessons: Int? = null
) {
    val isLesson: Boolean
        get() = courseName != null && lessonPosition != null && totalLessons != null

    val isLastLesson: Boolean
        get() = isLesson && lessonPosition == totalLessons
}
