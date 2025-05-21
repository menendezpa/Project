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
     * Estado de la interfaz de usuario
     * */
    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Idle)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
    /**
     * Estado de la lista de tareas
     * */
    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks.asStateFlow()
    /**
     * Estado de los días con tareas
     * */
    private val _daysWithTasks = MutableStateFlow<Set<LocalDate>>(emptySet())
    val daysWithTasks: StateFlow<Set<LocalDate>> = _daysWithTasks.asStateFlow()
    /**
     * Estado de las categorías
     * */
    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories.asStateFlow()
/**
 * Estado de las urgencias
 * */
    private val _urgencies = MutableStateFlow<List<Urgency>>(emptyList())
    val urgencies: StateFlow<List<Urgency>> = _urgencies.asStateFlow()

/**
 * Carga las tareas del usuario actual
 * */
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

/**
 * Cierra la sesión del usuario
 * */
    fun logOut(){
        viewModelScope.launch {
            authRepository.logOut()
        }
    }
/**
 * Carga las categorías y las urgencias
 * */
    fun getCategories() {
        viewModelScope.launch {
            _categories.value = userRepository.getCategories()
        }
    }
/**
 * Carga las urgencias
 * */
    fun getUrgencies() {
        viewModelScope.launch {
            _urgencies.value = userRepository.getUrgencies()
        }
    }
/**
 * Actualiza una tarea
 * @param task La tarea actualizada
 * */
    fun updateTask(task: Task) {
        viewModelScope.launch {
            userRepository.updateTask(task)
        }
    }
/**
 * Elimina una tarea
 * @param task La tarea a eliminar
 * */
    fun deleteTask(task: Task) {
        viewModelScope.launch {
            userRepository.deleteTask(task)
        }
    }
}

