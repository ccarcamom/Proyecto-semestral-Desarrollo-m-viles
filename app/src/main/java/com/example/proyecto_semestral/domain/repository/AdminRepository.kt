package com.example.proyecto_semestral.domain.repository

import com.example.proyecto_semestral.model.admin.AdminCategory
import com.example.proyecto_semestral.model.admin.AdminContactQuery
import com.example.proyecto_semestral.model.admin.AdminCourse
import com.example.proyecto_semestral.model.admin.AdminCourseDraft
import com.example.proyecto_semestral.model.admin.AdminLessonDraft
import com.example.proyecto_semestral.model.admin.AdminRecipe
import com.example.proyecto_semestral.model.admin.AdminRecipeDraft

interface AdminRepository {
    suspend fun getRecipes(): List<AdminRecipe>
    suspend fun getRecipe(id: Long): AdminRecipe
    suspend fun createRecipe(draft: AdminRecipeDraft): AdminRecipe
    suspend fun updateRecipe(id: Long, draft: AdminRecipeDraft): AdminRecipe
    suspend fun deleteRecipe(id: Long)
    suspend fun getRecipeCategories(): List<AdminCategory>

    suspend fun getCourses(): List<AdminCourse>
    suspend fun createCourse(draft: AdminCourseDraft): AdminCourse
    suspend fun updateCourse(id: Long, draft: AdminCourseDraft): AdminCourse
    suspend fun changeCoursePublication(id: Long, published: Boolean): AdminCourse
    suspend fun deleteCourse(id: Long)
    suspend fun addLesson(courseId: Long, draft: AdminLessonDraft)
    suspend fun updateLesson(courseId: Long, lessonId: Long, draft: AdminLessonDraft)
    suspend fun deleteLesson(courseId: Long, lessonId: Long)
    suspend fun getCourseCategories(): List<AdminCategory>
    suspend fun createCourseCategory(name: String): AdminCategory
    suspend fun updateCourseCategory(id: Long, name: String): AdminCategory
    suspend fun deleteCourseCategory(id: Long)

    suspend fun getContactQueries(): List<AdminContactQuery>
    suspend fun updateContactQueryStatus(id: Long, status: String): AdminContactQuery
    suspend fun deleteContactQuery(id: Long)
}

open class AdminRepositoryException(
    message: String,
    cause: Throwable? = null
) : Exception(message, cause)
