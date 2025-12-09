package com.poncegl.sigc.ui.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.poncegl.sigc.ui.feature.auth.model.LoginScreen
import com.poncegl.sigc.ui.feature.onboarding.OnboardingScreen

fun NavGraphBuilder.authGraph(navController: NavController, widthSizeClass: WindowWidthSizeClass) {

    composable(route = OnboardingDestination.route) {
        OnboardingScreen(
            onFinishOnboarding = {
                navController.navigate(LoginDestination.route) {
                    popUpTo(OnboardingDestination.route) {
                        inclusive = true
                    }
                }
            }
        )
    }

    composable(route = LoginDestination.route) {
        LoginScreen(
            widthSizeClass = widthSizeClass,
            onLoginSuccess = {
                navController.navigate(HomeDestination.route) {
                    popUpTo(LoginDestination.route) { inclusive = true }
                }
            }
        )
    }

    // 3. Pantalla de Registro (Placeholder temporal)
    composable(route = SignUpDestination.route) {
        SignUpRoutePlaceholder()
    }
}

@Composable
fun SignUpRoutePlaceholder() {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(text = "Pantalla SIGN UP")
    }
}