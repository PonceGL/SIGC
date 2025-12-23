package com.poncegl.sigc.ui.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.poncegl.sigc.BuildConfig
import com.poncegl.sigc.ui.feature.auth.LoginScreen
import com.poncegl.sigc.ui.feature.auth.NewPasswordScreen
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
            onNavigateToLegals = {
                navController.navigate(LegalsDestination.route)
            },
            onLoginSuccess = {
                navController.navigate(HomeDestination.route) {
                    popUpTo(LoginDestination.route) { inclusive = true }
                }
            }
        )
    }

    composable(
        route = "new_password?oobCode={oobCode}",
        arguments = listOf(
            navArgument("oobCode") { type = NavType.StringType },
            navArgument("apiKey") { type = NavType.StringType; nullable = true; defaultValue = null },
            navArgument("mode") { type = NavType.StringType; nullable = true; defaultValue = null },
            navArgument("continueUrl") { type = NavType.StringType; nullable = true; defaultValue = null },
            navArgument("lang") { type = NavType.StringType; nullable = true; defaultValue = null }
        ),
        deepLinks = listOf(navDeepLink {
            uriPattern = "https://${BuildConfig.AUTH_HOST}/__/auth/action?apiKey={apiKey}&mode={mode}&oobCode={oobCode}&continueUrl={continueUrl}&lang={lang}"
        })
    ) {
        NewPasswordScreen(
            onPasswordChangedSuccess = {
                navController.navigate(LoginDestination.route) {
                    popUpTo(LoginDestination.route) { inclusive = true }
                    launchSingleTop = true
                }
            }
        )
    }


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
