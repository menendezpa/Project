package com.project.ui.screens.register

import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.text.input.PasswordVisualTransformation
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
 * Pantalla de registro.
 * @param navController Controlador de navegación.
 * @param viewModel ViewModel asociado a esta pantalla.
 * @see RegisterViewModel
 * @see RegisterUiState*/
@Composable
fun RegisterScreen(
    navController: NavController,
    viewModel: RegisterViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val username by viewModel.username.collectAsState()
    val password by viewModel.password.collectAsState()
    val confirmPassword by viewModel.confirmPassword.collectAsState()

    LaunchedEffect(uiState) {
        if (uiState is RegisterUiState.Success) {
            navController.navigate("home") {
                popUpTo("login") { inclusive = true }
            }
        }
    }

    Scaffold(
        topBar = {
            AppTopBar(
                title = "Registro",
                canNavigateBack = false,
                canLogOut = false
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (uiState is RegisterUiState.Loading) {
                CircularProgressIndicator()
            } else {
                RegisterForm(
                    username = username,
                    onUsernameChange = viewModel::updateUsername,
                    password = password,
                    onPasswordChange = viewModel::updatePassword,
                    confirmPassword = confirmPassword,
                    onConfirmPasswordChange = viewModel::updateConfirmPassword,
                    uiState = uiState,
                    onRegister = viewModel::register
                )
            }
        }
    }
}
/**
 * Composable para el formulario de registro.
 * @param username Nombre de usuario.
 * @param onUsernameChange Función para manejar cambios en el nombre de usuario.
 * @param password Contraseña.
 * @param onPasswordChange Función para manejar cambios en la contraseña.
 * @param confirmPassword Confirmación de contraseña.
 * @param onConfirmPasswordChange Función para manejar cambios en la confirmación de contraseña.
 * @param uiState Estado de la interfaz de usuario.
 * @param onRegister Función para manejar el registro.
 * @see RegisterUiState
 * */
@Composable
fun RegisterForm(
    username: String,
    onUsernameChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    confirmPassword: String,
    onConfirmPasswordChange: (String) -> Unit,
    uiState: RegisterUiState,
    onRegister: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        RegisterHeader()

        RegisterInputFields(
            username = username,
            onUsernameChange = onUsernameChange,
            password = password,
            onPasswordChange = onPasswordChange,
            confirmPassword = confirmPassword,
            onConfirmPasswordChange = onConfirmPasswordChange
        )

        RegisterErrorMessage(uiState)

        RegisterActions(onRegister)
    }
}
/**
 * Composable para la cabecera del formulario de registro.
 * @see RegisterForm
 * */
@Composable
fun RegisterHeader() {
    Box(
        modifier = Modifier
            .size(100.dp)
            .drawWithCache {
                val polygon = RoundedPolygon(
                    numVertices = 10,
                    radius = size.minDimension / 2,
                    centerX = size.width / 2,
                    centerY = size.height / 2,
                    rounding = CornerRounding(size.minDimension / 10f, smoothing = 0.1f)
                )
                val path = polygon.toPath().asComposePath()
                onDrawBehind { drawPath(path, color = Color.Black) }
            }
    )
}
/**
 * Composable para los campos de entrada del formulario de registro.
 * @param username Nombre de usuario.
 * @param onUsernameChange Función para manejar cambios en el nombre de usuario.
 * @param password Contraseña.
 * @param onPasswordChange Función para manejar cambios en la contraseña.
 * @param confirmPassword Confirmación de contraseña.
 * @param onConfirmPasswordChange Función para manejar cambios en la confirmación de contraseña.
 * @see RegisterForm
 * */
@Composable
fun RegisterInputFields(
    username: String,
    onUsernameChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    confirmPassword: String,
    onConfirmPasswordChange: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        OutlinedTextField(
            value = username,
            onValueChange = onUsernameChange,
            label = { Text("Usuario") },
            modifier = Modifier.fillMaxWidth(0.8f)
        )
        OutlinedTextField(
            value = password,
            onValueChange = onPasswordChange,
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth(0.8f),
            visualTransformation = PasswordVisualTransformation()
        )
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = onConfirmPasswordChange,
            label = { Text("Confirmar Contraseña") },
            modifier = Modifier.fillMaxWidth(0.8f),
            visualTransformation = PasswordVisualTransformation()
        )
    }
}
/**
 * Composable para mostrar mensajes de error en el formulario de registro.
 * @param uiState Estado de la interfaz de usuario.
 * @see RegisterForm
 * */
@Composable
fun RegisterErrorMessage(uiState: RegisterUiState) {
    if (uiState is RegisterUiState.Error) {
        Text(
            text = uiState.message,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
/**
 * Composable para las acciones del formulario de registro.
 * @param onRegister Función para manejar el registro.
 * @see RegisterForm
 * */
@Composable
fun RegisterActions(onRegister: () -> Unit) {
    Button(
        onClick = onRegister,
        modifier = Modifier.fillMaxWidth(0.8f)
    ) {
        Text("Registrarse")
    }
}
