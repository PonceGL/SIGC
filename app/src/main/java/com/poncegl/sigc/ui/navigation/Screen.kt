package com.poncegl.sigc.ui.navigation

sealed class Screen(val route: String) {
    object Welcome : Screen("welcome")
    object Login : Screen("login")
    object SignUp : Screen("signup")
    object Dashboard : Screen("dashboard")
    object PatientList : Screen("patient_list")
    object MedicationList : Screen("medication_list")
}
