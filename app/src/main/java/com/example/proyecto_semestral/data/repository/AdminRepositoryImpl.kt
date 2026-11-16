package com.example.proyecto_semestral.data.repository

import com.example.proyecto_semestral.BuildConfig
import com.example.proyecto_semestral.data.dto.admin.AdminCourseDto
import com.example.proyecto_semestral.data.dto.admin.ChangePublicationRequestDto
import com.example.proyecto_semestral.data.dto.admin.CourseCategoryDto
import com.example.proyecto_semestral.data.dto.admin.CreateCourseRequestDto
import com.example.proyecto_semestral.data.dto.admin.CreateIngredientRequestDto
import com.example.proyecto_semestral.data.dto.admin.CreateLessonRequestDto
import com.example.proyecto_semestral.data.dto.admin.CreateObjectiveRequestDto
import com.example.proyecto_semestral.data.dto.admin.CreateRecipeRequestDto
import com.example.proyecto_semestral.data.dto.admin.CreateStepRequestDto
import com.example.proyecto_semestral.data.dto.admin.UpdateRecipeRequestDto
import com.example.proyecto_semestral.data.dto.recipe.RecipeCategoryDto
import com.example.proyecto_semestral.data.dto.recipe.RecipeDetailDto
import com.example.proyecto_semestral.data.dto.recipe.RecipeSummaryDto
import com.example.proyecto_semestral.data.remote.AdminApi
import com.example.proyecto_semestral.domain.repository.AdminRepository
import com.example.proyecto_semestral.domain.repository.AdminRepositoryException
import com.example.proyecto_semestral.model.admin.AdminCategory
import com.example.proyecto_semestral.model.admin.AdminCourse
import com.example.proyecto_semestral.model.admin.AdminCourseDraft
import com.example.proyecto_semestral.model.admin.AdminIngredientDraft
import com.example.proyecto_semestral.model.admin.AdminLessonDraft
import com.example.proyecto_semestral.model.admin.AdminRecipe
import com.example.proyecto_semestral.model.admin.AdminRecipeDraft
import com.example.proyecto_semestral.model.admin.AdminStepDraft
import com.google.gson.JsonParser
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.IOException

