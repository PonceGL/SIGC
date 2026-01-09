package com.poncegl.sigc.ui.feature.patients.data.model

import java.util.Date

object PatientFields {
    const val ID = "id"
    const val NAME = "name"
    const val DOB = "dob"
    const val BLOOD_TYPE = "bloodType"
    const val ALLERGIES = "allergies"
    const val PRIMARY_TEAM_ID = "primaryTeamId"
}

data class PatientDto(
    val id: String = "",
    val name: String = "",
    val dob: Date? = null,
    val bloodType: String? = null,
    val allergies: List<String> = emptyList(),
    val primaryTeamId: String = ""
) {
    fun toMap(): Map<String, Any?> = mapOf(
        PatientFields.ID to id,
        PatientFields.NAME to name,
        PatientFields.DOB to dob,
        PatientFields.BLOOD_TYPE to bloodType,
        PatientFields.ALLERGIES to allergies,
        PatientFields.PRIMARY_TEAM_ID to primaryTeamId
    )
}