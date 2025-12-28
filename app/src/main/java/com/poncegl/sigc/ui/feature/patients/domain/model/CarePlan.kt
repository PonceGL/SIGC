package com.poncegl.sigc.ui.feature.patients.domain.model

import java.time.Instant

enum class CarePlanStatus {
    ACTIVE,
    COMPLETED,
    ARCHIVED
}

data class CarePlan(
    val id: String,
    val patientId: String,
    val name: String,
    val doctorName: String? = null,
    val doctorPhone: String? = null,
    val status: CarePlanStatus = CarePlanStatus.ACTIVE,
    val startDate: Instant,
    val endDate: Instant? = null
)