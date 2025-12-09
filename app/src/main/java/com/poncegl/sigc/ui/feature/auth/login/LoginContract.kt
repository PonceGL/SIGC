package com.poncegl.sigc.ui.feature.auth.login

enum class AuthMode {
    LOGIN, REGISTER
}

data class LoginUiState(
    val authMode: AuthMode = AuthMode.LOGIN,
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isEmailValid: Boolean = false,
    val isPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isLoginSuccessful: Boolean = false
)

sealed interface LoginUiEvent {
    data class OnModeChange(val mode: AuthMode) : LoginUiEvent
    data class OnNameChanged(val name: String) : LoginUiEvent
    data class OnEmailChanged(val email: String) : LoginUiEvent
    data class OnPasswordChanged(val password: String) : LoginUiEvent
    data class OnConfirmPasswordChanged(val value: String) : LoginUiEvent
    data object OnTogglePasswordVisibility : LoginUiEvent
    data object OnSubmitClicked : LoginUiEvent
    data object OnForgotPasswordClicked : LoginUiEvent
    data object OnGoogleSignInClicked : LoginUiEvent
    data object OnFacebookSignInClicked : LoginUiEvent
    data object OnErrorDismissed : LoginUiEvent
}
