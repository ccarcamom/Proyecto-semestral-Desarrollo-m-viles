package com.example.proyecto_semestral.data.remote

import com.example.proyecto_semestral.data.dto.admin.AdminCourseDto
import com.example.proyecto_semestral.data.dto.admin.ChangePublicationRequestDto
import com.example.proyecto_semestral.data.dto.admin.ContactQueryDto
import com.example.proyecto_semestral.data.dto.admin.CourseCategoryDto
import com.example.proyecto_semestral.data.dto.admin.CreateCourseRequestDto
import com.example.proyecto_semestral.data.dto.admin.CreateLessonRequestDto
import com.example.proyecto_semestral.data.dto.admin.CreateRecipeRequestDto
import com.example.proyecto_semestral.data.dto.admin.SaveCategoryRequestDto
import com.example.proyecto_semestral.data.dto.admin.UpdateContactStatusRequestDto
import com.example.proyecto_semestral.data.dto.admin.UpdateLessonRequestDto
import com.example.proyecto_semestral.data.dto.admin.UpdateRecipeRequestDto
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
import retrofit2.http.PUT
import retrofit2.http.Path

interface AdminApi {

    @GET("api/v1/admin/recipes")
    suspend fun getRecipes(): List<RecipeSummaryDto>

    @POST("api/v1/admin/recipes")
    suspend fun createRecipe(@Body request: CreateRecipeRequestDto): RecipeDetailDto

    @GET("api/v1/admin/recipes/{id}")
    suspend fun getRecipe(@Path("id") id: Long): RecipeDetailDto

    @PUT("api/v1/admin/recipes/{id}")
    suspend fun updateRecipe(
        @Path("id") id: Long,
        @Body request: UpdateRecipeRequestDto
    ): RecipeDetailDto

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

    @PUT("api/cursos/{id}")
    suspend fun updateCourse(
        @Path("id") id: Long,
        @Body request: CreateCourseRequestDto
    ): AdminCourseDto

    @Multipart
    @POST("api/cursos/{id}/image")
    suspend fun uploadCourseImage(
        @Path("id") id: Long,
        @Part image: MultipartBody.Part
    ): AdminCourseDto

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

    @PUT("api/cursos/{cursoId}/lecciones/{id}")
    suspend fun updateLesson(
        @Path("cursoId") courseId: Long,
        @Path("id") lessonId: Long,
        @Body request: UpdateLessonRequestDto
    ): LessonDto

    @DELETE("api/cursos/{cursoId}/lecciones/{id}")
    suspend fun deleteLesson(
        @Path("cursoId") courseId: Long,
        @Path("id") lessonId: Long
    )

    @GET("api/categorias")
    suspend fun getCourseCategories(): List<CourseCategoryDto>

    @POST("api/categorias")
    suspend fun createCourseCategory(@Body request: SaveCategoryRequestDto): CourseCategoryDto

    @PUT("api/categorias/{id}")
    suspend fun updateCourseCategory(
        @Path("id") id: Long,
        @Body request: SaveCategoryRequestDto
    ): CourseCategoryDto

    @DELETE("api/categorias/{id}")
    suspend fun deleteCourseCategory(@Path("id") id: Long)

    @GET("api/consultas")
    suspend fun getContactQueries(): List<ContactQueryDto>

    @PATCH("api/consultas/{id}/estado")
    suspend fun updateContactQueryStatus(
        @Path("id") id: Long,
        @Body request: UpdateContactStatusRequestDto
    ): ContactQueryDto

    @DELETE("api/consultas/{id}")
    suspend fun deleteContactQuery(@Path("id") id: Long)
}
