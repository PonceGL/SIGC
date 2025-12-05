package com.poncegl.sigc.ui.feature.auth.model

import com.poncegl.sigc.R

/**
 * Modelo de datos que representa una página individual en el carrusel de Onboarding.
 */
data class OnboardingPage(
    val title: String,
    val description: String,
    val icon: Int
)

/**
 * Fuente de datos estática para el Onboarding.
 */
val onboardingPagesData = listOf(
    OnboardingPage(
        title = "Cuidado Integral",
        description = "Centraliza toda la información de cuidados de tu paciente en un solo lugar. Medicamentos, signos vitales y más.",
        icon = R.drawable.heart
    ),
    OnboardingPage(
        title = "Alertas Inteligentes",
        description = "Sistema de notificaciones escalonadas que garantiza que ninguna dosis sea olvidada. Escalación automática al responsable.",
        icon = R.drawable.bell
    ),
    OnboardingPage(
        title = "Trabajo en Equipo",
        description = "Coordina turnos entre cuidadores con sincronización en tiempo real. Todos siempre al día.",
        icon = R.drawable.people
    ),
    OnboardingPage(
        title = "Trazabilidad Total",
        description = "Historial completo de quién hizo qué y cuándo. Genera reportes para revisión médica.",
        icon = R.drawable.list
    )
)