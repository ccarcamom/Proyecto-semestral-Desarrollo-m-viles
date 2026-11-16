package com.example.proyecto_semestral.data.repository

import com.example.proyecto_semestral.BuildConfig
import com.example.proyecto_semestral.data.dto.course.CourseSummaryDto
import com.example.proyecto_semestral.data.dto.course.CourseDetailDto
import com.example.proyecto_semestral.data.remote.CourseApi
import com.example.proyecto_semestral.domain.repository.CourseRepository
import com.example.proyecto_semestral.domain.repository.CourseRepositoryException
import com.example.proyecto_semestral.model.course.CoursePage
import com.example.proyecto_semestral.model.course.CourseSummary
import com.example.proyecto_semestral.model.course.CourseDetail
import com.example.proyecto_semestral.model.course.CourseLesson
import retrofit2.HttpException
import java.io.IOException

class CourseRepositoryImpl(
    private val api: CourseApi,
    private val gatewayBaseUrl: String = BuildConfig.API_BASE_URL
) : CourseRepository {

    override suspend fun getCourses(
        query: String?,
        categoryId: Long?,
        page: Int,
        size: Int
    ): CoursePage = try {
        val response = api.getCourses(query, categoryId, page, size)
        CoursePage(
            courses = response.content.map { it.toModel() },
            page = response.number,
            totalPages = response.totalPages
        )
    } catch (exception: HttpException) {
        throw CourseRepositoryException(
            if (exception.code() >= 500) {
                "El servidor de cursos no está disponible."
            } else {
                "No se pudieron cargar los cursos."
            },
            exception
        )
    } catch (exception: IOException) {
        throw CourseRepositoryException(
            "No se pudo conectar con el servidor de cursos.",
            exception
        )
    }

    override suspend fun getCourse(id: Long): CourseDetail = try {
        api.getCourse(id).toModel()
    } catch (exception: HttpException) {
        throw CourseRepositoryException(
            if (exception.code() == 404) "El curso ya no está disponible."
            else "No se pudo cargar el curso.",
            exception
        )
    } catch (exception: IOException) {
        throw CourseRepositoryException(
            "No se pudo conectar con el servidor de cursos.",
            exception
        )
    }

    private fun CourseSummaryDto.toModel() = CourseSummary(
        id = id,
        title = titulo,
        description = descripcion.orEmpty(),
        imageUrl = imagenUrl.toGatewayUrl(),
        categoryName = categoria?.nombre.orEmpty(),
        lessonCount = cantidadLecciones ?: 0,
        durationSeconds = duracionTotalSegundos ?: 0L
    )

    private fun CourseDetailDto.toModel() = CourseDetail(
        id = id,
        title = titulo,
        description = descripcion.orEmpty(),
        imageUrl = imagenUrl.toGatewayUrl(),
        categoryName = categoria?.nombre.orEmpty(),
        objectives = objetivos.orEmpty()
            .sortedBy { it.orden ?: Int.MAX_VALUE }
            .mapNotNull { it.descripcion?.takeIf(String::isNotBlank) },
        lessons = lecciones.orEmpty()
            .sortedBy { it.orden ?: Int.MAX_VALUE }
            .map { lesson ->
                CourseLesson(
                    id = lesson.id,
                    title = lesson.titulo,
                    description = lesson.descripcion.orEmpty(),
                    order = lesson.orden ?: 0,
                    videoUrl = lesson.videoUrl.orEmpty(),
                    durationSeconds = lesson.duracionSegundos ?: 0
                )
            },
        durationSeconds = duracionTotalSegundos ?: 0L
    )

    private fun String?.toGatewayUrl(): String? {
        val value = this?.trim()?.takeIf(String::isNotEmpty) ?: return null
        if (value.startsWith("http://") || value.startsWith("https://")) return value
        return gatewayBaseUrl.trimEnd('/') + "/" + value.trimStart('/')
    }
}
