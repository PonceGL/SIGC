package com.poncegl.sigc.ui.feature.auth

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.poncegl.sigc.ui.components.login.SigcSnackbarVisuals
import com.poncegl.sigc.ui.components.login.SnackbarType
import com.poncegl.sigc.ui.components.shared.SigcTextField
import com.poncegl.sigc.ui.feature.auth.newpassword.NewPasswordUiEffect
import com.poncegl.sigc.ui.feature.auth.newpassword.NewPasswordUiEvent
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

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                val visuals = data.visuals as? SigcSnackbarVisuals
                val type = visuals?.type ?: SnackbarType.ERROR

                val (containerColor, contentColor) = when (type) {
                    SnackbarType.ERROR -> MaterialTheme.colorScheme.errorContainer to MaterialTheme.colorScheme.onErrorContainer
                    SnackbarType.SUCCESS -> com.poncegl.sigc.ui.theme.SigcTheme.colors.success to MaterialTheme.colorScheme.onPrimary
                    SnackbarType.WARNING -> com.poncegl.sigc.ui.theme.SigcTheme.colors.warning to MaterialTheme.colorScheme.onPrimary
                    SnackbarType.INFO -> MaterialTheme.colorScheme.primaryContainer to MaterialTheme.colorScheme.onPrimaryContainer
                }

                androidx.compose.material3.Snackbar(
                    snackbarData = data,
                    containerColor = containerColor,
                    contentColor = contentColor,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.padding(16.dp)
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(20.dp)
                .imePadding(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Crea una nueva contraseña",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                )

                Text(
                    text = "Tu nueva contraseña debe ser diferente a la anterior.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),
                )

                SigcTextField(
                    value = state.password,
                    onValueChange = { viewModel.onEvent(NewPasswordUiEvent.OnPasswordChanged(it)) },
                    label = "Nueva contraseña",
                    icon = Icons.Default.Lock,
                    keyboardType = KeyboardType.Password,
                    isPassword = true,
                    isPasswordVisible = state.isPasswordVisible,
                    onTogglePassword = { viewModel.onEvent(NewPasswordUiEvent.OnTogglePasswordVisibility) },
                    enabled = !state.isLoading
                )

                Spacer(modifier = Modifier.height(16.dp))

                SigcTextField(
                    value = state.confirmPassword,
                    onValueChange = {
                        viewModel.onEvent(
                            NewPasswordUiEvent.OnConfirmPasswordChanged(
                                it
                            )
                        )
                    },
                    label = "Confirmar contraseña",
                    icon = Icons.Default.Lock,
                    keyboardType = KeyboardType.Password,
                    isPassword = true,
                    isPasswordVisible = state.isPasswordVisible,
                    onTogglePassword = { viewModel.onEvent(NewPasswordUiEvent.OnTogglePasswordVisibility) },
                    enabled = !state.isLoading
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = { viewModel.onEvent(NewPasswordUiEvent.OnSubmit) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    enabled = state.isSubmitEnabled,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(
                        text = "Cambiar Contraseña",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            if (state.isLoading) {
                CircularProgressIndicator()
            }
        }
    }
}
