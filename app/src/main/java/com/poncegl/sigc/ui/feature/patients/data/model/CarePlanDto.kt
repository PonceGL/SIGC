package com.poncegl.sigc.ui.feature.patients.data.model

import java.util.Date

object CarePlanFields {
    const val ID = "id"
    const val PATIENT_ID = "patientId"
    const val NAME = "name"
    const val DOCTOR_NAME = "doctorName"
    const val DOCTOR_PHONE = "doctorPhone"
    const val STATUS = "status"
    const val START_DATE = "startDate"
    const val END_DATE = "endDate"
}

data class CarePlanDto(
    val id: String = "",
    val patientId: String = "",
    val name: String = "",
    val doctorName: String? = null,
    val doctorPhone: String? = null,
    val status: String = "ACTIVE",
    val startDate: Date? = null,
    val endDate: Date? = null
) {
    fun toMap(): Map<String, Any?> = mapOf(
        CarePlanFields.ID to id,
        CarePlanFields.PATIENT_ID to patientId,
        CarePlanFields.NAME to name,
        CarePlanFields.DOCTOR_NAME to doctorName,
        CarePlanFields.DOCTOR_PHONE to doctorPhone,
        CarePlanFields.STATUS to status,
        CarePlanFields.START_DATE to startDate,
        CarePlanFields.END_DATE to endDate
    )
}