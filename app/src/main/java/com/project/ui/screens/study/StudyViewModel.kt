package com.project.ui.screens.study

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.data.Task
import com.project.data.Urgency
import com.project.data.repository.AuthRepository
import com.project.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel para la pantalla de estudios.
 * @param userRepository Repositorio de usuario para interactuar con la base de datos.
 * @param authRepository Repositorio de autenticación para obtener el usuario actual.
 * @see ViewModel
 * @see StudyUiState
 * @see MutableStateFlow*/
class StudyViewModel(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<StudyUiState>(StudyUiState.Idle)
    val uiState: StateFlow<StudyUiState> = _uiState.asStateFlow()

    private val _urgencies = MutableStateFlow<List<Urgency>>(emptyList())
    val urgencies: StateFlow<List<Urgency>> = _urgencies.asStateFlow()

    /**
     * Inserta una tarea en la base de datos.
     * @param task La tarea a insertar.
     * @see StudyUiState*/
    @RequiresApi(Build.VERSION_CODES.O)
    fun insertTask(task: Task) {
        viewModelScope.launch {
            _uiState.value = StudyUiState.Loading
            val userId = authRepository.currentUser?.uid
            if (userId != null) {
                userRepository.insertTask(userId, task)
                _uiState.value = StudyUiState.Success()
            }

        }
    }

    /**
     * Carga las urgencias desde el repositorio.
     * @see Urgency
     * @see StudyUiState*/
    fun loadUrgencies() {
        viewModelScope.launch {
            _urgencies.value = userRepository.getUrgencies()
        }
    }


}


