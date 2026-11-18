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
    val categoryId: Long?,
    val categoryName: String,
    val videoUrl: String = "",
    val yield: String = "",
    val suppliesTitle: String = "",
    val suppliesDescription: String = "",
    val published: Boolean = false,
    val ingredients: List<AdminIngredientDraft> = emptyList(),
    val steps: List<AdminStepDraft> = emptyList()
)

data class AdminLesson(
    val id: Long,
    val title: String,
    val description: String,
    val order: Int,
    val videoUrl: String,
    val durationSeconds: Int
)

data class AdminCourse(
    val id: Long,
    val title: String,
    val description: String,
    val imageUrl: String?,
    val categoryId: Long?,
    val categoryName: String,
    val published: Boolean,
    val lessonCount: Int,
    val durationSeconds: Long,
    val objectives: List<String>,
    val lessons: List<AdminLesson> = emptyList()
)

data class AdminContactQuery(
    val id: Long,
    val userId: Long?,
    val email: String,
    val phone: String,
    val businessName: String,
    val businessType: String,
    val businessSize: String,
    val description: String,
    val status: String,
    val createdAt: String
)

data class AdminIngredientDraft(
    val name: String,
    val amount: String
)

data class AdminStepDraft(
    val description: String
)

data class AdminRecipeImage(
    val bytes: ByteArray,
    val fileName: String,
    val contentType: String
)

data class AdminCourseImage(
    val bytes: ByteArray,
    val fileName: String,
    val contentType: String
)

data class AdminRecipeDraft(
    val title: String,
    val description: String,
    val categoryId: Long,
    val duration: String,
    val yield: String,
    val suppliesTitle: String,
    val suppliesDescription: String,
    val videoUrl: String,
    val image: AdminRecipeImage?,
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
    val image: AdminCourseImage?,
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
