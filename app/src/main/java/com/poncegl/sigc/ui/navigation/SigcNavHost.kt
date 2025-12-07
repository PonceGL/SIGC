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

@Composable
fun SigcNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = OnboardingDestination.route
    ) {
        authGraph(navController)

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
