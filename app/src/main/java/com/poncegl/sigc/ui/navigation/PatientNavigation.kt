package com.poncegl.sigc.ui.navigation

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.poncegl.sigc.ui.feature.patients.presentation.register.RegisterPatientScreen

fun NavGraphBuilder.patientGraph(
    navController: NavController,
    widthSizeClass: WindowWidthSizeClass
) {

    composable(route = RegisterPatientDestination.route) {
        RegisterPatientScreen(
            widthSizeClass = widthSizeClass,
            onNavigateToHome = {
                navController.navigate(HomeDestination.route) {
                    popUpTo(LoginDestination.route) { inclusive = true }
                }
            }
        )
    }

}