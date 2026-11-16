package com.example.proyecto_semestral.data.dto.course

data class CourseCategoryDto(
    val id: Long,
    val nombre: String
)

data class CourseSummaryDto(
    val id: Long,
    val titulo: String,
    val descripcion: String?,
    val imagenUrl: String?,
    val categoria: CourseCategoryDto?,
    val cantidadLecciones: Int?,
    val duracionTotalSegundos: Long?
)

data class CourseObjectiveDto(
    val id: Long,
    val descripcion: String?,
    val orden: Int?
)

data class CourseLessonDto(
    val id: Long,
    val titulo: String,
    val descripcion: String?,
    val orden: Int?,
    val videoUrl: String?,
    val duracionSegundos: Int?
)

data class CourseDetailDto(
    val id: Long,
    val titulo: String,
    val descripcion: String?,
    val imagenUrl: String?,
    val categoria: CourseCategoryDto?,
    val objetivos: List<CourseObjectiveDto>?,
    val lecciones: List<CourseLessonDto>?,
    val publicado: Boolean?,
    val duracionTotalSegundos: Long?
)
