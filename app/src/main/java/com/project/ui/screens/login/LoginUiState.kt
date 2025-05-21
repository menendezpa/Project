package com.project.ui.screens.login

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