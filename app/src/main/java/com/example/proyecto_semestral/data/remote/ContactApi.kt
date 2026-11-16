package com.example.proyecto_semestral.data.remote

import com.example.proyecto_semestral.data.dto.contact.ContactResponseDto
import com.example.proyecto_semestral.data.dto.contact.CreateContactRequestDto
import retrofit2.http.Body
import retrofit2.http.POST

interface ContactApi {
    @POST("api/contacto")
    suspend fun createContact(
        @Body request: CreateContactRequestDto
    ): ContactResponseDto
}
