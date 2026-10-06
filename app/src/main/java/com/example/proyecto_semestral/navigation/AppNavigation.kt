package com.example.proyecto_semestral.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.example.proyecto_semestral.ui.screens.home.HomeScreen
import com.example.proyecto_semestral.ui.screens.auth.LoginScreen
import com.example.proyecto_semestral.ui.screens.auth.RegisterScreen

@Composable
fun AppNavigation() {
    val context = LocalContext.current
    val routerGuard = remember { RouterGuard(context.applicationContext) }
    var currentRoute by rememberSaveable {
        mutableStateOf(
            if (routerGuard.canAccessDashboard()) {
                AppRoute.Home
            } else {
                AppRoute.Login
            }
        )
    }

    when (currentRoute) {
        AppRoute.Home -> {
            if (routerGuard.canAccessDashboard()) {
                HomeScreen(
                    onLogout = {
                        routerGuard.logout()
                        currentRoute = AppRoute.Login
                    }
                )
            } else {
                LoginScreen(
                    onLoginSuccess = {
                        routerGuard.saveSession(true)
                        currentRoute = AppRoute.Home
                    },
                    onRegisterClick = { currentRoute = AppRoute.Register }
                )
            }
        }

        AppRoute.Register -> RegisterScreen(
            onRegisterSuccess = {
                routerGuard.saveSession(true)
                currentRoute = AppRoute.Home
            },
            onLoginClick = { currentRoute = AppRoute.Login }
        )

        AppRoute.Login -> LoginScreen(
            onLoginSuccess = {
                routerGuard.saveSession(true)
                currentRoute = AppRoute.Home
            },
            onRegisterClick = { currentRoute = AppRoute.Register }
        )
    }
}

private enum class AppRoute {
    Login,
    Register,
    Home
}
