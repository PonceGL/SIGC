package com.poncegl.sigc.ui.feature.auth.newpassword

import androidx.lifecycle.SavedStateHandle
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
class NewPasswordViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val oobCode: String? = savedStateHandle.get<String>("oobCode")

    private val _uiState = MutableStateFlow(NewPasswordUiState())
    val uiState: StateFlow<NewPasswordUiState> = _uiState.asStateFlow()

    private val _uiEffect = Channel<NewPasswordUiEffect>()
    val uiEffect = _uiEffect.receiveAsFlow()

    init {
        if (oobCode.isNullOrBlank()) {
            sendErrorEffect("Enlace inválido o incompleto.")
        }
    }

    fun onEvent(event: NewPasswordUiEvent) {
        when (event) {
            is NewPasswordUiEvent.OnPasswordChanged -> {
                _uiState.update {
                    it.copy(
                        password = event.value,
                        isSubmitEnabled = calculateValidation(it.copy(password = event.value))
                    )
                }
            }

            is NewPasswordUiEvent.OnConfirmPasswordChanged -> {
                _uiState.update {
                    it.copy(
                        confirmPassword = event.value,
                        isSubmitEnabled = calculateValidation(it.copy(confirmPassword = event.value))
                    )
                }
            }

            is NewPasswordUiEvent.OnTogglePasswordVisibility -> {
                _uiState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
            }

            is NewPasswordUiEvent.OnSubmit -> {
                performChangePassword()
            }
        }
    }

    private fun calculateValidation(state: NewPasswordUiState): Boolean {
        if (state.isLoading) return false
        return state.password.isNotEmpty() &&
                state.password == state.confirmPassword &&
                !oobCode.isNullOrBlank()
    }

    private fun performChangePassword() {
        val currentState = _uiState.value
        val code = oobCode ?: return

        if (currentState.password != currentState.confirmPassword) {
            sendErrorEffect("Las contraseñas no coinciden")
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, isSubmitEnabled = false) }

            val result = authRepository.confirmPasswordReset(code, currentState.password)

            _uiState.update { it.copy(isLoading = false) }

            result.fold(
                onSuccess = {
                    _uiState.update { it.copy(isSuccess = true) }
                    _uiEffect.send(NewPasswordUiEffect.ShowMessage("Contraseña actualizada correctamente."))
                },
                onFailure = { exception ->
                    sendErrorEffect(exception.message ?: "Error al actualizar contraseña")
                    _uiState.update { it.copy(isSubmitEnabled = true) }
                }
            )
        }
    }

    private fun sendErrorEffect(message: String) {
        viewModelScope.launch {
            _uiEffect.send(NewPasswordUiEffect.ShowError(message))
        }
    }
}
