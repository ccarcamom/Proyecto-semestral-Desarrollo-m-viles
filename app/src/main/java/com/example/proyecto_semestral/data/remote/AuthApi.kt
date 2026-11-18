package com.example.proyecto_semestral.data.remote

import com.example.proyecto_semestral.data.dto.auth.AuthResponseDto
import com.example.proyecto_semestral.data.dto.auth.LoginRequestDto
import com.example.proyecto_semestral.data.dto.auth.RegisterRequestDto
import com.example.proyecto_semestral.data.dto.auth.UpdateProfileRequestDto
import com.example.proyecto_semestral.data.dto.auth.UserDto
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part

interface AuthApi {
    @POST("api/auth/register")
    suspend fun register(@Body request: RegisterRequestDto): AuthResponseDto

    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequestDto): AuthResponseDto

    @GET("api/auth/me")
    suspend fun me(): UserDto

    @PUT("api/auth/perfil")
    suspend fun updateProfile(@Body request: UpdateProfileRequestDto): UserDto

    @Multipart
    @POST("api/auth/perfil/foto")
    suspend fun uploadProfilePhoto(@Part image: MultipartBody.Part): UserDto
}
