package com.project.di

import com.project.data.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.project.data.repository.UserRepository
import com.project.ui.screens.gym.GymViewModel
import com.project.ui.screens.home.HomeViewModel
//import com.project.data.repository.AuthRepository
import com.project.ui.screens.login.LoginViewModel
import com.project.ui.screens.register.RegisterViewModel
import com.project.ui.screens.social.SocialViewModel
import com.project.ui.screens.study.StudyViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
/**
 * Módulo para Firebase.
 * @see module
 * @see single*/
// Módulo para Firebase
val firebaseModule = module {
    single { FirebaseAuth.getInstance() }
    single { FirebaseFirestore.getInstance() }
}

/**
 * Módulo para el repositorio.
 * @see module
 * @see single*/
// Módulo para el repositorio
val repositoryModule = module {
    // Suponiendo que AuthRepository recibe una instancia de FirebaseAuth
    single { AuthRepository(get()) }
    single { UserRepository() }
}
/**
 * Módulo para los ViewModels.
 * @see module
 * @see viewModel*/
// Módulo para los ViewModels
val ViewModelModule = module {
    viewModel { LoginViewModel(get()) }
    viewModel { HomeViewModel(get(), get()) }
    viewModel {GymViewModel(get(), get())}
    viewModel { SocialViewModel(get(), get()) }
    viewModel { StudyViewModel(get(), get()) }
    viewModel { RegisterViewModel(get(), get()) }
}




/**
 * Lista total de módulos para Koin.
 * @see listOf*/
// Lista total de módulos para Koin
val appModules = listOf(firebaseModule, repositoryModule, ViewModelModule)