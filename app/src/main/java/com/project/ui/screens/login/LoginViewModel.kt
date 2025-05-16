package com.project.ui.screens.login

import com.project.data.repository.AuthRepository
import android.util.Log
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel para gestionar la lógica de autenticación
 * @param auth Instancia de FirebaseAuth para autenticación
 */
open class LoginViewModel(private val auth: AuthRepository) : ViewModel() {

    /**
     * Estado de la interfaz de usuario
     */
    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    /**
     * Nombre de usuario actual
     */
    private val _username = MutableStateFlow("")
    val username = _username.asStateFlow()

    /**
     * Contraseña actual
     */
    private val _password = MutableStateFlow("")
    val password = _password.asStateFlow()

    /**
     * Actualiza el nombre de usuario
     * @param username Nuevo nombre de usuario
     */
    fun updateUsername(username: String) {
        _username.value = username
    }

    /**
     * Actualiza la contraseña
     * @param password Nueva contraseña
     */
    fun updatePassword(password: String) {
        _password.value = password
    }

    /**
     * Inicia sesión con el usuario y contraseña actuales
     */
    @VisibleForTesting
    fun login() {
        if (username.value.isEmpty() || password.value.isEmpty()) {
            _uiState.value = LoginUiState.Error("Usuario y contraseña son requeridos")
            return
        }

        _uiState.value = LoginUiState.Loading

        viewModelScope.launch {
            val result = auth.login(username.value, password.value)
            _uiState.value = if (result.isSuccess) {
                LoginUiState.Success
            } else {
                LoginUiState.Error(result.exceptionOrNull()?.message ?: "Error al iniciar sesión")
            }
        }
    }


    /**
     * Navega a la pantalla de registro o implementa el registro
     */
    fun register() {
        // Aquí puedes navegar a la pantalla de registro o implementar el registro directamente
    }

    /**
     * Gestiona el proceso de recuperación de contraseña
     */
    fun resetPassword() {
        // Implementación para recuperar contraseña
    }

    /**
     * Verifica si el usuario ya está autenticado
     * @return true si hay un usuario actualmente autenticado
     */
    fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    /**
     * Recordar que el usuario está logueado*/
    fun rememberUser() {
        // Implementación para recordar al usuario
    }
}

/**
 * Estados posibles de la interfaz de usuario para la pantalla de login
 */
sealed class LoginUiState {
    /**
     * Estado inicial o en reposo
     */
    data object Idle : LoginUiState()

    /**
     * Estado de carga mientras se procesa la autenticación
     */
    data object Loading : LoginUiState()

    /**
     * Estado de éxito después de la autenticación
     */
    data object Success : LoginUiState()

    /**
     * Estado de error con mensaje
     * @param message Mensaje de error
     */
    data class Error(val message: String) : LoginUiState()
}