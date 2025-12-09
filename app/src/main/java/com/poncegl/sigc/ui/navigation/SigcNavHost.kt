package com.poncegl.sigc.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.poncegl.sigc.ui.feature.home.HomeScreen

@Composable
fun SigcNavHost(startDestination: String, windowSize: WindowWidthSizeClass) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        authGraph(navController, windowSize)

        composable(HomeDestination.route) {
            HomeScreen(
                onNavigateToLogin = {
                    navController.navigate(LoginDestination.route) {
                        popUpTo(HomeDestination.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(DashboardDestination.route) {
            PlaceholderScreen("Dashboard Principal")
        }

        composable(PatientListDestination.route) {
            PlaceholderScreen("Lista de Pacientes")
        }

        composable(MedicationListDestination.route) {
            PlaceholderScreen("Lista de Medicamentos")
        }
    }
}

@Composable
fun PlaceholderScreen(name: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = name)
    }
}
