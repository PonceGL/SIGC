package com.poncegl.sigc.ui.feature.patients.data.model

import java.util.Date

object MedicationFields {
    const val ID = "id"
    const val CARE_PLAN_ID = "carePlanId"
    const val NAME = "name"
    const val CONCENTRATION = "concentration"
    const val TYPE = "type"
    const val PRESENTATION = "presentation"
    const val INVENTORY = "inventory"
    const val CONFIG = "config"
    const val INSTRUCTIONS = "instructions"
    const val CREATED_AT = "createdAt"

    // Inventory Fields
    const val INVENTORY_QUANTITY = "quantityCurrent" // Renombrado de currentStock
    const val INVENTORY_ITEM_SIZE = "itemSize"
    const val INVENTORY_UNIT = "unit"
    const val INVENTORY_THRESHOLD = "alertThreshold"
    const val INVENTORY_LAST_REFILL = "lastRefillDate"

    // Config Fields
    const val CONFIG_DOSE_QTY = "doseQuantity"
    const val CONFIG_DOSE_DESC = "doseDescription"
    const val CONFIG_FREQ_DESC = "frequencyDescription"
    const val CONFIG_FREQ_DAYS = "frequencyDays"
    const val CONFIG_IS_INDEFINITE = "isIndefinite"
    const val CONFIG_CRON = "cronExpression"
}

data class MedicationDto(
    val id: String = "",
    val carePlanId: String = "",
    val name: String = "",
    val concentration: String? = null,
    val type: String = "MEDICINE",
    val presentation: String? = null,
    val inventory: Map<String, Any>? = null,
    val config: Map<String, Any> = emptyMap(),
    val instructions: String? = null,
    val createdAt: Date? = null
) {
    fun toMap(): Map<String, Any?> = mapOf(
        MedicationFields.ID to id,
        MedicationFields.CARE_PLAN_ID to carePlanId,
        MedicationFields.NAME to name,
        MedicationFields.CONCENTRATION to concentration,
        MedicationFields.TYPE to type,
        MedicationFields.PRESENTATION to presentation,
        MedicationFields.INVENTORY to inventory,
        MedicationFields.CONFIG to config,
        MedicationFields.INSTRUCTIONS to instructions,
        MedicationFields.CREATED_AT to createdAt
    )
}