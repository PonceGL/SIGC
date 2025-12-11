package com.poncegl.sigc.ui.feature.auth.login

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

    private val nameRegex = Regex("^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ\\s]{3,}$")

    fun onEvent(event: LoginUiEvent) {
        when (event) {
            is LoginUiEvent.OnModeChange -> {
                _uiState.update {
                    val newState = it.copy(authMode = event.mode)
                    newState.copy(isSubmitEnabled = calculateValidation(newState))
                }
            }

            is LoginUiEvent.OnNameChanged -> {
                _uiState.update {
                    val newState = it.copy(name = event.name)
                    newState.copy(isSubmitEnabled = calculateValidation(newState))
                }
            }

            is LoginUiEvent.OnEmailChanged -> {
                val isValid = Patterns.EMAIL_ADDRESS.matcher(event.email).matches()
                _uiState.update {
                    val newState = it.copy(email = event.email, isEmailValid = isValid)
                    newState.copy(isSubmitEnabled = calculateValidation(newState))
                }
            }

            is LoginUiEvent.OnPasswordChanged -> {
                _uiState.update {
                    val newState = it.copy(password = event.password)
                    newState.copy(isSubmitEnabled = calculateValidation(newState))
                }
            }

            is LoginUiEvent.OnConfirmPasswordChanged -> {
                _uiState.update {
                    val newState = it.copy(confirmPassword = event.value)
                    newState.copy(isSubmitEnabled = calculateValidation(newState))
                }
            }

            is LoginUiEvent.OnTogglePasswordVisibility -> {
                _uiState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
            }

            is LoginUiEvent.OnSubmitClicked -> {
                if (_uiState.value.authMode == AuthMode.LOGIN) performLogin() else performRegister()
            }

            is LoginUiEvent.OnGoogleSignInClicked -> {
                performGoogleSignIn(event.context)
            }
            // TODO: Implementar luego los clicks de Facebook/Olvidé contraseña
            else -> {}
        }
    }

    private fun performGoogleSignIn(context: android.content.Context) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val result = authRepository.signInWithGoogle(context)

            handleAuthResult(result)
        }
    }

    private fun calculateValidation(state: LoginUiState): Boolean {
        if (state.isLoading) return false

        return if (state.authMode == AuthMode.LOGIN) {

            state.isEmailValid && state.password.isNotBlank()
        } else {

            val isNameValid = state.name.matches(nameRegex)
            val isEmailValid = state.isEmailValid
            val isPasswordValid = state.password.isNotEmpty()
            val doPasswordsMatch = state.password == state.confirmPassword

            isNameValid && isEmailValid && isPasswordValid && doPasswordsMatch
        }
    }

    private fun performLogin() {
        val currentState = _uiState.value

        if (!currentState.isEmailValid || currentState.password.isBlank()) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, isSubmitEnabled = false) }
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
            _uiState.update { it.copy(isLoading = true, isSubmitEnabled = false) }
            val result = authRepository.signUp(
                currentState.name,
                currentState.email,
                currentState.password
            )
            handleAuthResult(result)
        }
    }

    private fun handleAuthResult(result: Result<Any>) {
        _uiState.update { it.copy(isLoading = false) }

        result.fold(
            onSuccess = {
                _uiState.update { it.copy(isLoginSuccessful = true) }
            },
            onFailure = { exception ->
                sendErrorEffect(exception.message ?: "Ocurrió un error inesperado")

                _uiState.update { it.copy(isSubmitEnabled = calculateValidation(it)) }
            }
        )
    }

    private fun sendErrorEffect(message: String) {
        viewModelScope.launch {
            _uiEffect.send(LoginUiEffect.ShowError(message))
        }
    }
}