package com.poncegl.sigc.ui.components.login

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poncegl.sigc.ui.components.shared.SigcTextField
import com.poncegl.sigc.ui.feature.auth.login.AuthMode
import com.poncegl.sigc.ui.feature.auth.login.LoginUiEvent
import com.poncegl.sigc.ui.feature.auth.login.LoginUiState
import com.poncegl.sigc.ui.theme.SIGCTheme

@Composable
fun LoginContent(
    state: LoginUiState,
    widthSizeClass: WindowWidthSizeClass,
    snackbarHostState: SnackbarHostState,
    onEvent: (LoginUiEvent) -> Unit
) {
    val scrollState = rememberScrollState()

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                LoginHeader()
                Spacer(modifier = Modifier.height(48.dp))

                if (widthSizeClass == WindowWidthSizeClass.Compact) {

                    LoginFormCard(state, onEvent)
                    Spacer(modifier = Modifier.height(24.dp))
                    SocialButtons(
                        onGoogleClick = { onEvent(LoginUiEvent.OnGoogleSignInClicked) },
                        onFacebookClick = { onEvent(LoginUiEvent.OnFacebookSignInClicked) },
                    )
                } else {

                    Row(
                        modifier = Modifier.widthIn(max = 900.dp),
                        horizontalArrangement = Arrangement.spacedBy(48.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Box(modifier = Modifier.weight(1f)) {
                            LoginFormCard(state, onEvent)
                        }
                        Column(
                            modifier = Modifier
                                .weight(0.8f)
                                .padding(top = 16.dp),
                            verticalArrangement = Arrangement.Center
                        ) {
                            SocialButtons(
                                onGoogleClick = { onEvent(LoginUiEvent.OnGoogleSignInClicked) },
                                onFacebookClick = { onEvent(LoginUiEvent.OnFacebookSignInClicked) },
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }

            if (state.isLoading) {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
private fun LoginFormCard(
    state: LoginUiState,
    onEvent: (LoginUiEvent) -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            AuthModeToggle(
                currentMode = state.authMode,
                onModeSelected = { onEvent(LoginUiEvent.OnModeChange(it)) }
            )

            Spacer(modifier = Modifier.height(24.dp))


            AnimatedVisibility(
                visible = state.authMode == AuthMode.REGISTER,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column {
                    SigcTextField(
                        value = state.name,
                        onValueChange = { onEvent(LoginUiEvent.OnNameChanged(it)) },
                        label = "Nombre completo",
                        icon = Icons.Default.Person,
                        enabled = !state.isLoading
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }


            SigcTextField(
                value = state.email,
                onValueChange = { onEvent(LoginUiEvent.OnEmailChanged(it)) },
                label = "Correo electrónico",
                icon = Icons.Default.Email,
                keyboardType = KeyboardType.Email,
                enabled = !state.isLoading
            )

            val showPasswordInput = state.isEmailValid || state.authMode == AuthMode.REGISTER

            AnimatedVisibility(
                visible = showPasswordInput,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column {
                    Spacer(modifier = Modifier.height(16.dp))
                    SigcTextField(
                        value = state.password,
                        onValueChange = { onEvent(LoginUiEvent.OnPasswordChanged(it)) },
                        label = if (state.authMode == AuthMode.REGISTER) "Crea una contraseña" else "Contraseña",
                        icon = Icons.Default.Lock,
                        keyboardType = KeyboardType.Password,
                        isPassword = true,
                        isPasswordVisible = state.isPasswordVisible,
                        onTogglePassword = { onEvent(LoginUiEvent.OnTogglePasswordVisibility) },
                        enabled = !state.isLoading
                    )

                    if (state.authMode == AuthMode.REGISTER) {
                        Spacer(modifier = Modifier.height(16.dp))
                        SigcTextField(
                            value = state.confirmPassword,
                            onValueChange = { onEvent(LoginUiEvent.OnConfirmPasswordChanged(it)) },
                            label = "Confirmar contraseña",
                            icon = Icons.Default.Lock,
                            keyboardType = KeyboardType.Password,
                            isPassword = true,
                            isPasswordVisible = state.isPasswordVisible,
                            onTogglePassword = { onEvent(LoginUiEvent.OnTogglePasswordVisibility) },
                            enabled = !state.isLoading
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Al registrarte aceptas nuestros Términos de Servicio y Política de Privacidad.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            val buttonText =
                if (state.authMode == AuthMode.LOGIN) "Iniciar Sesión" else "Crear Cuenta"
            Button(
                onClick = { onEvent(LoginUiEvent.OnSubmitClicked) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                enabled = !state.isLoading && showPasswordInput
            ) {
                Text(
                    text = buttonText,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            if (state.authMode == AuthMode.LOGIN) {
                Spacer(modifier = Modifier.height(16.dp))
                TextButton(
                    onClick = { onEvent(LoginUiEvent.OnForgotPasswordClicked) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        "¿Olvidaste tu contraseña?",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}


@Preview(
    name = "1. Mobile Light - Login",
    device = "id:pixel_5",
    apiLevel = 31,
    showBackground = true
)
@Composable
private fun PreviewLoginScreenLight() {
    SIGCTheme(darkTheme = false) {
        LoginContent(
            state = LoginUiState(email = "test@email.com", isEmailValid = true),
            widthSizeClass = WindowWidthSizeClass.Compact,
            snackbarHostState = remember { SnackbarHostState() },
            onEvent = {}
        )
    }
}

@Preview(
    name = "2. Mobile Dark - Register",
    device = "id:pixel_5",
    apiLevel = 31,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun PreviewLoginScreenDarkRegister() {
    SIGCTheme(darkTheme = true) {
        LoginContent(
            state = LoginUiState(authMode = AuthMode.REGISTER),
            widthSizeClass = WindowWidthSizeClass.Compact,
            snackbarHostState = remember { SnackbarHostState() },
            onEvent = {}
        )
    }
}

@Preview(
    name = "3. Tablet Dark",
    device = "spec:width=1280dp,height=800dp,dpi=240",
    apiLevel = 31,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun PreviewLoginScreenTablet() {
    SIGCTheme(darkTheme = true) {
        LoginContent(
            state = LoginUiState(authMode = AuthMode.LOGIN, isEmailValid = true),
            widthSizeClass = WindowWidthSizeClass.Expanded,
            snackbarHostState = remember { SnackbarHostState() },
            onEvent = {}
        )
    }
}