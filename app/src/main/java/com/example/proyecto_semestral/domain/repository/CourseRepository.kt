package com.example.proyecto_semestral.domain.repository

import com.example.proyecto_semestral.model.course.CoursePage
import com.example.proyecto_semestral.model.course.CourseDetail

interface CourseRepository {
    suspend fun getCourses(
        query: String? = null,
        categoryId: Long? = null,
        page: Int = 0,
        size: Int = 12
    ): CoursePage

    suspend fun getCourse(id: Long): CourseDetail
}

class CourseRepositoryException(
    message: String,
    cause: Throwable? = null
) : Exception(message, cause)
