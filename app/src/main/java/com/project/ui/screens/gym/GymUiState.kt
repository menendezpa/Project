package com.project.ui.screens.gym
/**
 * Estados posibles de la interfaz de usuario para la pantalla de inicio
 * @see Idle
 * @see Loading
 * @see Success
 * @see Error
 * */
sealed class GymUiState {
    /**
     * Estado inicial o en reposo
     * */
    data object Idle : GymUiState()
    /**
     * Estado de carga mientras se procesa la autenticación
     * */
    data object Loading : GymUiState()
    /**
     * Estado de éxito después de la autenticación
     * @param message Mensaje de éxito
     * */
    data class Success(val message: String = "Tarea guardada correctamente") : GymUiState()
    /**
     * Estado de error con mensaje
     * @param message Mensaje de error
     * */
    data class Error(val message: String) : GymUiState()
}
