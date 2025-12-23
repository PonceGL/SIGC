package com.poncegl.sigc.ui.feature.auth

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.poncegl.sigc.ui.components.auth.NewPasswordContent
import com.poncegl.sigc.ui.components.login.SigcSnackbarVisuals
import com.poncegl.sigc.ui.components.login.SnackbarType
import com.poncegl.sigc.ui.feature.auth.newpassword.NewPasswordUiEffect
import com.poncegl.sigc.ui.feature.auth.newpassword.NewPasswordViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun NewPasswordScreen(
    viewModel: NewPasswordViewModel = hiltViewModel(),
    onPasswordChangedSuccess: () -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            onPasswordChangedSuccess()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collectLatest { effect ->
            when (effect) {
                is NewPasswordUiEffect.ShowError -> {
                    snackbarHostState.showSnackbar(
                        SigcSnackbarVisuals(
                            message = effect.message,
                            type = SnackbarType.ERROR,
                            withDismissAction = true
                        )
                    )
                }
                is NewPasswordUiEffect.ShowMessage -> {
                    snackbarHostState.showSnackbar(
                        SigcSnackbarVisuals(
                            message = effect.message,
                            type = SnackbarType.SUCCESS,
                            withDismissAction = true
                        )
                    )
                }
            }
        }
    }

    NewPasswordContent(
        state = state,
        snackbarHostState = snackbarHostState,
        onEvent = viewModel::onEvent
    )
}
