// Mantiene en memoria las recetas iniciales y las creadas por el administrador.
package com.example.proyecto_semestral.repository.admin

import com.example.proyecto_semestral.R
import com.example.proyecto_semestral.model.admin.AppRecipe
import com.example.proyecto_semestral.model.content.Ingredient
import java.util.UUID
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

object InMemoryRecipeRepository {

    private val _recipes = MutableStateFlow(initialRecipes)
    val recipes: StateFlow<List<AppRecipe>> = _recipes.asStateFlow()

    fun findById(id: String): AppRecipe? = _recipes.value.firstOrNull { it.id == id }

    fun create(
        title: String,
        description: String,
        videoUrl: String,
        category: String = "Receta"
    ): AppRecipe {
        val recipe = AppRecipe(
            id = UUID.randomUUID().toString(),
            title = title.trim(),
            description = description.trim(),
            videoUrl = videoUrl.trim(),
            category = category,
            imageRes = R.drawable.galletas
        )
        _recipes.update { current -> current + recipe }
        return recipe
    }

    fun update(updatedRecipe: AppRecipe): Boolean {
        var updated = false
        _recipes.update { current ->
            current.map { recipe ->
                if (recipe.id == updatedRecipe.id) {
                    updated = true
                    updatedRecipe
                } else {
                    recipe
                }
            }
        }
        return updated
    }

    fun delete(id: String) {
        _recipes.update { current -> current.filterNot { it.id == id } }
    }
}

private val initialRecipes = listOf(
    AppRecipe(
        id = "galletas-chips-chocolate",
        title = "Galletas con chips de chocolate",
        description = "Crujientes por fuera, suaves por dentro. Una receta versátil para sumar a tu vitrina.",
        videoUrl = "https://youtube.com/watch?v=galletas",
        category = "Galletas",
        duration = "35 min",
        yield = "12 unidades",
        imageRes = R.drawable.galletas,
        ingredients = listOf(
            Ingredient("Harina", "180 g"),
            Ingredient("Mantequilla", "100 g"),
            Ingredient("Azúcar", "100 g"),
            Ingredient("Huevo", "1 unidad"),
            Ingredient("Chips de chocolate", "100 g")
        ),
        steps = listOf(
            "Mezcla la mantequilla a temperatura ambiente con el azúcar. Agrega el huevo e integra.",
            "Incorpora la harina y los chips de chocolate hasta obtener una masa uniforme.",
            "Forma 12 porciones sobre una bandeja con papel para hornear.",
            "Hornea a 180 °C durante 12 a 15 minutos."
        )
    ),
    AppRecipe(
        id = "empanadas-queso-horno",
        title = "Empanadas de queso al horno",
        description = "Una preparación dorada y crujiente, con un relleno de queso fundido.",
        videoUrl = "https://youtube.com/watch?v=empanadas",
        category = "Empanadas",
        duration = "50 min",
        yield = "10 unidades",
        imageRes = R.drawable.bolleria
    ),
    AppRecipe(
        id = "postre-chocolate-vaso",
        title = "Postre de chocolate en vaso",
        description = "Capas suaves y cremosas de chocolate para una presentación individual.",
        videoUrl = "https://youtube.com/watch?v=postre-chocolate",
        category = "Chocolatería",
        duration = "25 min + frío",
        yield = "6 unidades",
        imageRes = R.drawable.chocolate
    )
)
