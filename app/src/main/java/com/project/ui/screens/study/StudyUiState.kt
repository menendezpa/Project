package com.project.ui.screens.study
/**
 * Clase que representa el estado de la pantalla de estudios.
 * Puede ser Idle, Loading, Success o Error.
 * @see Idle
 * @see Loading
 * @see Success
 * @see Error
 */
sealed class StudyUiState {
    data object Idle : StudyUiState()
    data object Loading : StudyUiState()
    data class Success(val message: String = "Tarea guardada correctamente") : StudyUiState()
    data class Error(val message: String) : StudyUiState()
}