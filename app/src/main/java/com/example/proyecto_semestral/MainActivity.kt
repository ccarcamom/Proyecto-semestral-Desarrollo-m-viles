package com.example.proyecto_semestral

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.proyecto_semestral.ui.HomeScreen
import com.example.proyecto_semestral.ui.theme.ProyectoSemestralTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            ProyectoSemestralTheme {
                HomeScreen()
            }
        }
    }
}
