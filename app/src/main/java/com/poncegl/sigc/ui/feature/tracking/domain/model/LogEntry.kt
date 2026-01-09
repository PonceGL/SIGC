package com.poncegl.sigc.ui.feature.tracking.domain.model

import java.time.Instant
import java.time.LocalDate

enum class VitalSignType {
    BLOOD_PRESSURE,     // Requiere 2 valores (Sistólica/Diastólica)
    HEART_RATE,         // 1 valor (BPM)
    TEMPERATURE,        // 1 valor (Celsius/Fahrenheit)
    OXYGEN_SATURATION,  // 1 valor (%)
    WEIGHT,             // 1 valor (kg/lbs)
    GLUCOSE             // 1 valor (mg/dL)
}

enum class LogCategory {
    HISTORY,
    NOTE,
    VITAL_SIGN,
    INCIDENT
}

sealed interface LogEntry {
    val id: String
    val patientId: String
    val timestamp: Instant
    val authorId: String
    val category: LogCategory
}

/**
 * Para eventos históricos (ej. "Cirugía hace 2 años").
 * Se usa en el Wizard de registro.
 */
data class HistoryLogEntry(
    override val id: String,
    override val patientId: String,
    override val timestamp: Instant,
    override val authorId: String,
    val description: String,
    val occurredOn: LocalDate
) : LogEntry {
    override val category = LogCategory.HISTORY
}

/**
 * Nota general de texto.
 */
data class NoteLogEntry(
    override val id: String,
    override val patientId: String,
    override val timestamp: Instant,
    override val authorId: String,
    val text: String
) : LogEntry {
    override val category = LogCategory.NOTE
}

data class VitalSignLogEntry(
    override val id: String,
    override val patientId: String,
    override val timestamp: Instant,
    override val authorId: String,
    val type: VitalSignType,
    val value1: Double,       // Valor principal (ej. Sistólica, Peso, Temp)
    val value2: Double? = null, // Valor secundario opcional (ej. Diastólica)
    val unit: String          // Ej: "mmHg", "kg", "bpm"
) : LogEntry {
    override val category = LogCategory.VITAL_SIGN
}