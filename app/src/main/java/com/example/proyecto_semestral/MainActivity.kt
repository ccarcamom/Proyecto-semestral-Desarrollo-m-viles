package com.example.proyecto_semestral

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.example.proyecto_semestral.ui.HomeScreen
import com.example.proyecto_semestral.ui.screens.auth.LoginScreen
import com.example.proyecto_semestral.ui.screens.auth.RegisterScreen
import com.example.proyecto_semestral.ui.theme.ProyectoSemestralTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            ProyectoSemestralTheme {
                var isLoggedIn by rememberSaveable { mutableStateOf(false) }
                var showRegister by rememberSaveable { mutableStateOf(false) }

                if (isLoggedIn) {
                    HomeScreen()
                } else if (showRegister) {
                    RegisterScreen(
                        onRegisterSuccess = { isLoggedIn = true },
                        onLoginClick = { showRegister = false }
                    )
                } else {
                    LoginScreen(
                        onLoginSuccess = { isLoggedIn = true },
                        onRegisterClick = { showRegister = true }
                    )
                }
            }
        }
    }
}
