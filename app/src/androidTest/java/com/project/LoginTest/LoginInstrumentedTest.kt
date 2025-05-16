package com.project.loginTest

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.project.ui.screens.login.LoginScreen
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock

class LoginInstrumentedTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun loginScreenTest() {
        // En este test usamos un NavController real obtenido a través de rememberNavController.
        composeTestRule.setContent {
            LoginScreen(navController = rememberNavController())
        }
        // Verificamos que el botón "Iniciar Sesión" se muestra en la UI.
        composeTestRule.onNodeWithText("Iniciar Sesión").assertIsDisplayed()
    }

    @Test
    fun iniciarSesionTest() {
        // Creamos un NavController mockeado para poder inyectarlo en la UI.
        val navController = mock(NavController::class.java)
        composeTestRule.setContent {
            LoginScreen(navController = navController)
        }
        // Realizamos el click sobre el botón "Iniciar Sesión".
        composeTestRule.onNodeWithText("Iniciar Sesión").performClick()
        // En este ejemplo no verificamos la navegación explícitamente,
        // pero podrías usar Mockito.verify(navController).navigate("ruta_destino")
        // si tu LoginScreen invoca algo como navController.navigate("...") en respuesta al click.
    }
}