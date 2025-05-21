package com.project

import android.app.Application
import android.os.Build
import com.google.android.libraries.places.api.Places
import com.google.firebase.FirebaseApp
import com.project.di.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

/**
 * Clase de aplicación personalizada.
 *
 * Esta clase extiende de [Application] y se encarga de inicializar las librerías y frameworks
 * principales que se utilizarán durante toda la vida de la app.
 *
 * - Inicializa Firebase para autenticación y servicios backend.
 * - Inicializa la API de Google Places para autocompletado y selección de ubicaciones.
 * - Inicializa Koin para la gestión de dependencias a nivel global.
 *
 * Debe estar declarada en el archivo `AndroidManifest.xml` como:
 *
 * ```xml
 * <application
 *     android:name=".ProjectApplication"
 *     ... >
 *     ...
 * </application>
 * ```
 *
 * @see Application
 * @see FirebaseApp
 * @see Places
 * @see startKoin
 */
class ProjectApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Inicializa Firebase, necesario para todas las operaciones que involucren autenticación,
        // Firestore, Cloud Messaging, etc.
        FirebaseApp.initializeApp(this)

        // Inicializa Google Places con la API Key definida en res/values/strings.xml
        Places.initialize(
            applicationContext,
            getString(R.string.places_API)
        )

        // Inicializa Koin (librería de inyección de dependencias).
        // Se pasa el contexto de la aplicación y los módulos definidos en `appModules`.
        startKoin {
            androidContext(this@ProjectApplication)
            modules(appModules)
        }
    }
}
