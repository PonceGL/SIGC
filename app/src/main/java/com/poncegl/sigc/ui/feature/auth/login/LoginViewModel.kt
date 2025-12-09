package com.poncegl.sigc.ui.feature.auth.login

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poncegl.sigc.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onEvent(event: LoginUiEvent) {
        when (event) {
            is LoginUiEvent.OnModeChange -> {
                _uiState.update { it.copy(authMode = event.mode, errorMessage = null) }
            }
            is LoginUiEvent.OnNameChanged -> {
                _uiState.update { it.copy(name = event.name) }
            }
            is LoginUiEvent.OnEmailChanged -> {
                val isValid = Patterns.EMAIL_ADDRESS.matcher(event.email).matches()
                _uiState.update { it.copy(email = event.email, isEmailValid = isValid, errorMessage = null) }
            }
            is LoginUiEvent.OnPasswordChanged -> {
                _uiState.update { it.copy(password = event.password, errorMessage = null) }
            }
            is LoginUiEvent.OnConfirmPasswordChanged -> {
                _uiState.update { it.copy(confirmPassword = event.value) }
            }
            is LoginUiEvent.OnTogglePasswordVisibility -> {
                _uiState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
            }
            is LoginUiEvent.OnSubmitClicked -> {
                if (_uiState.value.authMode == AuthMode.LOGIN) performLogin() else performRegister()
            }
            // TODO: Implementar luego los clicks de Google/FB/Olvidé contraseña
            else -> {}
        }
    }

    private fun performLogin() {
        val currentState = _uiState.value
        if (!currentState.isEmailValid || currentState.password.isBlank()) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val result = authRepository.login(currentState.email, currentState.password)
            handleAuthResult(result)
        }
    }

    private fun performRegister() {
        val currentState = _uiState.value

        if (currentState.password != currentState.confirmPassword) {
            _uiState.update { it.copy(errorMessage = "Las contraseñas no coinciden") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val result = authRepository.signUp(currentState.email, currentState.password)
            handleAuthResult(result)
        }
    }

    private fun handleAuthResult(result: Result<Any>) {
        _uiState.update { state ->
            result.fold(
                onSuccess = { state.copy(isLoading = false, isLoginSuccessful = true) },
                onFailure = { state.copy(isLoading = false, errorMessage = it.message) }
            )
        }
    }
}