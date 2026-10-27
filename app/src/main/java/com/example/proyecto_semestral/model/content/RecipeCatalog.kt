package com.example.proyecto_semestral.model.content

import com.example.proyecto_semestral.R
import com.example.proyecto_semestral.ui.components.ContentFilter

object RecipeCatalog {
    private val recipes = listOf(
        Recipe(
            id = "galletas-chips-chocolate",
            category = "Galletas",
            title = "Galletas con chips de chocolate",
            description = "Crujientes por fuera, suaves por dentro. Una receta versátil para sumar a tu vitrina.",
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
                "Forma 12 porciones sobre una bandeja con papel para hornear, dejando espacio entre ellas.",
                "Hornea a 180 °C durante 12 a 15 minutos. Deja enfriar antes de servir."
            ),
            relatedVideo = ExploreContent(
                eyebrow = "VIDEO TÉCNICO",
                title = "Introducción al templado de chocolate",
                metadata = "Chocolatería · 8 min",
                type = ContentFilter.Videos,
                imageRes = R.drawable.chocolate,
                id = "video-templado-chocolate"
            )
        ),
        Recipe(
            id = "empanadas-queso-horno",
            category = "Empanadas",
            title = "Empanadas de queso al horno",
            description = "Una preparación dorada y crujiente, con un relleno de queso fundido para servir caliente.",
            duration = "50 min",
            yield = "10 unidades",
            imageRes = R.drawable.bolleria,
            ingredients = listOf(
                Ingredient("Harina", "300 g"),
                Ingredient("Mantequilla", "60 g"),
                Ingredient("Agua tibia", "120 ml"),
                Ingredient("Queso mantecoso", "250 g"),
                Ingredient("Huevo", "1 unidad")
            ),
            steps = listOf(
                "Mezcla la harina con la mantequilla y el agua tibia hasta formar una masa suave.",
                "Amasa durante unos minutos, cubre y deja reposar por 20 minutos.",
                "Estira la masa, corta discos y rellena cada uno con queso.",
                "Cierra las empanadas, pincela con huevo y hornea a 190 °C hasta dorar."
            )
        ),
        Recipe(
            id = "postre-chocolate-vaso",
            category = "Chocolatería",
            title = "Postre de chocolate en vaso",
            description = "Capas suaves y cremosas de chocolate para una presentación individual y elegante.",
            duration = "25 min + frío",
            yield = "6 unidades",
            imageRes = R.drawable.chocolate,
            ingredients = listOf(
                Ingredient("Chocolate semiamargo", "180 g"),
                Ingredient("Crema para batir", "300 ml"),
                Ingredient("Azúcar", "40 g"),
                Ingredient("Galletas molidas", "120 g"),
                Ingredient("Frutos rojos", "100 g")
            ),
            steps = listOf(
                "Derrite el chocolate a baño María y deja que baje ligeramente su temperatura.",
                "Bate la crema con el azúcar hasta obtener una textura aireada.",
                "Integra el chocolate con la crema usando movimientos envolventes.",
                "Arma capas de galleta y mousse en vasos. Refrigera antes de servir."
            )
        )
    )

    fun findById(id: String): Recipe? = recipes.firstOrNull { it.id == id }
}
