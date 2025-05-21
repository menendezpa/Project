package com.project.ui.screens.register
/**
 * Clase de estado para la pantalla de registro.
 * @see RegisterUiState
 * @see Idle
 * @see Loading
 * @see Success
 * @see Error
 * */
sealed class RegisterUiState {
    /**
     * Estado inicial o en reposo
     * */
    object Idle : RegisterUiState()
    /**
     * Estado de carga mientras se procesa el registro
     * */
    object Loading : RegisterUiState()
    /**
     * Estado de éxito después del registro
     * */
    object Success : RegisterUiState()
    /**
     * Estado de error con mensaje
     * @param message Mensaje de error
     * */
    data class Error(val message: String) : RegisterUiState()
}

