package com.project.ui.screens.study

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.data.Place
import com.project.data.Task
import com.project.data.Urgency
import com.project.data.repository.AuthRepository
import com.project.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class StudyViewModel(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<GymUiState>(GymUiState.Idle)
    val uiState: StateFlow<GymUiState> = _uiState.asStateFlow()

    private val _urgencies = MutableStateFlow<List<Urgency>>(emptyList())
    val urgencies: StateFlow<List<Urgency>> = _urgencies.asStateFlow()

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

    fun loadUrgencies() {
        viewModelScope.launch {
            _urgencies.value = userRepository.getUrgencies()
        }
    }


}

sealed class GymUiState {
    data object Idle : GymUiState()
    data object Loading : GymUiState()
    data class Success(val message: String = "Tarea guardada correctamente") : GymUiState()
    data class Error(val message: String) : GymUiState()
}
