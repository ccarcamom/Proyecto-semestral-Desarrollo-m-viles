package com.example.proyecto_semestral.data.repository

import com.example.proyecto_semestral.BuildConfig
import com.example.proyecto_semestral.data.dto.recipe.RecipeCategoryDto
import com.example.proyecto_semestral.data.dto.recipe.RecipeDetailDto
import com.example.proyecto_semestral.data.dto.recipe.RecipeIngredientDto
import com.example.proyecto_semestral.data.dto.recipe.RecipeStepDto
import com.example.proyecto_semestral.data.dto.recipe.RecipeSummaryDto
import com.example.proyecto_semestral.data.remote.RecipeApi
import com.example.proyecto_semestral.domain.repository.RecipeNotFoundException
import com.example.proyecto_semestral.domain.repository.RecipeRepository
import com.example.proyecto_semestral.domain.repository.RecipeRepositoryException
import com.example.proyecto_semestral.model.recipe.RecipeCategory
import com.example.proyecto_semestral.model.recipe.RecipeDetail
import com.example.proyecto_semestral.model.recipe.RecipeIngredient
import com.example.proyecto_semestral.model.recipe.RecipePage
import com.example.proyecto_semestral.model.recipe.RecipeStep
import com.example.proyecto_semestral.model.recipe.RecipeSummary
import retrofit2.HttpException
import java.io.IOException

class RecipeRepositoryImpl(
    private val api: RecipeApi,
    private val gatewayBaseUrl: String = BuildConfig.API_BASE_URL
) : RecipeRepository {
    override suspend fun getCategories(): List<RecipeCategory> = networkCall {
        api.getCategories().map { it.toModel() }
    }

    override suspend fun getRecipes(
        query: String?,
        categoryId: Long?,
        page: Int
    ): RecipePage = networkCall {
        val response = api.getRecipes(
            query = query?.trim()?.takeIf(String::isNotEmpty),
            categoryId = categoryId,
            page = page
        )
        RecipePage(
            recipes = response.content.map { it.toModel() },
            page = response.number,
            pageSize = response.size,
            totalElements = response.totalElements,
            totalPages = response.totalPages,
            isFirst = response.first,
            isLast = response.last
        )
    }

    override suspend fun getRecipe(id: Long): RecipeDetail {
        require(id > 0) { "El id de receta debe ser positivo." }
        return try {
            api.getRecipe(id).toModel()
        } catch (exception: HttpException) {
            if (exception.code() == 404) throw RecipeNotFoundException(exception)
            throw RecipeRepositoryException(httpErrorMessage(exception.code()), exception)
        } catch (exception: IOException) {
            throw RecipeRepositoryException(CONNECTION_ERROR, exception)
        }
    }

    private suspend fun <T> networkCall(block: suspend () -> T): T =
        try {
            block()
        } catch (exception: HttpException) {
            throw RecipeRepositoryException(httpErrorMessage(exception.code()), exception)
        } catch (exception: IOException) {
            throw RecipeRepositoryException(CONNECTION_ERROR, exception)
        }

    private fun RecipeSummaryDto.toModel() = RecipeSummary(
        id = id,
        title = titulo,
        description = descripcion.orEmpty(),
        imageUrl = imagenUrl.toGatewayUrl(),
        duration = duracionTexto.orEmpty(),
        category = categoria?.toModel(),
        published = publicada
    )

    private fun RecipeDetailDto.toModel() = RecipeDetail(
        id = id,
        title = titulo,
        description = descripcion.orEmpty(),
        imageUrl = imagenUrl.toGatewayUrl(),
        duration = duracionTexto.orEmpty(),
        category = categoria?.toModel(),
        videoUrl = videoUrl?.takeIf(String::isNotBlank),
        yield = rendimientoTexto.orEmpty(),
        suppliesTitle = insumosTitulo.orEmpty(),
        suppliesDescription = insumosDescripcion.orEmpty(),
        ingredients = ingredientes.orEmpty()
            .sortedBy { it.orden ?: Int.MAX_VALUE }
            .map { it.toModel() },
        steps = pasos.orEmpty()
            .sortedBy { it.numero ?: Int.MAX_VALUE }
            .map { it.toModel() }
    )

    private fun RecipeCategoryDto.toModel() = RecipeCategory(id = id, name = nombre)

    private fun RecipeIngredientDto.toModel() = RecipeIngredient(
        id = id,
        name = nombre,
        amount = cantidadTexto.orEmpty(),
        order = orden ?: 0
    )

    private fun RecipeStepDto.toModel() = RecipeStep(
        id = id,
        number = numero ?: 0,
        description = descripcion
    )

    private fun String?.toGatewayUrl(): String? {
        val value = this?.trim()?.takeIf(String::isNotEmpty) ?: return null
        if (value.startsWith("http://") || value.startsWith("https://")) return value
        return gatewayBaseUrl.trimEnd('/') + "/" + value.trimStart('/')
    }

    private fun httpErrorMessage(code: Int): String = when (code) {
        401, 403 -> "No tienes autorización para consultar las recetas."
        in 500..599 -> "El servidor de recetas no está disponible. Inténtalo más tarde."
        else -> "No se pudieron cargar las recetas."
    }

    private companion object {
        const val CONNECTION_ERROR = "No se pudo conectar con el servidor. Revisa tu conexión."
    }
}
