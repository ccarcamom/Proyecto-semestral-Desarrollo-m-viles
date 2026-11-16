package com.example.proyecto_semestral.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.example.proyecto_semestral.ui.components.BottomBarItem
import com.example.proyecto_semestral.ui.components.ContentFilter
import com.example.proyecto_semestral.ui.screens.admin.AdminScreen
import com.example.proyecto_semestral.ui.screens.auth.LoginScreen
import com.example.proyecto_semestral.ui.screens.auth.RegisterScreen
import com.example.proyecto_semestral.ui.screens.contact.ContactScreen
import com.example.proyecto_semestral.ui.screens.course.CourseDetailScreen
import com.example.proyecto_semestral.ui.screens.explore.ExploreScreen
import com.example.proyecto_semestral.ui.screens.home.HomeScreen
import com.example.proyecto_semestral.ui.screens.recipe.RecipeDetailScreen
import com.example.proyecto_semestral.ui.screens.video.VideoScreen
import com.example.proyecto_semestral.ui.viewmodel.auth.AuthViewModel

private object Routes {
    const val Login = "login"
    const val Register = "register"
    const val Home = "home"
    const val Explore = "explore"
    const val Admin = "admin"
    const val Contact = "contact"
    const val Recipe = "recipe/{recipeId}"
    const val Course = "course/{courseId}"
    const val Lesson = "course/{courseId}/lesson/{lessonId}"

    fun recipe(id: Long) = "recipe/$id"
    fun course(id: Long) = "course/$id"
    fun lesson(courseId: Long, lessonId: Long) = "course/$courseId/lesson/$lessonId"
}

