package com.example.proyecto_semestral.domain.repository

import com.example.proyecto_semestral.model.admin.AdminCategory
import com.example.proyecto_semestral.model.admin.AdminCourse
import com.example.proyecto_semestral.model.admin.AdminCourseDraft
import com.example.proyecto_semestral.model.admin.AdminLessonDraft
import com.example.proyecto_semestral.model.admin.AdminRecipe
import com.example.proyecto_semestral.model.admin.AdminRecipeDraft

interface AdminRepository {
    suspend fun getRecipes(): List<AdminRecipe>
    suspend fun createRecipe(draft: AdminRecipeDraft): AdminRecipe
    suspend fun deleteRecipe(id: Long)
    suspend fun getRecipeCategories(): List<AdminCategory>

    suspend fun getCourses(): List<AdminCourse>
    suspend fun createCourse(draft: AdminCourseDraft): AdminCourse
    suspend fun changeCoursePublication(id: Long, published: Boolean): AdminCourse
    suspend fun deleteCourse(id: Long)
    suspend fun addLesson(courseId: Long, draft: AdminLessonDraft)
    suspend fun getCourseCategories(): List<AdminCategory>
}

open class AdminRepositoryException(
    message: String,
    cause: Throwable? = null
) : Exception(message, cause)
