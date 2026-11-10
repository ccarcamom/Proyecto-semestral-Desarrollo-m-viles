package com.example.proyecto_semestral.navigation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.example.proyecto_semestral.model.content.VideoCatalog
import com.example.proyecto_semestral.ui.components.BottomBarItem
import com.example.proyecto_semestral.ui.components.ContentFilter
import com.example.proyecto_semestral.ui.screens.course.CourseDetailScreen
import com.example.proyecto_semestral.ui.screens.explore.ExploreScreen
import com.example.proyecto_semestral.ui.screens.home.HomeScreen
import com.example.proyecto_semestral.ui.screens.recipe.RecipeDetailScreen
import com.example.proyecto_semestral.ui.screens.video.VideoScreen

@Composable
fun AppNavigation() {
    var currentRoute by rememberSaveable { mutableStateOf(AppRoute.Home) }
    var selectedExploreFilter by rememberSaveable { mutableStateOf(ContentFilter.All) }
    var exploreSearchQuery by rememberSaveable { mutableStateOf("") }
    var selectedRecipeId by rememberSaveable { mutableStateOf("") }
    var selectedVideoId by rememberSaveable { mutableStateOf("") }
    var selectedCourseId by rememberSaveable { mutableStateOf("") }

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
                onBottomBarItemSelected = ::navigateFromBottomBar,
                onSearch = { query ->
                    exploreSearchQuery = query
                    selectedExploreFilter = ContentFilter.All
                    currentRoute = AppRoute.Explore
                },
                onCourseClick = { courseId ->
                    selectedCourseId = courseId
                    currentRoute = AppRoute.Course
                },
                onRecipesClick = {
                    exploreSearchQuery = ""
                    selectedExploreFilter = ContentFilter.Recipes
                    currentRoute = AppRoute.Explore
                },
                onCoursesClick = {
                    exploreSearchQuery = ""
                    selectedExploreFilter = ContentFilter.Courses
                    currentRoute = AppRoute.Explore
                }
            )

            AppRoute.Explore -> ExploreScreen(
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
                recipeId = selectedRecipeId,
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

        }
    }
}

private enum class AppRoute {
    Home,
    Explore,
    Recipe,
    Course,
    Video
}
