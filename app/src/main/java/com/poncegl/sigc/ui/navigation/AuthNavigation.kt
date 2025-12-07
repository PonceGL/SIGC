package com.poncegl.sigc.ui.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.poncegl.sigc.ui.feature.onboarding.OnboardingScreen

fun NavGraphBuilder.authGraph(navController: NavController) {

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

    // 2. Pantalla de Login (Placeholder temporal)
    composable(route = LoginDestination.route) {
        // Aquí conectaremos LoginScreen real más adelante
        LoginRoutePlaceholder(
            onNavigateToSignUp = {
                navController.navigate(SignUpDestination.route)
            },
            onNavigateToDashboard = {
                navController.navigate(DashboardDestination.route) {
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
fun LoginRoutePlaceholder(
    onNavigateToSignUp: () -> Unit,
    onNavigateToDashboard: () -> Unit
) {
    Column {
        Text(text = "Pantalla LOGIN")
        Button (onClick = onNavigateToDashboard) {
            Text("Iniciar Sesión (Ir a Dashboard)")
        }
        Button(onClick = onNavigateToSignUp) {
            Text("Registrarse")
        }
    }
}

@Composable
fun SignUpRoutePlaceholder() {
    Text(text = "Pantalla SIGN UP")
}