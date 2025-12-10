package com.poncegl.sigc.ui.feature.auth.model

import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.poncegl.sigc.ui.components.login.LoginContent
import com.poncegl.sigc.ui.feature.auth.login.LoginUiEffect
import com.poncegl.sigc.ui.feature.auth.login.LoginViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun LoginScreen(
    widthSizeClass: WindowWidthSizeClass,
    onLoginSuccess: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.isLoginSuccessful) {
        if (state.isLoginSuccessful) onLoginSuccess()
    }

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collectLatest { effect ->
            when (effect) {
                is LoginUiEffect.ShowError -> {
                    snackbarHostState.showSnackbar(effect.message)
                }
            }
        }
    }

    LoginContent(
        state = state,
        widthSizeClass = widthSizeClass,
        snackbarHostState = snackbarHostState,
        onEvent = viewModel::onEvent
    )
}
