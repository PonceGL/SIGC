package com.poncegl.sigc.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.poncegl.sigc.ui.feature.auth.WelcomeScreen

@Composable
fun SigcNavHost() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Welcome.route) {
        composable(Screen.Welcome.route) {
            WelcomeScreen()
        }
        composable(Screen.Login.route) {
            PlaceholderScreen("Login Screen")
        }
        composable(Screen.SignUp.route) {
            PlaceholderScreen("Sign Up Screen")
        }
        composable(Screen.Dashboard.route) {
            PlaceholderScreen("Dashboard Screen")
        }
        composable(Screen.PatientList.route) {
            PlaceholderScreen("Patient List Screen")
        }
        composable(Screen.MedicationList.route) {
            PlaceholderScreen("Medication List Screen")
        }
    }
}

@Composable
fun PlaceholderScreen(name: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = name)
    }
}
