package com.poncegl.sigc.ui.feature.auth.newpassword

sealed interface NewPasswordUiEvent {
    data class OnPasswordChanged(val value: String) : NewPasswordUiEvent
    data class OnConfirmPasswordChanged(val value: String) : NewPasswordUiEvent
    data object OnTogglePasswordVisibility : NewPasswordUiEvent
    data object OnSubmit : NewPasswordUiEvent
}

data class NewPasswordUiState(
    val password: String = "",
    val confirmPassword: String = "",
    val isPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val isSubmitEnabled: Boolean = false
)

sealed interface NewPasswordUiEffect {
    data class ShowError(val message: String) : NewPasswordUiEffect
    data class ShowMessage(val message: String) : NewPasswordUiEffect
}
