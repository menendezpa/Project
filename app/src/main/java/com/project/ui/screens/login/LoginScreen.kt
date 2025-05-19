package com.project.ui.screens.login

import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.unit.dp
import androidx.graphics.shapes.CornerRounding
import androidx.graphics.shapes.RoundedPolygon
import androidx.graphics.shapes.toPath
import androidx.navigation.NavController
import com.project.ui.component.AppTopBar
import com.project.ui.component.UserOptionsButtons
import com.project.ui.component.ValidateUserComponent
import org.koin.androidx.compose.koinViewModel

/**
 * Pantalla de Login para gestionar los usuarios
 * @param navController Controlador de navegación
 * @param viewModel ViewModel para la lógica de login
 * @see LoginViewModel
 * */
@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val username by viewModel.username.collectAsState()
    val password by viewModel.password.collectAsState()

    // Verificar si el usuario ya está autenticado
    LaunchedEffect(key1 = Unit) {
        if (viewModel.isUserLoggedIn()) {
            navController.navigate("home") {
                popUpTo("login") { inclusive = true }
            }
        }
    }

    // Manejar el estado de éxito
    LaunchedEffect(key1 = uiState) {
        if (uiState is LoginUiState.Success) {
            navController.navigate("home") {
                popUpTo("login") { inclusive = true }
            }
        }
    }

    // Si está cargando, mostrar indicador de progreso
    if (uiState is LoginUiState.Loading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        // Si no está cargando, mostrar la pantalla de login
        Login(
            viewModel = viewModel,
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(),
            userValue = username,
            userOnChange = { viewModel.updateUsername(it) },
            passwordValue = password,
            passwordOnChange = { viewModel.updatePassword(it) },
            uiState = uiState,
            navController = navController
        )
    }
}

/**
 * Pantalla de Login para gestionar los usuarios
 * @param viewModel ViewModel para la gestión de login
 * @param modifier Modificador de composable
 * @param userValue Nombre del usuario
 * @param userOnChange Función de cambio de valor del nombre de usuario
 * @param passwordValue Contraseña del usuario
 * @param passwordOnChange Función de cambio de valor de contraseña de usuario
 * @param uiState Estado actual de la interfaz
 * @see Modifier
 * @see LoginViewModel
 * @see LoginUiState
 * */
@VisibleForTesting
@Composable
fun Login(
    viewModel: LoginViewModel,
    navController: NavController,
    modifier: Modifier = Modifier,
    /**Nombre del usuario*/
    userValue: String,
    /**Función de cambio de valor del nombre de usuario*/
    userOnChange: (String) -> Unit,
    /**Contraseña del usuario*/
    passwordValue: String,
    /**Función de cambio de valor de contraseña de usuario*/
    passwordOnChange: (String) -> Unit,
    /**Estado actual de la UI*/
    uiState: LoginUiState
) {
//    val context = LocalContext.current

    Scaffold (
        topBar = { AppTopBar(title = "Inicio de Sesión", canLogOut = false, canNavigateBack = false) }
    ) {innerPadding->
        Column(
            modifier = modifier.padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            /**
             * Se debe sustituir después para añadir el "logo" de la aplicación o un imágen a elegir*/
            Box(
                modifier = Modifier
                    .drawWithCache {
                        val roundedPolygon = RoundedPolygon(
                            numVertices = 10,
                            radius = size.minDimension / 2,
                            centerX = size.width / 2,
                            centerY = size.height / 2,
                            rounding = CornerRounding(
                                size.minDimension / 10f,
                                smoothing = 0.1f
                            )
                        )
                        val roundedPolygonPath = roundedPolygon.toPath().asComposePath()
                        onDrawBehind {
                            drawPath(roundedPolygonPath, color = Color.Black)
                        }
                    }
                    .size(100.dp)
            )

            /**
             * Campos para validar un usuario */
            ValidateUserComponent(userValue, userOnChange, passwordValue, passwordOnChange)

            // Mostrar mensaje de error si existe
            if (uiState is LoginUiState.Error) {
                Text(
                    text = uiState.message,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                HorizontalDivider(
                    modifier = Modifier
                        .width(340.dp),
                    thickness = 3.dp
                )
            }

            /**
             * Botones de Inicio, registo y recuperación de contraseña*/
            UserOptionsButtons(viewModel, navController)
        }
    }
}




