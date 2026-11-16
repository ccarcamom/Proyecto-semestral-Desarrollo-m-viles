package com.example.proyecto_semestral.data.repository

import com.example.proyecto_semestral.data.dto.contact.CreateContactRequestDto
import com.example.proyecto_semestral.data.remote.ContactApi
import com.example.proyecto_semestral.domain.repository.ContactDraft
import com.example.proyecto_semestral.domain.repository.ContactRepository
import com.example.proyecto_semestral.domain.repository.ContactRepositoryException
import com.example.proyecto_semestral.domain.repository.ContactReceipt
import retrofit2.HttpException
import java.io.IOException

class ContactRepositoryImpl(
    private val api: ContactApi
) : ContactRepository {
    override suspend fun send(draft: ContactDraft): ContactReceipt {
        return try {
            val response = api.createContact(
                CreateContactRequestDto(
                    usuarioId = draft.userId,
                    email = draft.email.trim(),
                    telefono = draft.phone.trim().ifBlank { null },
                    nombreNegocio = draft.businessName.trim(),
                    tipoNegocio = draft.businessType.trim().ifBlank { null },
                    tamanoNegocio = draft.businessSize.trim().ifBlank { null },
                    descripcion = draft.description.trim()
                )
            )
            ContactReceipt(
                id = response.id,
                status = response.estado.orEmpty()
            )
        } catch (exception: HttpException) {
            throw ContactRepositoryException(
                if (exception.code() == 400) "Revisa los datos del formulario."
                else "No se pudo enviar tu consulta.",
                exception
            )
        } catch (exception: IOException) {
            throw ContactRepositoryException(
                "No se pudo conectar con el servidor.",
                exception
            )
        }
    }
}
