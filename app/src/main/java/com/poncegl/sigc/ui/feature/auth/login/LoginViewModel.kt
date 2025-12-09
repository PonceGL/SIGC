package com.poncegl.sigc.ui.feature.auth.login

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poncegl.sigc.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val _uiEffect = Channel<LoginUiEffect>()
    val uiEffect = _uiEffect.receiveAsFlow()

    fun onEvent(event: LoginUiEvent) {
        when (event) {
            is LoginUiEvent.OnModeChange -> {
                _uiState.update { it.copy(authMode = event.mode) }
            }
            is LoginUiEvent.OnNameChanged -> {
                _uiState.update { it.copy(name = event.name) }
            }
            is LoginUiEvent.OnEmailChanged -> {
                val isValid = Patterns.EMAIL_ADDRESS.matcher(event.email).matches()
                _uiState.update { it.copy(email = event.email, isEmailValid = isValid) }
            }
            is LoginUiEvent.OnPasswordChanged -> {
                _uiState.update { it.copy(password = event.password) }
            }
            is LoginUiEvent.OnConfirmPasswordChanged -> {
                _uiState.update { it.copy(confirmPassword = event.value) }
            }
            is LoginUiEvent.OnTogglePasswordVisibility -> {
                _uiState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
            }
            is LoginUiEvent.OnSubmitClicked -> {
                Log.d("LoginViewModel", "Submit clicked in mode: ${_uiState.value.authMode}")
                if (_uiState.value.authMode == AuthMode.LOGIN) performLogin() else performRegister()
            }
            // TODO: Implementar luego los clicks de Google/FB/Olvidé contraseña
            else -> {}
        }
    }

    private fun performLogin() {
        Log.d("LoginViewModel", "====================================================")
        Log.d("LoginViewModel", "Performing login for email: ${_uiState.value.email}")
        Log.d("LoginViewModel", "Performing login for password: ${_uiState.value.password}")
        Log.d("LoginViewModel", "====================================================")
        val currentState = _uiState.value
        if (!currentState.isEmailValid || currentState.password.isBlank()) {
            sendErrorEffect("Por favor verifica los campos.")
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val result = authRepository.login(currentState.email, currentState.password)
            handleAuthResult(result)
        }
    }

    private fun performRegister() {
        val currentState = _uiState.value

        if (currentState.password != currentState.confirmPassword) {
            sendErrorEffect("Las contraseñas no coinciden")
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val result = authRepository.signUp(currentState.email, currentState.password)
            handleAuthResult(result)
        }
    }

    private fun handleAuthResult(result: Result<Any>) {
        _uiState.update { it.copy(isLoading = false) } // Apagamos loading primero

        result.fold(
            onSuccess = {
                _uiState.update { it.copy(isLoginSuccessful = true) }
            },
            onFailure = { exception ->
                sendErrorEffect(exception.message ?: "Ocurrió un error inesperado")
            }
        )
    }

    private fun sendErrorEffect(message: String) {
        viewModelScope.launch {
            _uiEffect.send(LoginUiEffect.ShowError(message))
        }
    }
}