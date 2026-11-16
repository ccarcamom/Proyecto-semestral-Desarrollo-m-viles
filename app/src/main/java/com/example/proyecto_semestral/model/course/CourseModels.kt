package com.example.proyecto_semestral.model.course

data class CourseSummary(
    val id: Long,
    val title: String,
    val description: String,
    val imageUrl: String?,
    val categoryName: String,
    val lessonCount: Int,
    val durationSeconds: Long
)

data class CoursePage(
    val courses: List<CourseSummary>,
    val page: Int,
    val totalPages: Int
)

data class CourseLesson(
    val id: Long,
    val title: String,
    val description: String,
    val order: Int,
    val videoUrl: String,
    val durationSeconds: Int
)

data class CourseDetail(
    val id: Long,
    val title: String,
    val description: String,
    val imageUrl: String?,
    val categoryName: String,
    val objectives: List<String>,
    val lessons: List<CourseLesson>,
    val durationSeconds: Long
)
