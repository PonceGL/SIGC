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

    // Paso 1
    val patientName: String = "",
    val patientDob: LocalDate? = null,
    val patientAgeInput: String = "",
    val isDobUnknown: Boolean = false,
    val diagnosisName: String = "",

    // Paso 2
    val addedMedications: List<DraftMedication> = emptyList(),
    val isAddingMedication: Boolean = false,
    val medicationForm: MedicationFormState = MedicationFormState(),

    // Paso 3 y 4...
    val doctorName: String = "",
    val doctorPhone: String = "",
    val historyEvents: List<LogEntry> = emptyList(),
    val eventDescription: String = "",
    val eventDate: LocalDate = LocalDate.now()
)

data class MedicationFormState(
    val presentation: MedicationPresentation = MedicationPresentation.TABLET,
    val name: String = "",
    val concentration: String = "",

    // Tratamiento
    val dose: String = "",
    val unit: String = "pzas",
    val frequencyTimes: List<LocalTime> = emptyList(),
    val frequencyDays: Set<Int> = setOf(1, 2, 3, 4, 5, 6, 7),
    val isIndefinite: Boolean = false,
    val durationDays: String = "",

    // Inventario
    val unitsPerPackage: String = "",
    val packageCount: String = "",    // Envases (2 cajas)

    // Alertas
    val isStockAlertEnabled: Boolean = true,
    val stockAlertThreshold: String = "",
    val stockAlertDescription: String = "",

    // Extras
    val instructions: String = "",
    val usageReason: String = ""
)

data class DraftMedication(
    val tempId: String,
    val name: String,
    val presentation: MedicationPresentation?,
    val config: MedicationConfig,
    val inventory: MedicationInventory?,
    val type: MedicationType = MedicationType.MEDICINE,
    val instructions: String?
)