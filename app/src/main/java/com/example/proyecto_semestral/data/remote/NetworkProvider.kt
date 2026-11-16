package com.example.proyecto_semestral.data.remote

import com.example.proyecto_semestral.BuildConfig
import com.example.proyecto_semestral.data.local.AuthTokenStore
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object NetworkProvider {
    private const val CONNECT_TIMEOUT_SECONDS = 15L
    private const val READ_TIMEOUT_SECONDS = 30L
    private const val WRITE_TIMEOUT_SECONDS = 30L

    fun createOkHttpClient(tokenStore: AuthTokenStore): OkHttpClient =
        OkHttpClient.Builder()
            .connectTimeout(CONNECT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .addInterceptor(AuthInterceptor(tokenStore))
            .apply {
                if (BuildConfig.DEBUG) {
                    addInterceptor(
                        HttpLoggingInterceptor().apply {
                            redactHeader("Authorization")
                            level = HttpLoggingInterceptor.Level.BASIC
                        }
                    )
                }
            }
            .build()

    fun createRetrofit(tokenStore: AuthTokenStore): Retrofit {
        val baseUrl = BuildConfig.API_BASE_URL
        require(baseUrl.isNotBlank()) {
            "API_BASE_URL must be configured for this build type."
        }
        require(baseUrl.endsWith('/')) {
            "API_BASE_URL must end with '/'."
        }

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(createOkHttpClient(tokenStore))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
