package com.poncegl.sigc.ui.feature.patients.presentation.register

import com.poncegl.sigc.ui.feature.patients.domain.model.MedicationConfig
import com.poncegl.sigc.ui.feature.patients.domain.model.MedicationInventory
import com.poncegl.sigc.ui.feature.patients.domain.model.MedicationPresentation
import com.poncegl.sigc.ui.feature.patients.domain.model.MedicationType
import com.poncegl.sigc.ui.feature.tracking.domain.model.LogEntry
import java.time.LocalDate
import java.time.LocalTime

data class RegisterPatientUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val currentStep: Int = 0,
    val isComplete: Boolean = false,

    val isOfflineMode: Boolean = false,

    // --- Paso 1: Datos del Paciente ---
    val patientName: String = "",
    val patientDob: LocalDate? = null,
    val patientAgeInput: String = "",
    val isDobUnknown: Boolean = false,
    val diagnosisName: String = "",

    // --- Paso 2: Medicamentos ---
    val addedMedications: List<DraftMedication> = emptyList(),
    val isAddingMedication: Boolean = false,
    val medicationForm: MedicationFormState = MedicationFormState(),

    // --- Paso 3: Doctor ---
    val doctorName: String = "",
    val doctorPhone: String = "",

    // --- Paso 4: Historial ---
    val historyEvents: List<LogEntry> = emptyList(),
    val eventDescription: String = "",
    val eventDate: LocalDate = LocalDate.now()
)

/**
 * Estado del formulario de "Agregar Medicamento" (Sheet/Dialog)
 */
data class MedicationFormState(
    val presentation: MedicationPresentation = MedicationPresentation.TABLET,
    val name: String = "",
    val dose: String = "",
    val unit: String = "mg",
    val frequencyTimes: List<LocalTime> = emptyList(),
    val durationDays: String = "",
    val unitsPerPackage: String = "",
    val packageCount: String = "",
    val stockAlertThreshold: String = "0",
    val instructions: String = "",
    val usageReason: String = ""
)

/**
 * Representación visual del medicamento en la lista antes de guardar
 */
data class DraftMedication(
    val tempId: String, // UUID temporal para borrar de la lista UI
    val name: String,
    val presentation: MedicationPresentation?,
    val config: MedicationConfig,
    val inventory: MedicationInventory?,
    val type: MedicationType = MedicationType.MEDICINE,
    val instructions: String?
)