package com.poncegl.sigc.ui.navigation

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.poncegl.sigc.ui.feature.registerPatient.RegisterPatientScreen

fun NavGraphBuilder.patientGraph(
    navController: NavController,
    widthSizeClass: WindowWidthSizeClass
) {

    composable(route = RegisterPatientDestination.route) {
        RegisterPatientScreen(
            widthSizeClass = widthSizeClass,
        )
    }

}