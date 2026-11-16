package com.example.proyecto_semestral.di

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.example.proyecto_semestral.data.local.AuthTokenStore
import com.example.proyecto_semestral.data.remote.AuthApi
import com.example.proyecto_semestral.data.remote.NetworkProvider
import com.example.proyecto_semestral.data.repository.AuthRepositoryImpl
import com.example.proyecto_semestral.domain.repository.AuthRepository
import com.example.proyecto_semestral.ui.viewmodel.auth.AuthViewModel

class AppContainer(context: Context) {
    private val tokenStore = AuthTokenStore(context.applicationContext)
    private val authApi = NetworkProvider.createRetrofit(tokenStore)
        .create(AuthApi::class.java)

    private val authRepository: AuthRepository = AuthRepositoryImpl(
        tokenStore = tokenStore,
        api = authApi
    )

    val authViewModelFactory: ViewModelProvider.Factory =
        AuthViewModel.factory(authRepository)
}
