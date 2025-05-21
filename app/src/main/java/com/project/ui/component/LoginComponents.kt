package com.project.ui.component

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.DefaultTintColor
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.project.ui.screens.login.LoginViewModel
import com.project.ui.theme.bottomShape
import com.project.ui.theme.topShape

/**
 * Muestra el formulario de inicio de sesión.
 * @param userValue Valor del campo de usuario.
 * @param userOnChange Función para manejar cambios en el campo de usuario.
 * @param passwordValue Valor del campo de contraseña.
 * @param passwordOnChange Función para manejar cambios en el campo de contraseña.
 * @see OutlinedTextField
 * @see Text
 * @see Modifier
 * @see Column*/
@Composable
fun ValidateUserComponent(
    userValue: String,
    userOnChange: (String) -> Unit,
    passwordValue: String,
    passwordOnChange: (String) -> Unit,
) {
    var passwordVisibilityState by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        OutlinedTextField(
            singleLine = true,
            modifier = Modifier.width(280.dp),
            shape = topShape,
            colors = TextFieldDefaults.colors(),
            value = userValue,
            onValueChange = userOnChange,
            label = { Text("Usuario", style = MaterialTheme.typography.labelLarge) },
            textStyle = MaterialTheme.typography.bodyLarge,
        )

        OutlinedTextField(
            singleLine = true,
            modifier = Modifier.width(280.dp),
            shape = bottomShape,
            value = passwordValue,
            colors = TextFieldDefaults.colors(),
            textStyle = MaterialTheme.typography.bodyLarge,
            onValueChange = passwordOnChange,
            visualTransformation = if (passwordVisibilityState) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                val image = if (passwordVisibilityState)
                    Icons.Filled.Visibility
                else Icons.Filled.VisibilityOff

                // Please provide localized description for accessibility services
                val description =
                    if (passwordVisibilityState) "Hide password" else "Show password"

                IconButton(onClick = { passwordVisibilityState = !passwordVisibilityState }) {
                    Icon(imageVector = image, description)
                }
            },
            label = {
                Text(
                    "Contraseña",
                    modifier = Modifier.background(
                        shape = RectangleShape,
                        color = DefaultTintColor
                    ),
                    style = MaterialTheme.typography.labelLarge
                )
            },
            placeholder = { Text("Contraseña") }
        )

    }
}

/**
 * Muestra los botones de inicio de sesión y registro.
 * @param viewModel Modelo de vista para manejar la lógica del inicio de sesión.
 * @param navController Controlador de navegación para navegar entre pantallas.
 * @see ElevatedButton
 * @see Text
 * @see Modifier
 * */
@SuppressLint("VisibleForTests")
@Composable
fun UserOptionsButtons(viewModel: LoginViewModel, navController: NavController) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ElevatedButton(
            onClick = { viewModel.login() },
            modifier = Modifier
                .width(280.dp),
            shape = topShape,
            colors = ButtonDefaults.buttonColors()
        ) {
            Text("Iniciar Sesión")
        }

        ElevatedButton(
            onClick = { navController.navigate("register") },
            modifier = Modifier.width(280.dp),
            shape = bottomShape,
            colors = ButtonDefaults.buttonColors(),
            elevation = ButtonDefaults.buttonElevation(8.dp)
        ) {
            Text("Registrate")
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(align = Alignment.Center),
        verticalAlignment = Alignment.Bottom
    ) {
        TextButton(onClick = { viewModel.resetPassword() }) {
            Text("¿Olvidaste tu contraseña?")
        }
    }
}

