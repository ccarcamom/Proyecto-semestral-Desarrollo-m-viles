package com.example.proyecto_semestral.model.content

import androidx.annotation.DrawableRes
import com.example.proyecto_semestral.ui.components.ContentFilter

data class ExploreContent(
    val eyebrow: String,
    val title: String,
    val metadata: String,
    val type: ContentFilter,
    @DrawableRes val imageRes: Int,
    val id: String = title.lowercase().replace(" ", "-"),
    val imageUri: String? = null
)
