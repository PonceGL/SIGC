package com.poncegl.sigc.ui.feature.patients.domain.model

import java.time.Instant

enum class MedicationType {
    MEDICINE,
    THERAPY,
    CURATION
}


data class MedicationInventory(
    val currentStock: Double,
    val unit: String,
    val alertThreshold: Int = 0
)


data class MedicationConfig(
    val dose: String,
    val frequencyDescription: String,
    val cronExpression: String? = null
)

data class Medication(
    val id: String,
    val carePlanId: String,
    val name: String,
    val type: MedicationType = MedicationType.MEDICINE,
    val inventory: MedicationInventory? = null,
    val config: MedicationConfig,
    val instructions: String? = null,
    val createdAt: Instant
)