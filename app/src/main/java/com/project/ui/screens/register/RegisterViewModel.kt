package com.project.ui.screens.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.data.User
import com.project.data.repository.AuthRepository
import com.project.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel para la pantalla de registro
 * @param auth Repositorio de autenticación
 * @param userRepository Repositorio de usuario
 * */
class RegisterViewModel(
    private val auth: AuthRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    /**
     * Estado de la interfaz de usuario*/
    private val _uiState = MutableStateFlow<RegisterUiState>(RegisterUiState.Idle)
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    /**
     * Estados de la interfaz de usuario*/
    private val _username = MutableStateFlow("")
    val username: StateFlow<String> = _username.asStateFlow()

    /**
     * Contraseña actual*/
    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    /**
     * Confirmar contraseña*/
    private val _confirmPassword = MutableStateFlow("")
    val confirmPassword: StateFlow<String> = _confirmPassword.asStateFlow()

    /**
     * Actualiza el nombre de usuario
     * @param username Nuevo nombre de usuario*/
    fun updateUsername(username: String) {
        _username.value = username
    }

    /**
     * Actualiza la contraseña
     * @param password Nueva contraseña*/
    fun updatePassword(password: String) {
        _password.value = password
    }

    /**
     * Actualiza la confirmación de contraseña
     * @param confirmPassword Nueva confirmación de contraseña*/
    fun updateConfirmPassword(confirmPassword: String) {
        _confirmPassword.value = confirmPassword
    }

    /**
     * Registra un nuevo usuario
     * */
    fun register() {
        /**
         * Verifica que los campos no estén vacíos
         * */
        if (_username.value.isBlank() || _password.value.isBlank() || _confirmPassword.value.isBlank()) {
            _uiState.value = RegisterUiState.Error("Todos los campos son obligatorios")
            return
        }
        /**
         * Verifica que las contraseñas coincidan
         * */
        if (_password.value != _confirmPassword.value) {
            _uiState.value = RegisterUiState.Error("Las contraseñas no coinciden")
            return
        }
        /**
         * Inicia el proceso de registro
         * */
        _uiState.value = RegisterUiState.Loading
        /**
         * Registra al usuario en Firebase Authentication
         * */
        viewModelScope.launch {
            val result = auth.register(_username.value, _password.value)

            if (result.isSuccess) {
                val currentUser = auth.currentUser
                if (currentUser != null) {
                    userRepository.createUser(User(id = currentUser.uid, email = _username.value))
                    _uiState.value = RegisterUiState.Success
                } else {
                    _uiState.value = RegisterUiState.Error("Error al obtener el usuario registrado")
                }
            } else {
                _uiState.value = RegisterUiState.Error(
                    result.exceptionOrNull()?.message ?: "Error al registrar"
                )
            }
        }
    }

}

