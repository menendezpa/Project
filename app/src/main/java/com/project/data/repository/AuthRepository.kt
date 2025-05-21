package com.project.data.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import kotlinx.coroutines.tasks.await

/**
 * Repositorio de autenticación para gestionar el inicio de sesión, registro,
 * cierre de sesión y estado de autenticación del usuario con Firebase.
 *
 * @param auth Instancia de FirebaseAuth utilizada para realizar operaciones de autenticación.
 * @see FirebaseAuth
 */
open class AuthRepository(private val auth: FirebaseAuth) {

    /**
     * Obtiene el usuario actual autenticado, si existe.
     *
     * Esta propiedad es `open` para permitir la sobreescritura en tests.
     */
    open val currentUser get() = auth.currentUser

    /**
     * Inicia sesión con correo electrónico y contraseña utilizando Firebase Authentication.
     *
     * @param email Correo electrónico del usuario.
     * @param password Contraseña del usuario.
     * @return Un objeto [Result] que contiene éxito si la operación fue exitosa o una excepción detallada si falló.
     */
    open suspend fun login(email: String, password: String): Result<Unit> {
        return try {
            // Intenta iniciar sesión de forma asíncrona.
            auth.signInWithEmailAndPassword(email, password).await()

            // Log de depuración para desarrollo (evita mostrar contraseñas reales en producción).
            Log.d("LogIn", "Logged in as ${email}/${password}")

            Result.success(Unit)

        } catch (e: FirebaseAuthException) {
            // Manejo específico según el tipo de error
            when (e.errorCode) {
                "ERROR_INVALID_CREDENTIAL" ->
                    Result.failure(Exception("El Usuario y/o la contraseña son incorrectos, inténtelo de nuevo"))

                "ERROR_INVALID_EMAIL" ->
                    Result.failure(Exception("Correo electrónico mal formado"))

                else -> {
                    Log.e("LogIn", e.errorCode)
                    Result.failure(Exception("Error de autenticación: ${e.message}"))
                }
            }
        }
    }

    /**
     * Cierra la sesión del usuario actual.
     *
     * Este método invoca `signOut()` sobre FirebaseAuth, eliminando la sesión activa.
     */
    fun logOut() {
        auth.signOut()
    }

    /**
     * Registra un nuevo usuario con correo electrónico y contraseña.
     *
     * @param email Correo electrónico del nuevo usuario.
     * @param password Contraseña del nuevo usuario.
     * @return Un objeto [Result] que contiene éxito o error según el resultado.
     */
    suspend fun register(email: String, password: String): Result<Unit> {
        return try {
            auth.createUserWithEmailAndPassword(email, password).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Verifica si hay un usuario actualmente autenticado.
     *
     * @return `true` si un usuario ha iniciado sesión, `false` en caso contrario.
     */
    open fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }
}
