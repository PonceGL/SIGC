package com.poncegl.sigc.ui.feature.patients.domain.model

import java.time.Instant

enum class MedicationType {
    MEDICINE,
    THERAPY,
    CURATION
}


/**
 * @param quantityCurrent Si es CALCULATED: Cantidad total de unidades (ej: 40 pastillas).
 * Si es BY_CONTAINER: Cantidad de envases cerrados/en uso (ej: 2 tubos).
 * @param itemSize El tamaño de un solo envase (ej: 20 pzas, 400 ml). Útil para reabastecer.
 * @param unit La unidad de medida (mg, ml, pzas, envases).
 */
data class MedicationInventory(
    val quantityCurrent: Double,
    val itemSize: Double,
    val unit: String,
    val alertThreshold: Int = 0,
    val lastRefillDate: Instant? = null
)

/**
 * @param doseQuantity Cantidad numérica a restar por toma (ej: 1.0, 5.0). Null si es estrategia BY_CONTAINER.
 * @param frequencyDays Lista de días de la semana (1=Lunes, 7=Domingo). Si está vacío implica todos los días.
 * @param isIndefinite Si es true, ignoramos fecha fin del CarePlan (o no tiene fecha fin).
 */
data class MedicationConfig(
    val doseQuantity: Double?,
    val doseDescription: String,
    val frequencyDescription: String,
    val frequencyDays: List<Int> = emptyList(),
    val isIndefinite: Boolean = false,
    val cronExpression: String? = null
)

data class Medication(
    val id: String,
    val carePlanId: String,
    val name: String,
    val concentration: String? = null,
    val type: MedicationType = MedicationType.MEDICINE,
    val presentation: MedicationPresentation? = null,
    val inventory: MedicationInventory? = null,
    val config: MedicationConfig,
    val instructions: String? = null,
    val createdAt: Instant
)