class AdminRepositoryImpl(
    private val api: AdminApi,
    private val gatewayBaseUrl: String = BuildConfig.API_BASE_URL
) : AdminRepository {

    override suspend fun getRecipes(): List<AdminRecipe> = apiCall {
        api.getRecipes().map { it.toModel() }
    }

    override suspend fun getRecipe(id: Long): AdminRecipe = apiCall {
        api.getRecipe(id).toModel()
    }

    override suspend fun createRecipe(draft: AdminRecipeDraft): AdminRecipe = apiCall {
        val created = api.createRecipe(
            CreateRecipeRequestDto(
                titulo = draft.title.trim(),
                descripcion = draft.description.trim().ifBlank { null },
                categoriaId = draft.categoryId,
                videoUrl = draft.videoUrl.trim().ifBlank { null },
                imagenUrl = null,
                duracionTexto = draft.duration.trim().ifBlank { null },
                rendimientoTexto = draft.yield.trim().ifBlank { null },
                insumosTitulo = null,
                insumosDescripcion = null,
                publicada = draft.published,
                creadoPor = draft.createdBy.trim(),
                ingredientes = draft.ingredients
                    .filter { it.name.isNotBlank() }
                    .mapIndexed { index, ingredient ->
                        CreateIngredientRequestDto(
                            nombre = ingredient.name.trim(),
                            cantidadTexto = ingredient.amount.trim().ifBlank { null },
                            orden = index
                        )
                    },
                pasos = draft.steps
                    .filter { it.description.isNotBlank() }
                    .mapIndexed { index, step ->
                        CreateStepRequestDto(
                            numero = index + 1,
                            descripcion = step.description.trim()
                        )
                    }
            )
        )

        val result = draft.image?.let { image ->
            val body = image.bytes.toRequestBody(image.contentType.toMediaType())
            val part = MultipartBody.Part.createFormData("image", image.fileName, body)
            api.uploadRecipeImage(created.id, part)
        } ?: created

        result.toModel()
    }

    override suspend fun deleteRecipe(id: Long) = apiCall {
        api.deleteRecipe(id)
    }

    override suspend fun updateRecipe(
        id: Long,
        draft: AdminRecipeDraft
    ): AdminRecipe = apiCall {
        val updated = api.updateRecipe(
            id,
            draft.toUpdateRequest()
        )
        val result = draft.image?.let { image ->
            val body = image.bytes.toRequestBody(image.contentType.toMediaType())
            val part = MultipartBody.Part.createFormData("image", image.fileName, body)
            api.uploadRecipeImage(id, part)
        } ?: updated
        result.toModel()
    }

    override suspend fun getRecipeCategories(): List<AdminCategory> = apiCall {
        api.getRecipeCategories().map { it.toModel() }
    }

    override suspend fun getCourses(): List<AdminCourse> = apiCall {
        api.getCourses().map { it.toModel() }
    }

    override suspend fun createCourse(draft: AdminCourseDraft): AdminCourse = apiCall {
        var created = api.createCourse(
            CreateCourseRequestDto(
                titulo = draft.title.trim(),
                descripcion = draft.description.trim(),
                categoriaId = draft.categoryId,
                imagenUrl = draft.imageUrl.trim().ifBlank { null },
                objetivos = draft.objectives
                    .filter { it.isNotBlank() }
                    .mapIndexed { index, objective ->
                        CreateObjectiveRequestDto(
                            descripcion = objective.trim(),
                            orden = index + 1
                        )
                    }
            )
        )

        draft.image?.let { image ->
            val body = image.bytes.toRequestBody(image.contentType.toMediaType())
            val part = MultipartBody.Part.createFormData("image", image.fileName, body)
            created = api.uploadCourseImage(created.id, part)
        }

        if (draft.publishNow) {
            api.changeCoursePublication(
                created.id,
                ChangePublicationRequestDto(publicado = true)
            ).toModel()
        } else {
            created.toModel()
        }
    }

    override suspend fun changeCoursePublication(id: Long, published: Boolean): AdminCourse =
        apiCall {
            api.changeCoursePublication(
                id,
                ChangePublicationRequestDto(publicado = published)
            ).toModel()
        }

    override suspend fun updateCourse(
        id: Long,
        draft: AdminCourseDraft
    ): AdminCourse = apiCall {
        var updated = api.updateCourse(id, draft.toRequest())
        draft.image?.let { image ->
            val body = image.bytes.toRequestBody(image.contentType.toMediaType())
            val part = MultipartBody.Part.createFormData("image", image.fileName, body)
            updated = api.uploadCourseImage(id, part)
        }
        if (updated.publicado == draft.publishNow) {
            updated.toModel()
        } else {
            api.changeCoursePublication(
                id,
                ChangePublicationRequestDto(publicado = draft.publishNow)
            ).toModel()
        }
    }

    override suspend fun deleteCourse(id: Long) = apiCall {
        api.deleteCourse(id)
    }

    override suspend fun addLesson(courseId: Long, draft: AdminLessonDraft) = apiCall {
        api.createLesson(
            courseId = courseId,
            request = CreateLessonRequestDto(
                titulo = draft.title.trim(),
                descripcion = draft.description.trim().ifBlank { null },
                orden = draft.order,
                videoUrl = draft.videoUrl.trim(),
                duracionSegundos = draft.durationSeconds
            )
        )
        Unit
    }

    override suspend fun getCourseCategories(): List<AdminCategory> = apiCall {
        api.getCourseCategories().map { it.toModel() }
    }

    private fun RecipeSummaryDto.toModel() = AdminRecipe(
        id = id,
        title = titulo,
        description = descripcion.orEmpty(),
        imageUrl = imagenUrl.toGatewayUrl(),
        duration = duracionTexto.orEmpty(),
        categoryId = categoria?.id,
        categoryName = categoria?.nombre.orEmpty()
    )

    private fun RecipeDetailDto.toModel() = AdminRecipe(
        id = id,
        title = titulo,
        description = descripcion.orEmpty(),
        imageUrl = imagenUrl.toGatewayUrl(),
        duration = duracionTexto.orEmpty(),
        categoryId = categoria?.id,
        categoryName = categoria?.nombre.orEmpty(),
        videoUrl = videoUrl.orEmpty(),
        yield = rendimientoTexto.orEmpty(),
        published = publicada == true,
        ingredients = ingredientes.orEmpty().map {
            AdminIngredientDraft(
                name = it.nombre,
                amount = it.cantidadTexto.orEmpty()
            )
        },
        steps = pasos.orEmpty().map {
            AdminStepDraft(description = it.descripcion.orEmpty())
        }
    )

    private fun AdminCourseDto.toModel() = AdminCourse(
        id = id,
        title = titulo,
        description = descripcion.orEmpty(),
        imageUrl = imagenUrl.toGatewayUrl(),
        categoryId = categoria?.id,
        categoryName = categoria?.nombre.orEmpty(),
        published = publicado == true,
        lessonCount = lecciones?.size ?: 0,
        durationSeconds = duracionTotalSegundos ?: 0L,
        objectives = objetivos.orEmpty()
            .sortedBy { it.orden ?: Int.MAX_VALUE }
            .mapNotNull { it.descripcion?.takeIf(String::isNotBlank) }
    )

    private fun AdminCourseDraft.toRequest() = CreateCourseRequestDto(
        titulo = title.trim(),
        descripcion = description.trim(),
        categoriaId = categoryId,
        imagenUrl = null,
        objetivos = objectives
            .filter { it.isNotBlank() }
            .mapIndexed { index, objective ->
                CreateObjectiveRequestDto(
                    descripcion = objective.trim(),
                    orden = index + 1
                )
            }
    )

    private fun AdminRecipeDraft.toUpdateRequest() = UpdateRecipeRequestDto(
        titulo = title.trim(),
        descripcion = description.trim(),
        categoriaId = categoryId,
        videoUrl = videoUrl.trim(),
        imagenUrl = null,
        duracionTexto = duration.trim(),
        rendimientoTexto = yield.trim(),
        insumosTitulo = "",
        insumosDescripcion = "",
        publicada = published,
        ingredientes = ingredients
            .filter { it.name.isNotBlank() }
            .mapIndexed { index, ingredient ->
                CreateIngredientRequestDto(
                    nombre = ingredient.name.trim(),
                    cantidadTexto = ingredient.amount.trim().ifBlank { null },
                    orden = index
                )
            },
        pasos = steps
            .filter { it.description.isNotBlank() }
            .mapIndexed { index, step ->
                CreateStepRequestDto(
                    numero = index + 1,
                    descripcion = step.description.trim()
                )
            }
    )

    private fun RecipeCategoryDto.toModel() = AdminCategory(id = id, name = nombre)

    private fun CourseCategoryDto.toModel() = AdminCategory(id = id, name = nombre)

    private suspend fun <T> apiCall(block: suspend () -> T): T =
        try {
            block()
        } catch (exception: HttpException) {
            throw AdminRepositoryException(exception.toMessage(), exception)
        } catch (_: IOException) {
            throw AdminRepositoryException(
                "No se pudo conectar con el servidor. Revisa tu conexión."
            )
        }

    private fun HttpException.toMessage(): String {
        val serverMessage = runCatching {
            val body = response()?.errorBody()?.string().orEmpty()
            val json = JsonParser.parseString(body).asJsonObject
            sequenceOf("message", "mensaje", "detail", "error")
                .mapNotNull { key ->
                    json.get(key)?.takeIf { it.isJsonPrimitive }?.asString
                }
                .firstOrNull { it.isNotBlank() }
        }.getOrNull()

        val fallback = when (code()) {
            400 -> "Revisa los datos ingresados."
            401 -> "Tu sesión expiró. Inicia sesión nuevamente."
            403 -> "Solo el administrador puede realizar esta acción."
            404 -> "El recurso ya no existe."
            409 -> "Ya existe un registro con esos datos."
            in 500..599 -> "El servidor no está disponible. Inténtalo más tarde."
            else -> "No se pudo completar la operación."
        }
        return serverMessage ?: fallback
    }

    private fun String?.toGatewayUrl(): String? {
        val value = this?.trim()?.takeIf(String::isNotEmpty) ?: return null
        if (value.startsWith("http://") || value.startsWith("https://")) return value
        return gatewayBaseUrl.trimEnd('/') + "/" + value.trimStart('/')
    }
}
