package com.example.proyecto_semestral.navigation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.example.proyecto_semestral.model.admin.AppUser
import com.example.proyecto_semestral.model.admin.UserRole
import com.example.proyecto_semestral.model.profile.DEFAULT_BUSINESS_SIZE
import com.example.proyecto_semestral.model.profile.DEFAULT_BUSINESS_TYPE
import com.example.proyecto_semestral.repository.admin.InMemorySessionRepository
import com.example.proyecto_semestral.repository.admin.InMemoryUserRepository
import com.example.proyecto_semestral.repository.admin.LoginResult
import com.example.proyecto_semestral.ui.components.BottomBarItem
import com.example.proyecto_semestral.ui.components.ContentFilter
import com.example.proyecto_semestral.ui.screens.admin.ManageRecipesScreen
import com.example.proyecto_semestral.ui.screens.admin.ManageUsersScreen
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
    var currentRoute by rememberSaveable {
        mutableStateOf(AppRoute.Login)
    }
    var currentUserName by rememberSaveable {
        mutableStateOf("Usuario")
    }
    var currentUserEmail by rememberSaveable {
        mutableStateOf("")
    }
    var currentProfileImageUri by rememberSaveable {
        mutableStateOf("")
    }
    var currentUserPhone by rememberSaveable {
        mutableStateOf("")
    }
    var currentBusinessName by rememberSaveable {
        mutableStateOf("")
    }
    var currentBusinessType by rememberSaveable {
        mutableStateOf(DEFAULT_BUSINESS_TYPE)
    }
    var currentBusinessSize by rememberSaveable {
        mutableStateOf(DEFAULT_BUSINESS_SIZE)
    }
    var currentInterests by remember {
        mutableStateOf(emptySet<String>())
    }
    var selectedExploreFilter by rememberSaveable { mutableStateOf(ContentFilter.All) }
    var exploreSearchQuery by rememberSaveable { mutableStateOf("") }
    var selectedRecipeId by rememberSaveable { mutableStateOf("") }
    var selectedVideoId by rememberSaveable { mutableStateOf("") }
    var selectedCourseId by rememberSaveable { mutableStateOf("") }
    var videoReturnRoute by rememberSaveable { mutableStateOf(AppRoute.Explore) }

    fun openSession(user: AppUser) {
        InMemorySessionRepository.start(user)
        currentUserName = user.name
        currentUserEmail = user.email
        currentBusinessName = user.businessName
        currentBusinessType = user.businessType.ifBlank { DEFAULT_BUSINESS_TYPE }
        currentBusinessSize = user.businessSize.ifBlank { DEFAULT_BUSINESS_SIZE }
        currentRoute = if (user.role == UserRole.ADMIN) AppRoute.AdminUsers else AppRoute.Home
    }

    fun login(email: String, password: String): Boolean {
        return when (val result = InMemoryUserRepository.authenticate(email, password)) {
            is LoginResult.Success -> {
                openSession(result.user)
                true
            }
            LoginResult.Blocked, LoginResult.InvalidCredentials -> false
        }
    }

    fun logout() {
        InMemorySessionRepository.clear()
        currentUserName = "Usuario"
        currentUserEmail = ""
        currentProfileImageUri = ""
        currentUserPhone = ""
        currentBusinessName = ""
        currentBusinessType = DEFAULT_BUSINESS_TYPE
        currentBusinessSize = DEFAULT_BUSINESS_SIZE
        currentInterests = emptySet()
        currentRoute = AppRoute.Login
    }

    AnimatedContent(
        targetState = currentRoute,
        transitionSpec = {
            val movingForward = targetState.ordinal >= initialState.ordinal
            val enterDirection = if (movingForward) 1 else -1
            val exitDirection = -enterDirection

            (slideInHorizontally(
                animationSpec = tween(durationMillis = 320),
                initialOffsetX = { fullWidth -> enterDirection * fullWidth / 5 }
            ) + fadeIn(animationSpec = tween(durationMillis = 240))) togetherWith
                (slideOutHorizontally(
                    animationSpec = tween(durationMillis = 320),
                    targetOffsetX = { fullWidth -> exitDirection * fullWidth / 5 }
                ) + fadeOut(animationSpec = tween(durationMillis = 220)))
        },
        label = "Transición entre pantallas"
    ) { route ->
    when (route) {
        AppRoute.Home -> {
            if (InMemorySessionRepository.currentUser != null) {
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
                    onLogout = ::logout
                )
            } else {
                LoginScreen(
                    onLoginSuccess = ::login,
                    onRegisterClick = { currentRoute = AppRoute.Register }
                )
            }
        }

        AppRoute.Explore -> {
            if (InMemorySessionRepository.currentUser != null) {
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
                    onLoginSuccess = ::login,
                    onRegisterClick = { currentRoute = AppRoute.Register }
                )
            }
        }

        AppRoute.Register -> RegisterScreen(
            onRegisterSuccess = { name, email, password, businessName, businessType, businessSize ->
                val user = InMemoryUserRepository.register(
                    name = name,
                    email = email,
                    password = password,
                    businessName = businessName,
                    businessType = businessType,
                    businessSize = businessSize
                )
                if (user != null) {
                    currentInterests = emptySet()
                    openSession(user)
                }
                user != null
            },
            onLoginClick = { currentRoute = AppRoute.Login }
        )

        AppRoute.Login -> LoginScreen(
            onLoginSuccess = ::login,
            onRegisterClick = { currentRoute = AppRoute.Register }
        )

        AppRoute.AdminUsers -> {
            if (InMemorySessionRepository.currentUser?.role == UserRole.ADMIN) {
                ManageUsersScreen(
                    onNavigateToRecipes = { currentRoute = AppRoute.AdminRecipes },
                    onLogout = ::logout
                )
            } else {
                currentRoute = AppRoute.Login
            }
        }

        AppRoute.AdminRecipes -> {
            if (InMemorySessionRepository.currentUser?.role == UserRole.ADMIN) {
                ManageRecipesScreen(
                    onNavigateToUsers = { currentRoute = AppRoute.AdminUsers },
                    onLogout = ::logout
                )
            } else {
                currentRoute = AppRoute.Login
            }
        }

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
            if (InMemorySessionRepository.currentUser != null) {
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
            if (InMemorySessionRepository.currentUser != null) {
                ProfileScreen(
                    initialName = currentUserName,
                    initialEmail = currentUserEmail,
                    initialProfileImageUri = currentProfileImageUri,
                    initialPhone = currentUserPhone,
                    initialBusinessName = currentBusinessName,
                    initialBusinessType = currentBusinessType,
                    initialBusinessSize = currentBusinessSize,
                    initialInterests = currentInterests,
                    onBack = { currentRoute = AppRoute.Home },
                    onSaveChanges = { name, email, profileImageUri, phone, businessName, businessType, businessSize, interests ->
                        currentUserName = name.trim().ifBlank { currentUserName }
                        currentUserEmail = email.trim()
                        currentProfileImageUri = profileImageUri
                        currentUserPhone = phone.trim()
                        currentBusinessName = businessName.trim()
                        currentBusinessType = businessType
                        currentBusinessSize = businessSize
                        currentInterests = interests
                        InMemorySessionRepository.currentUser?.let { user ->
                            InMemoryUserRepository.updateProfile(
                                id = user.id,
                                name = currentUserName,
                                email = currentUserEmail
                            )
                        }
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
}

private enum class AppRoute {
    Login,
    Register,
    AdminUsers,
    AdminRecipes,
    Home,
    Explore,
    Recipe,
    Video,
    Course,
    Contact,
    Profile
}
