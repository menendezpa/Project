package com.project.ui.screens.gym

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.data.Place
import com.project.data.Task
import com.project.data.repository.AuthRepository
import com.project.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GymViewModel(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<GymUiState>(GymUiState.Idle)
    val uiState: StateFlow<GymUiState> = _uiState.asStateFlow()

    fun insertTask(task: Task) {
        viewModelScope.launch {
            _uiState.value = GymUiState.Loading
            val userId = authRepository.currentUser?.uid
            if (userId != null) {
                userRepository.insertTask(userId, task)
                _uiState.value = GymUiState.Success()
            }

        }
    }

}

sealed class GymUiState {
    data object Idle : GymUiState()
    data object Loading : GymUiState()
    data class Success(val message: String = "Tarea guardada correctamente") : GymUiState()
    data class Error(val message: String) : GymUiState()
}
