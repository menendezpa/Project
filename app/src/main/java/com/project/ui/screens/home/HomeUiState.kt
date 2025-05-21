package com.project.ui.screens.home

import com.project.data.Task


/**
 * Estados posibles de la interfaz de usuario para la pantalla de inicio
 * @see Idle
 * @see Loading
 * @see Success
 * @see Error
 * */
sealed class HomeUiState {
    /**
     * Estado inicial o en reposo
     * */
    data object Idle : HomeUiState()
    /**
     * Estado de carga mientras se procesa la autenticación
     * */
    data object Loading : HomeUiState()
    /**
     * Estado de éxito después de la autenticación
     * @param tasks Lista de tareas
     * */
    data class Success(val tasks: List<Task>) : HomeUiState()
    /**
     * Estado de error con mensaje
     * @param message Mensaje de error
     * */
    data class Error(val message: String) : HomeUiState()

}