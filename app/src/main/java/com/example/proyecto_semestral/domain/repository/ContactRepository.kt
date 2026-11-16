package com.example.proyecto_semestral.domain.repository

data class ContactDraft(
    val userId: Long?,
    val email: String,
    val phone: String,
    val businessName: String,
    val businessType: String,
    val businessSize: String,
    val description: String
)

interface ContactRepository {
    suspend fun send(draft: ContactDraft): ContactReceipt
}

data class ContactReceipt(
    val id: Long,
    val status: String
)

class ContactRepositoryException(
    message: String,
    cause: Throwable? = null
) : Exception(message, cause)
