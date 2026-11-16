package com.example.proyecto_semestral

import android.app.Application
import com.example.proyecto_semestral.di.AppContainer

class MartiniApplication : Application() {
    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}
