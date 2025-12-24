package com.poncegl.sigc.ui.components.auth

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poncegl.sigc.core.constants.UI
import com.poncegl.sigc.ui.components.login.SigcSnackbarVisuals
import com.poncegl.sigc.ui.components.login.SnackbarType
import com.poncegl.sigc.ui.components.shared.SigcTextField
import com.poncegl.sigc.ui.feature.auth.newpassword.NewPasswordUiEvent
import com.poncegl.sigc.ui.feature.auth.newpassword.NewPasswordUiState
import com.poncegl.sigc.ui.theme.SIGCTheme
import com.poncegl.sigc.ui.theme.SigcTheme

@Composable
fun NewPasswordContent(
    state: NewPasswordUiState,
    snackbarHostState: SnackbarHostState,
    onEvent: (NewPasswordUiEvent) -> Unit,
) {
    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                val visuals = data.visuals as? SigcSnackbarVisuals
                val type = visuals?.type ?: SnackbarType.ERROR

                val (containerColor, contentColor) = when (type) {
                    SnackbarType.ERROR -> MaterialTheme.colorScheme.errorContainer to MaterialTheme.colorScheme.onErrorContainer
                    SnackbarType.SUCCESS -> SigcTheme.colors.success to MaterialTheme.colorScheme.onPrimary
                    SnackbarType.WARNING -> SigcTheme.colors.warning to MaterialTheme.colorScheme.onPrimary
                    SnackbarType.INFO -> MaterialTheme.colorScheme.primaryContainer to MaterialTheme.colorScheme.onPrimaryContainer
                }

                Snackbar(
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
                modifier = Modifier
                    .widthIn(max = UI.MAX_WIDTH.dp)
                    .fillMaxWidth(),
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
                    onValueChange = { onEvent(NewPasswordUiEvent.OnPasswordChanged(it)) },
                    label = "Nueva contraseña",
                    icon = Icons.Default.Lock,
                    keyboardType = KeyboardType.Password,
                    isPassword = true,
                    isPasswordVisible = state.isPasswordVisible,
                    onTogglePassword = { onEvent(NewPasswordUiEvent.OnTogglePasswordVisibility) },
                    enabled = !state.isLoading
                )

                Spacer(modifier = Modifier.height(16.dp))

                SigcTextField(
                    value = state.confirmPassword,
                    onValueChange = { onEvent(NewPasswordUiEvent.OnConfirmPasswordChanged(it)) },
                    label = "Confirmar nueva contraseña",
                    icon = Icons.Default.Lock,
                    keyboardType = KeyboardType.Password,
                    isPassword = true,
                    isPasswordVisible = state.isPasswordVisible,
                    onTogglePassword = { onEvent(NewPasswordUiEvent.OnTogglePasswordVisibility) },
                    enabled = !state.isLoading
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = { onEvent(NewPasswordUiEvent.OnSubmit) },
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

@Preview(name = "1. Mobile Light", showBackground = true)
@Composable
private fun PreviewNewPasswordLight() {
    SIGCTheme(darkTheme = false) {
        NewPasswordContent(
            state = NewPasswordUiState(isSubmitEnabled = true),
            snackbarHostState = remember { SnackbarHostState() },
            onEvent = {}
        )
    }
}

@Preview(name = "2. Mobile Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewNewPasswordDark() {
    SIGCTheme(darkTheme = true) {
        NewPasswordContent(
            state = NewPasswordUiState(isSubmitEnabled = true),
            snackbarHostState = remember { SnackbarHostState() },
            onEvent = {}
        )
    }
}

@Preview(
    name = "3. Tablet Dark",
    device = "spec:width=1280dp,height=800dp,dpi=240",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun PreviewNewPasswordTablet() {
    SIGCTheme(darkTheme = true) {
        NewPasswordContent(
            state = NewPasswordUiState(isLoading = true),
            snackbarHostState = remember { SnackbarHostState() },
            onEvent = {}
        )
    }
}
