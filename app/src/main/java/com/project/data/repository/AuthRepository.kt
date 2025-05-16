package com.project.data.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import kotlinx.coroutines.tasks.await

open class AuthRepository(private val auth: FirebaseAuth) {

    // Se evalúa solo cuando se acceda a currentUser
    open val currentUser get() = auth.currentUser


    open suspend fun login(email: String, password: String): Result<Unit> {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            Log.d("LogIn", "Logged in as ${email}/${password}")
            Result.success(Unit)
        } catch (e: FirebaseAuthException) {
            when (e.errorCode) {
                "ERROR_INVALID_CREDENTIAL" -> {
                    Result.failure(Exception("El Usuario y/o la contraseña son incorrectos, inténtelo de nuevo"))
                }

                "ERROR_INVALID_EMAIL" -> {
                    Result.failure(Exception("Correo electrónico mal formado"))
                }
                else -> {
                    Log.e("LogIn", e.errorCode)
                    Result.failure(Exception("Error de autenticación: ${e.message}"))
                }
            }
        }

    }


    fun logOut() {
        auth.signOut()
    }

    suspend fun register(email: String, password: String): Result<Unit> {
        return try {
            auth.createUserWithEmailAndPassword(email, password).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    open fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }
}