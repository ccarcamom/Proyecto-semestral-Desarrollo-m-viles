package com.example.proyecto_semestral.data.dto.admin

data class CourseCategoryDto(
    val id: Long,
    val nombre: String
)

data class CreateCourseRequestDto(
    val titulo: String,
    val descripcion: String,
    val categoriaId: Long,
    val imagenUrl: String?,
    val objetivos: List<CreateObjectiveRequestDto>
)

data class CreateObjectiveRequestDto(
    val descripcion: String,
    val orden: Int
)

data class CreateLessonRequestDto(
    val titulo: String,
    val descripcion: String?,
    val orden: Int,
    val videoUrl: String,
    val duracionSegundos: Int
)

data class LessonDto(
    val id: Long,
    val titulo: String,
    val descripcion: String?,
    val orden: Int?,
    val videoUrl: String?,
    val duracionSegundos: Int?
)

data class CourseObjectiveDto(
    val id: Long?,
    val descripcion: String?,
    val orden: Int?
)

data class AdminCourseDto(
    val id: Long,
    val titulo: String,
    val descripcion: String?,
    val imagenUrl: String?,
    val categoria: CourseCategoryDto?,
    val objetivos: List<CourseObjectiveDto>?,
    val lecciones: List<LessonDto>?,
    val publicado: Boolean?,
    val duracionTotalSegundos: Long?
)

data class ChangePublicationRequestDto(
    val publicado: Boolean
)
