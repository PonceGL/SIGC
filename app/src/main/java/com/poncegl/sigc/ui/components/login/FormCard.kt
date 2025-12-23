package com.poncegl.sigc.ui.components.login

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.poncegl.sigc.ui.components.shared.SigcTextField
import com.poncegl.sigc.ui.feature.auth.login.AuthMode
import com.poncegl.sigc.ui.feature.auth.login.LoginUiEvent
import com.poncegl.sigc.ui.feature.auth.login.LoginUiState

@Composable
fun LoginFormCard(
    state: LoginUiState,
    onEvent: (LoginUiEvent) -> Unit,
    onNavigateToLegals: () -> Unit
) {
    Column(modifier = Modifier.padding(vertical = 20.dp)) {
        AnimatedVisibility(
            visible = state.authMode != AuthMode.RECOVER_PASSWORD,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            Column {
                AuthModeToggle(
                    currentMode = state.authMode,
                    onModeSelected = { onEvent(LoginUiEvent.OnModeChange(it)) }
                )
                Spacer(modifier = Modifier.height(24.dp))
            }
        }

        AnimatedVisibility(
            visible = state.authMode == AuthMode.RECOVER_PASSWORD,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            Column {
                Text(
                    text = "Recuperar Contraseña",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = "Ingresa tu correo electrónico para recibir las instrucciones.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 24.dp)
                )
            }
        }

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

        val showPasswordInput = (state.isEmailValid || state.authMode == AuthMode.REGISTER) &&
                state.authMode != AuthMode.RECOVER_PASSWORD

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

                    TextButton(
                        onClick = {
                            onNavigateToLegals()
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
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
        }

        Spacer(modifier = Modifier.height(24.dp))

        val buttonText = when (state.authMode) {
            AuthMode.LOGIN -> "Iniciar Sesión"
            AuthMode.REGISTER -> "Crear Cuenta"
            AuthMode.RECOVER_PASSWORD -> "Enviar correo"
        }

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
            enabled = state.isSubmitEnabled
        ) {
            Text(
                text = buttonText,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (state.authMode == AuthMode.LOGIN) {
            TextButton(
                onClick = { onEvent(LoginUiEvent.OnModeChange(AuthMode.RECOVER_PASSWORD)) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "¿Olvidaste tu contraseña?",
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else if (state.authMode == AuthMode.RECOVER_PASSWORD) {
            TextButton(
                onClick = { onEvent(LoginUiEvent.OnModeChange(AuthMode.LOGIN)) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Volver al inicio de sesión",
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
