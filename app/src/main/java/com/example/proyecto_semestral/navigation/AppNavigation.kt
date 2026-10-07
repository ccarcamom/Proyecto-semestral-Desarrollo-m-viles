package com.example.proyecto_semestral.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.example.proyecto_semestral.ui.components.BottomBarItem
import com.example.proyecto_semestral.ui.screens.explore.ExploreScreen
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
    var currentUserName by rememberSaveable {
        mutableStateOf(routerGuard.getUserName().ifBlank { "Usuario" })
    }

    when (currentRoute) {
        AppRoute.Home -> {
            if (routerGuard.canAccessDashboard()) {
                HomeScreen(
                    userName = currentUserName,
                    onBottomBarItemSelected = { item ->
                        if (item == BottomBarItem.Explore) {
                            currentRoute = AppRoute.Explore
                        }
                    },
                    onLogout = {
                        routerGuard.logout()
                        currentUserName = "Usuario"
                        currentRoute = AppRoute.Login
                    }
                )
            } else {
                LoginScreen(
                    onLoginSuccess = { email, password ->
                        val user = UserStore.findUser(email, password)
                        if (user != null) {
                            currentUserName = user.name
                            routerGuard.saveSession(true, user.name)
                            currentRoute = AppRoute.Home
                            true
                        } else {
                            false
                        }
                    },
                    onRegisterClick = { currentRoute = AppRoute.Register }
                )
            }
        }

        AppRoute.Explore -> {
            if (routerGuard.canAccessDashboard()) {
                ExploreScreen(
                    onBottomBarItemSelected = { item ->
                        if (item == BottomBarItem.Home) {
                            currentRoute = AppRoute.Home
                        }
                    }
                )
            } else {
                LoginScreen(
                    onLoginSuccess = { email, password ->
                        val user = UserStore.findUser(email, password)
                        if (user != null) {
                            currentUserName = user.name
                            routerGuard.saveSession(true, user.name)
                            currentRoute = AppRoute.Explore
                            true
                        } else {
                            false
                        }
                    },
                    onRegisterClick = { currentRoute = AppRoute.Register }
                )
            }
        }

        AppRoute.Register -> RegisterScreen(
            onRegisterSuccess = { name, email, password ->
                val isRegistered = UserStore.registerUser(name, email, password)
                if (isRegistered) {
                    currentUserName = name.trim()
                    routerGuard.saveSession(true, currentUserName)
                    currentRoute = AppRoute.Home
                }
                isRegistered
            },
            onLoginClick = { currentRoute = AppRoute.Login }
        )

        AppRoute.Login -> LoginScreen(
            onLoginSuccess = { email, password ->
                val user = UserStore.findUser(email, password)
                if (user != null) {
                    currentUserName = user.name
                    routerGuard.saveSession(true, user.name)
                    currentRoute = AppRoute.Home
                    true
                } else {
                    false
                }
            },
            onRegisterClick = { currentRoute = AppRoute.Register }
        )
    }
}

private enum class AppRoute {
    Login,
    Register,
    Home,
    Explore
}
