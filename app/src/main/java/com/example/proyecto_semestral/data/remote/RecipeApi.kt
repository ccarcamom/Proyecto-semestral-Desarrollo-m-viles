package com.example.proyecto_semestral.data.remote

import com.example.proyecto_semestral.data.dto.recipe.PageResponseDto
import com.example.proyecto_semestral.data.dto.recipe.RecipeDetailDto
import com.example.proyecto_semestral.data.dto.recipe.RecipeSummaryDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RecipeApi {
    @GET("api/v1/recipes")
    suspend fun getRecipes(
        @Query("q") query: String? = null,
        @Query("categoriaId") categoryId: Long? = null,
        @Query("page") page: Int = 0
    ): PageResponseDto<RecipeSummaryDto>

    @GET("api/v1/recipes/{id}")
    suspend fun getRecipe(@Path("id") id: Long): RecipeDetailDto
}
