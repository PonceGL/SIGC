package com.poncegl.sigc.ui.feature.patients.data.mapper

import com.poncegl.sigc.ui.feature.patients.data.model.CarePlanDto
import com.poncegl.sigc.ui.feature.patients.data.model.MedicationDto
import com.poncegl.sigc.ui.feature.patients.data.model.MedicationFields
import com.poncegl.sigc.ui.feature.patients.data.model.PatientDto
import com.poncegl.sigc.ui.feature.patients.domain.model.CarePlan
import com.poncegl.sigc.ui.feature.patients.domain.model.CarePlanStatus
import com.poncegl.sigc.ui.feature.patients.domain.model.Medication
import com.poncegl.sigc.ui.feature.patients.domain.model.MedicationConfig
import com.poncegl.sigc.ui.feature.patients.domain.model.MedicationInventory
import com.poncegl.sigc.ui.feature.patients.domain.model.MedicationPresentation
import com.poncegl.sigc.ui.feature.patients.domain.model.MedicationType
import com.poncegl.sigc.ui.feature.patients.domain.model.Patient
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date

// --- Patient Mappers ---

fun PatientDto.toDomain(): Patient {
    return Patient(
        id = this.id,
        name = this.name,
        dob = this.dob?.toInstant()
            ?.atZone(ZoneId.systemDefault())
            ?.toLocalDate()
            ?: LocalDate.now(),
        bloodType = this.bloodType,
        allergies = this.allergies,
        primaryTeamId = this.primaryTeamId
    )
}

fun Patient.toDto(): PatientDto {
    return PatientDto(
        id = this.id,
        name = this.name,
        dob = Date.from(this.dob.atStartOfDay(ZoneId.systemDefault()).toInstant()),
        bloodType = this.bloodType,
        allergies = this.allergies,
        primaryTeamId = this.primaryTeamId
    )
}

// --- CarePlan Mappers ---

fun CarePlanDto.toDomain(): CarePlan {
    return CarePlan(
        id = this.id,
        patientId = this.patientId,
        name = this.name,
        doctorName = this.doctorName,
        doctorPhone = this.doctorPhone,
        status = try {
            CarePlanStatus.valueOf(this.status)
        } catch (e: Exception) {
            CarePlanStatus.ACTIVE
        },
        startDate = this.startDate?.toInstant() ?: Instant.now(),
        endDate = this.endDate?.toInstant()
    )
}

fun CarePlan.toDto(): CarePlanDto {
    return CarePlanDto(
        id = this.id,
        patientId = this.patientId,
        name = this.name,
        doctorName = this.doctorName,
        doctorPhone = this.doctorPhone,
        status = this.status.name,
        startDate = Date.from(this.startDate),
        endDate = this.endDate?.let { Date.from(it) }
    )
}

// --- Medication Mappers ---

fun MedicationDto.toDomain(): Medication {
    // 1. Mapeo de Inventario
    val inventoryDomain = this.inventory?.let { map ->
        val lastRefill = (map[MedicationFields.INVENTORY_LAST_REFILL] as? Date)?.toInstant()

        MedicationInventory(
            quantityCurrent = (map[MedicationFields.INVENTORY_QUANTITY] as? Number)?.toDouble()
                ?: 0.0,
            itemSize = (map[MedicationFields.INVENTORY_ITEM_SIZE] as? Number)?.toDouble() ?: 0.0,
            unit = (map[MedicationFields.INVENTORY_UNIT] as? String) ?: "",
            alertThreshold = (map[MedicationFields.INVENTORY_THRESHOLD] as? Number)?.toInt() ?: 0,
            lastRefillDate = lastRefill
        )
    }

    // 2. Mapeo de Configuración
    // Nota: frequencyDays viene como List<Long> en Firestore, casteamos seguro a Number y luego a Int
    val daysList = (this.config[MedicationFields.CONFIG_FREQ_DAYS] as? List<*>)
        ?.mapNotNull { (it as? Number)?.toInt() }
        ?: emptyList()

    val configDomain = MedicationConfig(
        doseQuantity = (this.config[MedicationFields.CONFIG_DOSE_QTY] as? Number)?.toDouble(),
        doseDescription = (this.config[MedicationFields.CONFIG_DOSE_DESC] as? String) ?: "",
        frequencyDescription = (this.config[MedicationFields.CONFIG_FREQ_DESC] as? String) ?: "",
        frequencyDays = daysList,
        isIndefinite = (this.config[MedicationFields.CONFIG_IS_INDEFINITE] as? Boolean) ?: false,
        cronExpression = this.config[MedicationFields.CONFIG_CRON] as? String
    )

    // 3. Mapeo de Enums (Con fallback seguro)
    val typeEnum = try {
        MedicationType.valueOf(this.type)
    } catch (e: Exception) {
        MedicationType.MEDICINE
    }

    val presentationEnum = this.presentation?.let {
        try {
            MedicationPresentation.valueOf(it)
        } catch (e: Exception) {
            null // O MedicationPresentation.OTHER para un default
        }
    }

    return Medication(
        id = this.id,
        carePlanId = this.carePlanId,
        name = this.name,
        concentration = this.concentration,
        type = typeEnum,
        presentation = presentationEnum,
        inventory = inventoryDomain,
        config = configDomain,
        instructions = this.instructions,
        createdAt = this.createdAt?.toInstant() ?: Instant.now()
    )
}

fun Medication.toDto(): MedicationDto {
    val inventoryMap = this.inventory?.let { inv ->
        mapOf(
            MedicationFields.INVENTORY_QUANTITY to inv.quantityCurrent,
            MedicationFields.INVENTORY_ITEM_SIZE to inv.itemSize,
            MedicationFields.INVENTORY_UNIT to inv.unit,
            MedicationFields.INVENTORY_THRESHOLD to inv.alertThreshold,
            MedicationFields.INVENTORY_LAST_REFILL to inv.lastRefillDate?.let { Date.from(it) }
        ).filterValues { it != null }
    }

    val configMap = mapOf(
        MedicationFields.CONFIG_DOSE_QTY to this.config.doseQuantity,
        MedicationFields.CONFIG_DOSE_DESC to this.config.doseDescription,
        MedicationFields.CONFIG_FREQ_DESC to this.config.frequencyDescription,
        MedicationFields.CONFIG_FREQ_DAYS to this.config.frequencyDays,
        MedicationFields.CONFIG_IS_INDEFINITE to this.config.isIndefinite,
        MedicationFields.CONFIG_CRON to this.config.cronExpression
    ).filterValues { it != null }

    return MedicationDto(
        id = this.id,
        carePlanId = this.carePlanId,
        name = this.name,
        concentration = this.concentration,
        type = this.type.name,
        presentation = this.presentation?.name,
        inventory = inventoryMap as? Map<String, Any>,
        config = configMap as Map<String, Any>,
        instructions = this.instructions,
        createdAt = Date.from(this.createdAt)
    )
}