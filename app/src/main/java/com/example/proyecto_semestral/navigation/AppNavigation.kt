package com.example.proyecto_semestral.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.example.proyecto_semestral.model.profile.DEFAULT_BUSINESS_SIZE
import com.example.proyecto_semestral.model.profile.DEFAULT_BUSINESS_TYPE
import com.example.proyecto_semestral.ui.components.BottomBarItem
import com.example.proyecto_semestral.ui.components.ContentFilter
import com.example.proyecto_semestral.ui.screens.contact.ContactScreen
import com.example.proyecto_semestral.ui.screens.course.CourseDetailScreen
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
    var currentUserPhone by rememberSaveable {
        mutableStateOf(routerGuard.getUserPhone())
    }
    var currentBusinessName by rememberSaveable {
        mutableStateOf(routerGuard.getBusinessName())
    }
    var currentBusinessType by rememberSaveable {
        mutableStateOf(routerGuard.getBusinessType())
    }
    var currentBusinessSize by rememberSaveable {
        mutableStateOf(routerGuard.getBusinessSize())
    }
    var currentInterests by remember {
        mutableStateOf(routerGuard.getUserInterests())
    }
    var selectedExploreFilter by rememberSaveable { mutableStateOf(ContentFilter.All) }
    var exploreSearchQuery by rememberSaveable { mutableStateOf("") }
    var selectedRecipeId by rememberSaveable { mutableStateOf("") }
    var selectedVideoId by rememberSaveable { mutableStateOf("") }
    var selectedCourseId by rememberSaveable { mutableStateOf("") }
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
                    onSearch = { query ->
                        exploreSearchQuery = query
                        selectedExploreFilter = ContentFilter.All
                        currentRoute = AppRoute.Explore
                    },
                    onCourseClick = { courseId ->
                        selectedCourseId = courseId
                        currentRoute = AppRoute.Course
                    },
                    onLogout = {
                        routerGuard.logout()
                        currentUserName = "Usuario"
                        currentUserEmail = ""
                        currentUserPhone = ""
                        currentBusinessName = ""
                        currentBusinessType = DEFAULT_BUSINESS_TYPE
                        currentBusinessSize = DEFAULT_BUSINESS_SIZE
                        currentInterests = emptySet()
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
                    },
                    onCourseClick = { courseId ->
                        selectedCourseId = courseId
                        currentRoute = AppRoute.Course
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
            onRegisterSuccess = { name, email, password, businessName, businessType, businessSize ->
                val isRegistered = UserStore.registerUser(name, email, password)
                if (isRegistered) {
                    currentUserName = name.trim()
                    currentUserEmail = email.trim().lowercase()
                    currentBusinessName = businessName.trim()
                    currentBusinessType = businessType
                    currentBusinessSize = businessSize
                    currentInterests = emptySet()
                    routerGuard.saveSession(true, currentUserName, currentUserEmail)
                    routerGuard.saveProfile(
                        name = currentUserName,
                        email = currentUserEmail,
                        phone = currentUserPhone,
                        businessName = currentBusinessName,
                        businessType = currentBusinessType,
                        businessSize = currentBusinessSize,
                        interests = currentInterests
                    )
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
            onReturnToCourse = { currentRoute = AppRoute.Course }
        )

        AppRoute.Course -> CourseDetailScreen(
            courseId = selectedCourseId,
            onBack = { currentRoute = AppRoute.Explore },
            onLessonClick = { videoId ->
                selectedVideoId = videoId
                videoReturnRoute = AppRoute.Course
                currentRoute = AppRoute.Video
            }
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
                    initialPhone = currentUserPhone,
                    initialBusinessName = currentBusinessName,
                    initialBusinessType = currentBusinessType,
                    initialBusinessSize = currentBusinessSize,
                    initialInterests = currentInterests,
                    onBack = { currentRoute = AppRoute.Home },
                    onSaveChanges = { name, email, phone, businessName, businessType, businessSize, interests ->
                        currentUserName = name.trim().ifBlank { currentUserName }
                        currentUserEmail = email.trim()
                        currentUserPhone = phone.trim()
                        currentBusinessName = businessName.trim()
                        currentBusinessType = businessType
                        currentBusinessSize = businessSize
                        currentInterests = interests
                        routerGuard.saveProfile(
                            name = currentUserName,
                            email = currentUserEmail,
                            phone = currentUserPhone,
                            businessName = currentBusinessName,
                            businessType = currentBusinessType,
                            businessSize = currentBusinessSize,
                            interests = currentInterests
                        )
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
    Course,
    Contact,
    Profile
}
