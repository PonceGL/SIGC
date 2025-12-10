package com.poncegl.sigc.ui.navigation

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavDeepLink
//import androidx.navigation.navDeepLink

/**
 * Contrato que deben cumplir todos los destinos de navegación en SIGC.
 */
interface SigcDestination {
    val route: String
    val arguments: List<NamedNavArgument>
        get() = emptyList()
    val deepLinks: List<NavDeepLink>
        get() = emptyList()
}

// --- Flujo de Autenticación ---
object OnboardingDestination : SigcDestination {
    override val route = "onboarding"
}

object LoginDestination : SigcDestination {
    override val route = "login"
    // Ejemplo de cómo se vería un DeepLink futuro:
    // override val deepLinks = listOf(navDeepLink { uriPattern = "sigc://login" })
}

object SignUpDestination : SigcDestination {
    override val route = "signup"
}

// --- Flujo Principal (App) ---
object HomeDestination : SigcDestination {
    override val route = "home"
}

object DashboardDestination : SigcDestination {
    override val route = "dashboard"
}

object PatientListDestination : SigcDestination {
    override val route = "patient_list"
}

object MedicationListDestination : SigcDestination {
    override val route = "medication_list"
}