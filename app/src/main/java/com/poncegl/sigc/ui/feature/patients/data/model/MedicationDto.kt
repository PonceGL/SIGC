package com.poncegl.sigc.ui.feature.patients.data.model

import java.util.Date

object MedicationFields {
    const val ID = "id"
    const val CARE_PLAN_ID = "carePlanId"
    const val NAME = "name"
    const val TYPE = "type"
    const val INVENTORY = "inventory"
    const val CONFIG = "config"
    const val INSTRUCTIONS = "instructions"
    const val CREATED_AT = "createdAt"

    const val INVENTORY_STOCK = "currentStock"
    const val INVENTORY_UNIT = "unit"
    const val INVENTORY_THRESHOLD = "alertThreshold"

    const val CONFIG_DOSE = "dose"
    const val CONFIG_FREQ = "frequencyDescription"
    const val CONFIG_CRON = "cronExpression"
}

data class MedicationDto(
    val id: String = "",
    val carePlanId: String = "",
    val name: String = "",
    val type: String = "MEDICINE",
    val inventory: Map<String, Any>? = null,
    val config: Map<String, Any> = emptyMap(),
    val instructions: String? = null,
    val createdAt: Date? = null
) {
    fun toMap(): Map<String, Any?> = mapOf(
        MedicationFields.ID to id,
        MedicationFields.CARE_PLAN_ID to carePlanId,
        MedicationFields.NAME to name,
        MedicationFields.TYPE to type,
        MedicationFields.INVENTORY to inventory,
        MedicationFields.CONFIG to config,
        MedicationFields.INSTRUCTIONS to instructions,
        MedicationFields.CREATED_AT to createdAt
    )
}