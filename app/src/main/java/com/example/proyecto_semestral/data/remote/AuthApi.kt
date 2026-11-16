package com.example.proyecto_semestral.data.remote

import com.example.proyecto_semestral.data.dto.auth.AuthResponseDto
import com.example.proyecto_semestral.data.dto.auth.LoginRequestDto
import com.example.proyecto_semestral.data.dto.auth.RegisterRequestDto
import com.example.proyecto_semestral.data.dto.auth.UserDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthApi {
    @POST("api/auth/register")
    suspend fun register(@Body request: RegisterRequestDto): AuthResponseDto

    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequestDto): AuthResponseDto

    @GET("api/auth/me")
    suspend fun me(): UserDto
}
