package com.example.proyecto_semestral.di

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.example.proyecto_semestral.data.local.AuthTokenStore
import com.example.proyecto_semestral.data.remote.AdminApi
import com.example.proyecto_semestral.data.remote.AuthApi
import com.example.proyecto_semestral.data.remote.CourseApi
import com.example.proyecto_semestral.data.remote.ContactApi
import com.example.proyecto_semestral.data.remote.NetworkProvider
import com.example.proyecto_semestral.data.remote.RecipeApi
import com.example.proyecto_semestral.data.repository.AdminRepositoryImpl
import com.example.proyecto_semestral.data.repository.AuthRepositoryImpl
import com.example.proyecto_semestral.data.repository.CourseRepositoryImpl
import com.example.proyecto_semestral.data.repository.ContactRepositoryImpl
import com.example.proyecto_semestral.data.repository.RecipeRepositoryImpl
import com.example.proyecto_semestral.domain.repository.AdminRepository
import com.example.proyecto_semestral.domain.repository.AuthRepository
import com.example.proyecto_semestral.domain.repository.CourseRepository
import com.example.proyecto_semestral.domain.repository.ContactRepository
import com.example.proyecto_semestral.domain.repository.RecipeRepository
import com.example.proyecto_semestral.ui.viewmodel.admin.AdminViewModel
import com.example.proyecto_semestral.ui.viewmodel.auth.AuthViewModel
import com.example.proyecto_semestral.ui.viewmodel.course.CourseListViewModel
import com.example.proyecto_semestral.ui.viewmodel.course.CourseDetailViewModel
import com.example.proyecto_semestral.ui.viewmodel.course.LessonViewModel
import com.example.proyecto_semestral.ui.viewmodel.contact.ContactViewModel
import com.example.proyecto_semestral.ui.viewmodel.recipe.RecipeDetailViewModel
import com.example.proyecto_semestral.ui.viewmodel.recipe.RecipeListViewModel

class AppContainer(context: Context) {
    private val tokenStore = AuthTokenStore(context.applicationContext)
    private val retrofit = NetworkProvider.createRetrofit(tokenStore)
    private val authApi = retrofit.create(AuthApi::class.java)
    private val recipeApi = retrofit.create(RecipeApi::class.java)
    private val courseApi = retrofit.create(CourseApi::class.java)
    private val contactApi = retrofit.create(ContactApi::class.java)
    private val adminApi = retrofit.create(AdminApi::class.java)

    private val authRepository: AuthRepository = AuthRepositoryImpl(
        tokenStore = tokenStore,
        api = authApi
    )
    private val recipeRepository: RecipeRepository = RecipeRepositoryImpl(recipeApi)
    private val courseRepository: CourseRepository = CourseRepositoryImpl(courseApi)
    private val contactRepository: ContactRepository = ContactRepositoryImpl(contactApi)
    private val adminRepository: AdminRepository = AdminRepositoryImpl(adminApi)

    val authViewModelFactory: ViewModelProvider.Factory =
        AuthViewModel.factory(authRepository)

    val recipeListViewModelFactory: ViewModelProvider.Factory =
        RecipeListViewModel.factory(recipeRepository)

    val courseListViewModelFactory: ViewModelProvider.Factory =
        CourseListViewModel.factory(courseRepository)

    val contactViewModelFactory: ViewModelProvider.Factory =
        ContactViewModel.factory(contactRepository)

    val adminViewModelFactory: ViewModelProvider.Factory =
        AdminViewModel.factory(adminRepository)

    fun recipeDetailViewModelFactory(recipeId: Long): ViewModelProvider.Factory =
        RecipeDetailViewModel.factory(recipeId, recipeRepository)

    fun courseDetailViewModelFactory(courseId: Long): ViewModelProvider.Factory =
        CourseDetailViewModel.factory(courseId, courseRepository)

    fun lessonViewModelFactory(
        courseId: Long,
        lessonId: Long
    ): ViewModelProvider.Factory =
        LessonViewModel.factory(courseId, lessonId, courseRepository)
}
