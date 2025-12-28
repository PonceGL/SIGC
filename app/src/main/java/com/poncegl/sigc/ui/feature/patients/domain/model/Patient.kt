package com.poncegl.sigc.ui.feature.patients.domain.model

import java.time.LocalDate

data class Patient(
    val id: String,
    val name: String,
    val dob: LocalDate,
    val bloodType: String? = null,
    val allergies: List<String> = emptyList(),
    val primaryTeamId: String
)