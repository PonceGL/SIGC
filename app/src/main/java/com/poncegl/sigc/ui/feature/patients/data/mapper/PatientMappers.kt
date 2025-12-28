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
    val inventoryDomain = this.inventory?.let { map ->
        MedicationInventory(
            currentStock = (map[MedicationFields.INVENTORY_STOCK] as? Number)?.toDouble() ?: 0.0,
            unit = (map[MedicationFields.INVENTORY_UNIT] as? String) ?: "",
            alertThreshold = (map[MedicationFields.INVENTORY_THRESHOLD] as? Number)?.toInt() ?: 0
        )
    }

    val configDomain = MedicationConfig(
        dose = (this.config[MedicationFields.CONFIG_DOSE] as? String) ?: "",
        frequencyDescription = (this.config[MedicationFields.CONFIG_FREQ] as? String) ?: "",
        cronExpression = this.config[MedicationFields.CONFIG_CRON] as? String
    )

    return Medication(
        id = this.id,
        carePlanId = this.carePlanId,
        name = this.name,
        type = try {
            MedicationType.valueOf(this.type)
        } catch (e: Exception) {
            MedicationType.MEDICINE
        },
        inventory = inventoryDomain,
        config = configDomain,
        instructions = this.instructions,
        createdAt = this.createdAt?.toInstant() ?: Instant.now()
    )
}

fun Medication.toDto(): MedicationDto {
    val inventoryMap = this.inventory?.let { inv ->
        mapOf(
            MedicationFields.INVENTORY_STOCK to inv.currentStock,
            MedicationFields.INVENTORY_UNIT to inv.unit,
            MedicationFields.INVENTORY_THRESHOLD to inv.alertThreshold
        )
    }

    val configMap = mapOf(
        MedicationFields.CONFIG_DOSE to this.config.dose,
        MedicationFields.CONFIG_FREQ to this.config.frequencyDescription,
        MedicationFields.CONFIG_CRON to this.config.cronExpression
    ).filterValues { it != null }

    return MedicationDto(
        id = this.id,
        carePlanId = this.carePlanId,
        name = this.name,
        type = this.type.name,
        inventory = inventoryMap,
        config = configMap as Map<String, Any>,
        instructions = this.instructions,
        createdAt = Date.from(this.createdAt)
    )
}