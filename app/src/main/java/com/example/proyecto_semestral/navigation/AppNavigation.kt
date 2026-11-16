package com.example.proyecto_semestral.navigation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proyecto_semestral.model.content.VideoCatalog
import com.example.proyecto_semestral.ui.components.BottomBarItem
import com.example.proyecto_semestral.ui.components.ContentFilter
import com.example.proyecto_semestral.ui.screens.course.CourseDetailScreen
import com.example.proyecto_semestral.ui.screens.admin.AdminScreen
import com.example.proyecto_semestral.ui.screens.auth.LoginScreen
import com.example.proyecto_semestral.ui.screens.auth.RegisterScreen
import com.example.proyecto_semestral.ui.screens.explore.ExploreScreen
import com.example.proyecto_semestral.ui.screens.home.HomeScreen
import com.example.proyecto_semestral.ui.screens.recipe.RecipeDetailScreen
import com.example.proyecto_semestral.ui.screens.video.VideoScreen
import com.example.proyecto_semestral.ui.viewmodel.auth.AuthViewModel

@Composable
fun AppNavigation(
    authViewModelFactory: ViewModelProvider.Factory,
    recipeListViewModelFactory: ViewModelProvider.Factory,
    recipeDetailViewModelFactory: (Long) -> ViewModelProvider.Factory,
    adminViewModelFactory: ViewModelProvider.Factory
) {
    val authViewModel: AuthViewModel = viewModel(
        factory = authViewModelFactory
    )
    val authState by authViewModel.uiState.collectAsState()
    var authRoute by rememberSaveable { mutableStateOf(AuthRoute.Login) }
    var currentRoute by rememberSaveable { mutableStateOf(AppRoute.Home) }
    var selectedExploreFilter by rememberSaveable { mutableStateOf(ContentFilter.All) }
    var exploreSearchQuery by rememberSaveable { mutableStateOf("") }
    var selectedRecipeId by rememberSaveable { mutableStateOf<Long?>(null) }
    var selectedVideoId by rememberSaveable { mutableStateOf("") }
    var selectedCourseId by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(authState.isAuthenticated) {
        if (!authState.isAuthenticated) currentRoute = AppRoute.Home
    }

    if (authState.isRestoring) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    if (!authState.isAuthenticated) {
        when (authRoute) {
            AuthRoute.Login -> LoginScreen(
                isLoading = authState.isLoading,
                serverError = authState.error,
                onLogin = authViewModel::login,
                onRegisterClick = {
                    authViewModel.clearError()
                    authRoute = AuthRoute.Register
                },
                onInputChanged = authViewModel::clearError
            )

            AuthRoute.Register -> RegisterScreen(
                isLoading = authState.isLoading,
                serverError = authState.error,
                onRegister = authViewModel::register,
                onLoginClick = {
                    authViewModel.clearError()
                    authRoute = AuthRoute.Login
                },
                onInputChanged = authViewModel::clearError
            )
        }
        return
    }

    fun navigateFromBottomBar(item: BottomBarItem) {
        currentRoute = when (item) {
            BottomBarItem.Home -> AppRoute.Home
            BottomBarItem.Explore -> AppRoute.Explore
        }
    }

    AnimatedContent(
        targetState = currentRoute,
        transitionSpec = {
            val direction = if (targetState.ordinal >= initialState.ordinal) 1 else -1
            (slideInHorizontally(tween(320)) { direction * it / 5 } + fadeIn(tween(240))) togetherWith
                (slideOutHorizontally(tween(320)) { -direction * it / 5 } + fadeOut(tween(220)))
        },
        label = "Transición entre pantallas"
    ) { route ->
        when (route) {
            AppRoute.Home -> HomeScreen(
                viewModelFactory = recipeListViewModelFactory,
                onBottomBarItemSelected = ::navigateFromBottomBar,
                onSearch = { query ->
                    exploreSearchQuery = query
                    selectedExploreFilter = ContentFilter.All
                    currentRoute = AppRoute.Explore
                },
                onRecipesClick = {
                    exploreSearchQuery = ""
                    selectedExploreFilter = ContentFilter.Recipes
                    currentRoute = AppRoute.Explore
                },
                onRecipeClick = { recipeId ->
                    selectedRecipeId = recipeId
                    currentRoute = AppRoute.Recipe
                },
                onCoursesClick = {
                    exploreSearchQuery = ""
                    selectedExploreFilter = ContentFilter.Courses
                    currentRoute = AppRoute.Explore
                },
                onLogoutClick = authViewModel::logout,
                isAdmin = authState.isAdmin,
                onAdminClick = { currentRoute = AppRoute.Admin }
            )

            AppRoute.Explore -> ExploreScreen(
                viewModelFactory = recipeListViewModelFactory,
                onBottomBarItemSelected = ::navigateFromBottomBar,
                selectedFilter = selectedExploreFilter,
                searchQuery = exploreSearchQuery,
                onFilterSelected = { selectedExploreFilter = it },
                onSearchQueryChange = { exploreSearchQuery = it },
                onRecipeClick = { recipeId ->
                    selectedRecipeId = recipeId
                    currentRoute = AppRoute.Recipe
                },
                onCourseClick = { courseId ->
                    selectedCourseId = courseId
                    currentRoute = AppRoute.Course
                }
            )

            AppRoute.Recipe -> RecipeDetailScreen(
                recipeId = selectedRecipeId ?: 0L,
                viewModelFactory = recipeDetailViewModelFactory(selectedRecipeId ?: 0L),
                onBack = { currentRoute = AppRoute.Explore }
            )

            AppRoute.Course -> CourseDetailScreen(
                courseId = selectedCourseId,
                onBack = { currentRoute = AppRoute.Explore },
                onLessonClick = { videoId ->
                    selectedVideoId = videoId
                    currentRoute = AppRoute.Video
                }
            )

            AppRoute.Video -> VideoScreen(
                videoId = selectedVideoId,
                onBack = { currentRoute = AppRoute.Course },
                onNextLesson = {
                    VideoCatalog.nextLesson(selectedVideoId)?.let { selectedVideoId = it.id }
                },
                onReturnToCourse = { currentRoute = AppRoute.Course }
            )

            AppRoute.Admin -> if (authState.isAdmin) {
                AdminScreen(
                    viewModelFactory = adminViewModelFactory,
                    adminEmail = authState.user?.email.orEmpty(),
                    onBack = { currentRoute = AppRoute.Home }
                )
            } else {
                LaunchedEffect(Unit) {
                    currentRoute = AppRoute.Home
                }
            }

        }
    }
}

private enum class AppRoute {
    Home,
    Explore,
    Recipe,
    Course,
    Video,
    Admin
}

private enum class AuthRoute {
    Login,
    Register
}
