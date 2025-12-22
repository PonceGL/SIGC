package com.poncegl.sigc.ui.components.login

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poncegl.sigc.ui.feature.auth.login.AuthMode
import com.poncegl.sigc.ui.feature.auth.login.LoginUiEvent
import com.poncegl.sigc.ui.feature.auth.login.LoginUiState
import com.poncegl.sigc.ui.theme.SIGCTheme
import com.poncegl.sigc.ui.theme.SigcTheme

@Composable
fun LoginContent(
    state: LoginUiState,
    widthSizeClass: WindowWidthSizeClass,
    snackbarHostState: SnackbarHostState,
    onEvent: (LoginUiEvent) -> Unit,
    onNavigateToLegals: () -> Unit,
) {
    val scrollState = rememberScrollState()

    val context = LocalContext.current

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
                .padding(vertical = 10.dp)
                .imePadding(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
            ) {

                LoginHeader(state = state)

                Spacer(modifier = Modifier.height(24.dp))

                if (widthSizeClass == WindowWidthSizeClass.Compact) {
                    LoginFormCard(state, onEvent, onNavigateToLegals)
                    Spacer(modifier = Modifier.height(24.dp))
                    if (state.authMode == AuthMode.LOGIN) {
                        SocialButtons(
                            onGoogleClick = {
                                onEvent(LoginUiEvent.OnGoogleSignInClicked(context))
                            },
                            onFacebookClick = { onEvent(LoginUiEvent.OnFacebookSignInClicked) },
                        )
                    }
                } else {

                    Row(
                        modifier = Modifier.widthIn(max = 900.dp),
                        horizontalArrangement = Arrangement.spacedBy(48.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Box(modifier = Modifier.weight(1f)) {
                            LoginFormCard(state, onEvent, onNavigateToLegals)
                        }
                        Column(
                            modifier = Modifier
                                .weight(0.8f)
                                .padding(top = 16.dp),
                            verticalArrangement = Arrangement.Center,
                        ) {

                            SocialButtons(
                                onGoogleClick = {
                                    onEvent(LoginUiEvent.OnGoogleSignInClicked(context))
                                },
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
            state = LoginUiState(email = "test@email.com", isEmailValid = false),
            widthSizeClass = WindowWidthSizeClass.Compact,
            snackbarHostState = remember { SnackbarHostState() },
            onEvent = {},
            onNavigateToLegals = {}
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
            onEvent = {},
            onNavigateToLegals = {}
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
            onEvent = {},
            onNavigateToLegals = {}
        )
    }
}
