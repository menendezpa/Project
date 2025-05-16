package com.project.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.delay

class FakeAuthRepository : AuthRepository(FirebaseAuth.getInstance()) {

    override suspend fun login(email: String, password: String): Result<Unit> {
        delay(50)  // Simula un pequeño retraso
        return if (email == "test@example.com" && password == "password") {
            Result.success(Unit)
        } else {
            Result.failure(Exception("Credenciales inválidas"))
        }
    }

    override val currentUser: FirebaseUser? = null
}