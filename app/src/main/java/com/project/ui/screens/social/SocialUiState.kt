package com.project.ui.screens.social

/**
 * Clase de estado que representa el estado de la pantalla de estudios.
 * @see SocialUiState
 * @see Idle
 * @see Loading
 * @see Success
 * @see Error
 * */
sealed class SocialUiState {
    data object Idle : SocialUiState()
    data object Loading : SocialUiState()
    data class Success(val message: String = "Tarea guardada correctamente") : SocialUiState()
    data class Error(val message: String) : SocialUiState()
}
