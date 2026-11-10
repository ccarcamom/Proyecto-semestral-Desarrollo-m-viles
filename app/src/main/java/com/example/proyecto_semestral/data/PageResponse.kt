package com.example.proyecto_semestral.data

data class PageResponse<T>(
    val content: List<T>,
    val number: Int,
    val size: Int,
    val totalElements: Long,
    val totalPages: Int,
    val first: Boolean,
    val last: Boolean,
    val empty: Boolean
)
