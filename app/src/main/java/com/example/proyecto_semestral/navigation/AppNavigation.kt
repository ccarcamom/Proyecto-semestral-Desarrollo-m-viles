package com.example.proyecto_semestral.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.example.proyecto_semestral.ui.components.BottomBarItem
import com.example.proyecto_semestral.ui.components.ContentFilter
import com.example.proyecto_semestral.ui.screens.contact.ContactScreen
import com.example.proyecto_semestral.ui.screens.explore.ExploreScreen
import com.example.proyecto_semestral.ui.screens.home.HomeScreen
import com.example.proyecto_semestral.ui.screens.profile.ProfileScreen
import com.example.proyecto_semestral.ui.screens.auth.LoginScreen
import com.example.proyecto_semestral.ui.screens.auth.RegisterScreen
import com.example.proyecto_semestral.ui.screens.recipe.RecipeDetailScreen
import com.example.proyecto_semestral.ui.screens.video.VideoScreen

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
    var currentUserEmail by rememberSaveable {
        mutableStateOf(routerGuard.getUserEmail())
    }
    var selectedExploreFilter by rememberSaveable { mutableStateOf(ContentFilter.All) }
    var exploreSearchQuery by rememberSaveable { mutableStateOf("") }
    var selectedRecipeId by rememberSaveable { mutableStateOf("") }
    var selectedVideoId by rememberSaveable { mutableStateOf("") }
    var videoReturnRoute by rememberSaveable { mutableStateOf(AppRoute.Explore) }

    when (currentRoute) {
        AppRoute.Home -> {
            if (routerGuard.canAccessDashboard()) {
                HomeScreen(
                    userName = currentUserName,
                    onBottomBarItemSelected = { item ->
                        when (item) {
                            BottomBarItem.Explore -> currentRoute = AppRoute.Explore
                            BottomBarItem.Contact -> currentRoute = AppRoute.Contact
                            BottomBarItem.Profile -> currentRoute = AppRoute.Profile
                            else -> Unit
                        }
                    },
                    onLogout = {
                        routerGuard.logout()
                        currentUserName = "Usuario"
                        currentUserEmail = ""
                        currentRoute = AppRoute.Login
                    }
                )
            } else {
                LoginScreen(
                    onLoginSuccess = { email, password ->
                        val user = UserStore.findUser(email, password)
                        if (user != null) {
                            currentUserName = user.name
                            currentUserEmail = user.email
                            routerGuard.saveSession(true, user.name, user.email)
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
                        when (item) {
                            BottomBarItem.Home -> currentRoute = AppRoute.Home
                            BottomBarItem.Contact -> currentRoute = AppRoute.Contact
                            BottomBarItem.Profile -> currentRoute = AppRoute.Profile
                            else -> Unit
                        }
                    },
                    selectedFilter = selectedExploreFilter,
                    searchQuery = exploreSearchQuery,
                    onFilterSelected = { selectedExploreFilter = it },
                    onSearchQueryChange = { exploreSearchQuery = it },
                    onRecipeClick = { recipeId ->
                        selectedRecipeId = recipeId
                        currentRoute = AppRoute.Recipe
                    },
                    onVideoClick = { videoId ->
                        selectedVideoId = videoId
                        videoReturnRoute = AppRoute.Explore
                        currentRoute = AppRoute.Video
                    }
                )
            } else {
                LoginScreen(
                    onLoginSuccess = { email, password ->
                        val user = UserStore.findUser(email, password)
                        if (user != null) {
                            currentUserName = user.name
                            currentUserEmail = user.email
                            routerGuard.saveSession(true, user.name, user.email)
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
                    currentUserEmail = email.trim().lowercase()
                    routerGuard.saveSession(true, currentUserName, currentUserEmail)
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
                    currentUserEmail = user.email
                    routerGuard.saveSession(true, user.name, user.email)
                    currentRoute = AppRoute.Home
                    true
                } else {
                    false
                }
            },
            onRegisterClick = { currentRoute = AppRoute.Register }
        )

        AppRoute.Recipe -> RecipeDetailScreen(
            recipeId = selectedRecipeId,
            onBack = { currentRoute = AppRoute.Explore },
            onRelatedVideoClick = { videoId ->
                selectedVideoId = videoId
                videoReturnRoute = AppRoute.Recipe
                currentRoute = AppRoute.Video
            }
        )

        AppRoute.Video -> VideoScreen(
            videoId = selectedVideoId,
            onBack = { currentRoute = videoReturnRoute },
            onNextLesson = { },
            onReturnToCourse = { currentRoute = AppRoute.Explore }
        )

        AppRoute.Contact -> {
            if (routerGuard.canAccessDashboard()) {
                ContactScreen(
                    initialEmail = currentUserEmail,
                    onBottomBarItemSelected = { item ->
                        when (item) {
                            BottomBarItem.Home -> currentRoute = AppRoute.Home
                            BottomBarItem.Explore -> currentRoute = AppRoute.Explore
                            BottomBarItem.Profile -> currentRoute = AppRoute.Profile
                            else -> Unit
                        }
                    }
                )
            } else {
                currentRoute = AppRoute.Login
            }
        }

        AppRoute.Profile -> {
            if (routerGuard.canAccessDashboard()) {
                ProfileScreen(
                    initialName = currentUserName,
                    initialEmail = currentUserEmail,
                    onBack = { currentRoute = AppRoute.Home },
                    onSaveChanges = { name, email, _, _, _, _ ->
                        currentUserName = name.trim().ifBlank { currentUserName }
                        currentUserEmail = email.trim()
                        routerGuard.saveSession(true, currentUserName, currentUserEmail)
                    },
                    onBottomBarItemSelected = { item ->
                        when (item) {
                            BottomBarItem.Home -> currentRoute = AppRoute.Home
                            BottomBarItem.Explore -> currentRoute = AppRoute.Explore
                            BottomBarItem.Contact -> currentRoute = AppRoute.Contact
                            else -> Unit
                        }
                    }
                )
            } else {
                currentRoute = AppRoute.Login
            }
        }
    }
}

private enum class AppRoute {
    Login,
    Register,
    Home,
    Explore,
    Recipe,
    Video,
    Contact,
    Profile
}