@Composable
fun AppNavigation(
    authViewModelFactory: ViewModelProvider.Factory,
    recipeListViewModelFactory: ViewModelProvider.Factory,
    courseListViewModelFactory: ViewModelProvider.Factory,
    recipeDetailViewModelFactory: (Long) -> ViewModelProvider.Factory,
    courseDetailViewModelFactory: (Long) -> ViewModelProvider.Factory,
    lessonViewModelFactory: (Long, Long) -> ViewModelProvider.Factory,
    contactViewModelFactory: ViewModelProvider.Factory,
    adminViewModelFactory: ViewModelProvider.Factory
) {
    val authViewModel: AuthViewModel = viewModel(factory = authViewModelFactory)
    val authState by authViewModel.uiState.collectAsState()

    if (authState.isRestoring) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    val navController = rememberNavController()
    var selectedFilter by rememberSaveable { mutableStateOf(ContentFilter.All) }
    var searchQuery by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(authState.isAuthenticated) {
        val target = if (authState.isAuthenticated) Routes.Home else Routes.Login
        if (navController.currentDestination?.route != target) {
            navController.navigate(target) {
                popUpTo(navController.graph.findStartDestination().id) { inclusive = true }
                launchSingleTop = true
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = if (authState.isAuthenticated) Routes.Home else Routes.Login
    ) {
        composable(Routes.Login) {
            LoginScreen(
                isLoading = authState.isLoading,
                serverError = authState.error,
                onLogin = authViewModel::login,
                onRegisterClick = {
                    authViewModel.clearError()
                    navController.navigate(Routes.Register)
                },
                onInputChanged = authViewModel::clearError
            )
        }

        composable(Routes.Register) {
            RegisterScreen(
                isLoading = authState.isLoading,
                serverError = authState.error,
                onRegister = authViewModel::register,
                onLoginClick = {
                    authViewModel.clearError()
                    navController.popBackStack()
                },
                onInputChanged = authViewModel::clearError
            )
        }

        composable(Routes.Home) {
            HomeScreen(
                viewModelFactory = recipeListViewModelFactory,
                courseViewModelFactory = courseListViewModelFactory,
                onBottomBarItemSelected = { navController.navigateBottom(it) },
                onSearch = { query ->
                    searchQuery = query
                    selectedFilter = ContentFilter.All
                    navController.navigate(Routes.Explore)
                },
                onRecipesClick = {
                    searchQuery = ""
                    selectedFilter = ContentFilter.Recipes
                    navController.navigate(Routes.Explore)
                },
                onCoursesClick = {
                    searchQuery = ""
                    selectedFilter = ContentFilter.Courses
                    navController.navigate(Routes.Explore)
                },
                onRecipeClick = { navController.navigate(Routes.recipe(it)) },
                onLogoutClick = authViewModel::logout,
                isAdmin = authState.isAdmin,
                onAdminClick = { navController.navigate(Routes.Admin) },
                onContactClick = { navController.navigate(Routes.Contact) },
                onCourseClick = { navController.navigate(Routes.course(it)) }
            )
        }

        composable(Routes.Explore) {
            ExploreScreen(
                viewModelFactory = recipeListViewModelFactory,
                courseViewModelFactory = courseListViewModelFactory,
                onBottomBarItemSelected = { navController.navigateBottom(it) },
                selectedFilter = selectedFilter,
                searchQuery = searchQuery,
                onFilterSelected = { selectedFilter = it },
                onSearchQueryChange = { searchQuery = it },
                onRecipeClick = { navController.navigate(Routes.recipe(it)) },
                onCourseClick = { navController.navigate(Routes.course(it)) }
            )
        }

        composable(
            route = Routes.Recipe,
            arguments = listOf(navArgument("recipeId") { type = NavType.LongType })
        ) { entry ->
            val id = entry.arguments?.getLong("recipeId") ?: return@composable
            RecipeDetailScreen(
                recipeId = id,
                viewModelFactory = recipeDetailViewModelFactory(id),
                onBack = navController::popBackStack
            )
        }

        composable(
            route = Routes.Course,
            arguments = listOf(navArgument("courseId") { type = NavType.LongType })
        ) { entry ->
            val courseId = entry.arguments?.getLong("courseId") ?: return@composable
            CourseDetailScreen(
                viewModelFactory = courseDetailViewModelFactory(courseId),
                onBack = navController::popBackStack,
                onLessonClick = { lessonId ->
                    navController.navigate(Routes.lesson(courseId, lessonId))
                }
            )
        }

        composable(
            route = Routes.Lesson,
            arguments = listOf(
                navArgument("courseId") { type = NavType.LongType },
                navArgument("lessonId") { type = NavType.LongType }
            )
        ) { entry ->
            val courseId = entry.arguments?.getLong("courseId") ?: return@composable
            val lessonId = entry.arguments?.getLong("lessonId") ?: return@composable
            VideoScreen(
                viewModelFactory = lessonViewModelFactory(courseId, lessonId),
                onBack = navController::popBackStack,
                onNextLesson = { nextId ->
                    navController.navigate(Routes.lesson(courseId, nextId)) {
                        popUpTo(Routes.course(courseId))
                    }
                },
                onReturnToCourse = {
                    navController.popBackStack(Routes.course(courseId), inclusive = false)
                }
            )
        }

        composable(Routes.Admin) {
            if (authState.isAdmin) {
                AdminScreen(
                    viewModelFactory = adminViewModelFactory,
                    adminEmail = authState.user?.email.orEmpty(),
                    onBack = navController::popBackStack
                )
            } else {
                LaunchedEffect(Unit) { navController.popBackStack() }
            }
        }

        composable(Routes.Contact) {
            ContactScreen(
                viewModelFactory = contactViewModelFactory,
                userId = authState.user?.id,
                initialEmail = authState.user?.email.orEmpty(),
                onBottomBarItemSelected = { navController.navigateBottom(it) }
            )
        }
    }
}

private fun NavHostController.navigateBottom(item: BottomBarItem) {
    when (item) {
        BottomBarItem.Home -> {
            if (!popBackStack(Routes.Home, inclusive = false)) {
                navigate(Routes.Home) {
                    popUpTo(graph.findStartDestination().id)
                    launchSingleTop = true
                }
            }
        }
        BottomBarItem.Explore -> navigate(Routes.Explore) {
            popUpTo(Routes.Home)
            launchSingleTop = true
        }
        BottomBarItem.Contact -> navigate(Routes.Contact) {
            popUpTo(Routes.Home)
            launchSingleTop = true
        }
    }
}
