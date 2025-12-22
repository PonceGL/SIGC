package com.poncegl.sigc.ui.feature.auth

import androidx.activity.compose.BackHandler
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.poncegl.sigc.ui.components.login.LoginContent
import com.poncegl.sigc.ui.components.login.SigcSnackbarVisuals
import com.poncegl.sigc.ui.components.login.SnackbarType
import com.poncegl.sigc.ui.feature.auth.login.AuthMode
import com.poncegl.sigc.ui.feature.auth.login.LoginUiEffect
import com.poncegl.sigc.ui.feature.auth.login.LoginUiEvent
import com.poncegl.sigc.ui.feature.auth.login.LoginViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    widthSizeClass: WindowWidthSizeClass,
    onLoginSuccess: () -> Unit,
    onNavigateToLegals: () -> Unit,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    BackHandler(enabled = state.authMode == AuthMode.RECOVER_PASSWORD) {
        viewModel.onEvent(LoginUiEvent.OnModeChange(AuthMode.LOGIN))
    }

    LaunchedEffect(state.isLoginSuccessful) {
        if (state.isLoginSuccessful) onLoginSuccess()
    }

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collectLatest { effect ->
            when (effect) {
                is LoginUiEffect.ShowError -> {
                    snackbarHostState.showSnackbar(
                        SigcSnackbarVisuals(
                            message = effect.message,
                            type = SnackbarType.ERROR,
                            withDismissAction = true
                        )
                    )
                }
                is LoginUiEffect.ShowMessage -> {
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

    LoginContent(
        state = state,
        widthSizeClass = widthSizeClass,
        snackbarHostState = snackbarHostState,
        onEvent = viewModel::onEvent,
        onNavigateToLegals = onNavigateToLegals
    )
}
