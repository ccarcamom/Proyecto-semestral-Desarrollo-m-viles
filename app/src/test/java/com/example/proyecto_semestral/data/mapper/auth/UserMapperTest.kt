package com.example.proyecto_semestral.data.mapper.auth

import com.example.proyecto_semestral.data.dto.auth.UserDto
import org.junit.Assert.assertEquals
import org.junit.Test

class UserMapperTest {
    @Test
    fun `toModel maps every user field`() {
        val dto = UserDto(
            id = 7L,
            nombre = "Ada",
            email = "ada@example.com",
            rol = "USER",
            activo = true,
            creadoEn = "2026-11-15T10:00:00Z"
        )

        val user = dto.toModel()

        assertEquals(7L, user.id)
        assertEquals("Ada", user.name)
        assertEquals("ada@example.com", user.email)
        assertEquals("USER", user.role)
        assertEquals(true, user.isActive)
        assertEquals("2026-11-15T10:00:00Z", user.createdAt)
    }
}
