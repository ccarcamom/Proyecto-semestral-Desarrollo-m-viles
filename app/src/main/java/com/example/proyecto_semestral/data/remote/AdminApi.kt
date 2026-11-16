package com.example.proyecto_semestral.data.remote

import com.example.proyecto_semestral.data.dto.admin.AdminCourseDto
import com.example.proyecto_semestral.data.dto.admin.ChangePublicationRequestDto
import com.example.proyecto_semestral.data.dto.admin.CourseCategoryDto
import com.example.proyecto_semestral.data.dto.admin.CreateCourseRequestDto
import com.example.proyecto_semestral.data.dto.admin.CreateLessonRequestDto
import com.example.proyecto_semestral.data.dto.admin.CreateRecipeRequestDto
import com.example.proyecto_semestral.data.dto.admin.LessonDto
import com.example.proyecto_semestral.data.dto.recipe.RecipeCategoryDto
import com.example.proyecto_semestral.data.dto.recipe.RecipeDetailDto
import com.example.proyecto_semestral.data.dto.recipe.RecipeSummaryDto
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Multipart
import retrofit2.http.Part
import retrofit2.http.POST
import retrofit2.http.Path

interface AdminApi {

    @GET("api/v1/admin/recipes")
    suspend fun getRecipes(): List<RecipeSummaryDto>

    @POST("api/v1/admin/recipes")
    suspend fun createRecipe(@Body request: CreateRecipeRequestDto): RecipeDetailDto

    @Multipart
    @POST("api/v1/admin/recipes/{id}/image")
    suspend fun uploadRecipeImage(
        @Path("id") id: Long,
        @Part image: MultipartBody.Part
    ): RecipeDetailDto

    @DELETE("api/v1/admin/recipes/{id}")
    suspend fun deleteRecipe(@Path("id") id: Long)

    @GET("api/v1/recipes/categorias")
    suspend fun getRecipeCategories(): List<RecipeCategoryDto>

    @GET("api/v1/admin/cursos")
    suspend fun getCourses(): List<AdminCourseDto>

    @POST("api/cursos")
    suspend fun createCourse(@Body request: CreateCourseRequestDto): AdminCourseDto

    @PATCH("api/cursos/{id}/publicacion")
    suspend fun changeCoursePublication(
        @Path("id") id: Long,
        @Body request: ChangePublicationRequestDto
    ): AdminCourseDto

    @DELETE("api/cursos/{id}")
    suspend fun deleteCourse(@Path("id") id: Long)

    @POST("api/cursos/{cursoId}/lecciones")
    suspend fun createLesson(
        @Path("cursoId") courseId: Long,
        @Body request: CreateLessonRequestDto
    ): LessonDto

    @GET("api/categorias")
    suspend fun getCourseCategories(): List<CourseCategoryDto>
}
