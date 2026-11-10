package com.example.proyecto_semestral.data.remote

import com.example.proyecto_semestral.data.PageResponse
import com.example.proyecto_semestral.data.RecipeSummaryDto
import retrofit2.http.GET
import retrofit2.http.Query

interface RecipeApi {
    @GET("recipes")
    suspend fun getRecipes(
        @Query("q") query: String? = null,
        @Query("categoriaId") categoryId: Long? = null,
        @Query("page") page: Int = 0
    ): PageResponse<RecipeSummaryDto>
}