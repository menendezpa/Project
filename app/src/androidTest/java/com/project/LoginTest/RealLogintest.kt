package com.project.loginTest

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.project.data.repository.AuthRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class RealLogintest  {

    private lateinit var auth: FirebaseAuth
    private lateinit var authRepository: AuthRepository

    // Credenciales de prueba: asegúrate de que estos datos existan en Firebase Authentication
    private val invalidEmail = "testuser@example.com"
    private val invalidPassword = "testpassword"

    private val email = "menendezpablo51@gmail.com"
    private val password = "123456"



    @Before
    fun setUp() {
        // Obtén el contexto de la aplicación y asegúrate de que Firebase esté inicializado
        val context: Context = ApplicationProvider.getApplicationContext()
        FirebaseApp.initializeApp(context)
        auth = FirebaseAuth.getInstance()
        authRepository = AuthRepository(auth)
    }

    @Test
    fun testRealLogin_IncorrectCredentials() = runBlocking {
        // Intenta el login con las credenciales reales
        val result = authRepository.login(invalidEmail, invalidPassword)

        // Verifica que el resultado sea exitoso
        assertTrue("Login falló: ${result.exceptionOrNull()?.message}", result.isFailure)
    }

    @Test
    fun testRealLogin_EmptyCredentials(): Unit = runBlocking {
        // Espera que se lance la excepción al pasar valores vacíos
        assertThrows(IllegalArgumentException::class.java) {
            runBlocking {
                authRepository.login("", "")
            }
        }
    }


    @Test
    fun testRealLogin_CorrectCredentials() = runBlocking {
        // Intenta el login con las credenciales reales
        val result = authRepository.login(email, password)
        assertTrue( "Login correcto: ${result.exceptionOrNull()?.message}", result.isSuccess)
    }
}