package com.poncegl.sigc.ui.feature.auth.model

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.poncegl.sigc.ui.components.login.Content
import com.poncegl.sigc.ui.theme.SIGCTheme

@Composable
fun LoginScreen(
    onNavigateToSignUp: () -> Unit,
    onNavigateToDashboard: () -> Unit
) {
    Content(
        onNavigateToSignUp = onNavigateToSignUp,
        onNavigateToDashboard = onNavigateToDashboard
    )
}

@Preview(device = "id:pixel_5", apiLevel = 31, showSystemUi = true)
@Composable
fun LoginScreenPreview() {
    SIGCTheme {
        LoginScreen(
            onNavigateToSignUp = {},
            onNavigateToDashboard = {}
        )
    }
}