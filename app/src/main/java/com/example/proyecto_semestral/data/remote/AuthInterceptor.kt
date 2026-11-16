package com.example.proyecto_semestral.data.remote

import com.example.proyecto_semestral.data.auth.AuthTokenStore
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val tokenStore: AuthTokenStore
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val isPublicAuthRequest = originalRequest.url.encodedPath in PUBLIC_AUTH_PATHS
        val token = if (isPublicAuthRequest) null else runBlocking { tokenStore.getToken() }
        val authenticatedRequest = if (token == null) {
            originalRequest
        } else {
            originalRequest.newBuilder()
                .header(AUTHORIZATION, "Bearer $token")
                .build()
        }

        val response = chain.proceed(authenticatedRequest)
        if (response.code == 401 && token != null) {
            runBlocking { tokenStore.clearToken() }
        }
        return response
    }

    private companion object {
        const val AUTHORIZATION = "Authorization"
        val PUBLIC_AUTH_PATHS = setOf(
            "/api/auth/login",
            "/api/auth/register"
        )
    }
}
