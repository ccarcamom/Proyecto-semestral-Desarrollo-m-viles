package com.example.proyecto_semestral.model.admin

data class AdminCategory(
    val id: Long,
    val name: String
)

data class AdminRecipe(
    val id: Long,
    val title: String,
    val description: String,
    val imageUrl: String?,
    val duration: String,
    val categoryName: String
)

data class AdminCourse(
    val id: Long,
    val title: String,
    val description: String,
    val imageUrl: String?,
    val categoryName: String,
    val published: Boolean,
    val lessonCount: Int,
    val durationSeconds: Long
)

data class AdminIngredientDraft(
    val name: String,
    val amount: String
)

data class AdminStepDraft(
    val description: String
)

data class AdminRecipeDraft(
    val title: String,
    val description: String,
    val categoryId: Long,
    val duration: String,
    val yield: String,
    val videoUrl: String,
    val imageUrl: String,
    val published: Boolean,
    val createdBy: String,
    val ingredients: List<AdminIngredientDraft>,
    val steps: List<AdminStepDraft>
)

data class AdminCourseDraft(
    val title: String,
    val description: String,
    val categoryId: Long,
    val imageUrl: String,
    val objectives: List<String>,
    val publishNow: Boolean
)

data class AdminLessonDraft(
    val title: String,
    val description: String,
    val order: Int,
    val videoUrl: String,
    val durationSeconds: Int
)
