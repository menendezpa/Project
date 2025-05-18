package com.project.ui.screens.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.data.Category
import com.project.data.Task
import com.project.data.Urgency
import com.project.data.repository.AuthRepository
import com.project.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

class HomeViewModel(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository // O TaskRepository
) : ViewModel() {
    /**
     * Estado de la interfaz de usuario*/
    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Idle)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
    /**
     * Estado de la lista de tareas*/
    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks.asStateFlow()
    /**
     * Estado de los días con tareas*/
    private val _daysWithTasks = MutableStateFlow<Set<LocalDate>>(emptySet())
    val daysWithTasks: StateFlow<Set<LocalDate>> = _daysWithTasks.asStateFlow()

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories.asStateFlow()

    private val _urgencies = MutableStateFlow<List<Urgency>>(emptyList())
    val urgencies: StateFlow<List<Urgency>> = _urgencies.asStateFlow()


    @RequiresApi(Build.VERSION_CODES.O)
    fun loadTasksForUser() {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading
            val userId = authRepository.currentUser?.uid
            if (userId != null) {
                val tasks = userRepository.getTasksForUser(userId)
                _tasks.value = tasks
                _daysWithTasks.value = tasks.mapNotNull { task ->
                    try {
                        LocalDate.parse(task.date) // Usa "yyyy-MM-dd", que es el que estás usando en Firestore
                    } catch (e: Exception) {
                        null // Si falla el parseo, lo ignora
                    }
                }.toSet()

                _uiState.value = HomeUiState.Success(tasks)
            }
        }
    }


    fun logOut(){
        viewModelScope.launch {
            authRepository.logOut()
        }
    }

    fun getCategories() {
        viewModelScope.launch {
            _categories.value = userRepository.getCategories()
        }
    }

    fun getUrgencies() {
        viewModelScope.launch {
            _urgencies.value = userRepository.getUrgencies()
        }
    }


}
/**
 * Estados de la pantalla de inicio*/
sealed class HomeUiState {
    data object Idle : HomeUiState()
    data object Loading : HomeUiState()
    data class Success(val tasks: List<Task>) : HomeUiState()
    data class Error(val message: String) : HomeUiState()

}
