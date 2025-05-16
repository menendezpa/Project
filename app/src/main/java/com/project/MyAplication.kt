package com.project

import android.app.Application
import android.os.Build
import com.google.android.libraries.places.api.Places
import com.google.firebase.FirebaseApp
import com.project.di.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class ProjectApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Inicializa Firebase
        FirebaseApp.initializeApp(this)

        Places.initialize(applicationContext,  getString(R.string.places_API))
        // Inicializa Koin (inyección de dependencias)
        startKoin {
            androidContext(this@ProjectApplication)
            modules(appModules)
        }
    }
}