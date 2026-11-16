package com.example.proyecto_semestral.data.remote

import com.example.proyecto_semestral.data.dto.course.CourseSummaryDto
import com.example.proyecto_semestral.data.dto.course.CourseDetailDto
import com.example.proyecto_semestral.data.dto.recipe.PageResponseDto
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Path

interface CourseApi {
    @GET("api/cursos")
    suspend fun getCourses(
        @Query("q") query: String? = null,
        @Query("categoriaId") categoryId: Long? = null,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 12
    ): PageResponseDto<CourseSummaryDto>

    @GET("api/cursos/{id}")
    suspend fun getCourse(@Path("id") id: Long): CourseDetailDto
}
