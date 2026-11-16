package com.example.proyecto_semestral.data.mapper.auth

import com.example.proyecto_semestral.data.dto.auth.UserDto
import com.example.proyecto_semestral.model.auth.User

fun UserDto.toModel(): User = User(
    id = id,
    name = nombre,
    email = email,
    role = rol,
    isActive = activo,
    createdAt = creadoEn
)
