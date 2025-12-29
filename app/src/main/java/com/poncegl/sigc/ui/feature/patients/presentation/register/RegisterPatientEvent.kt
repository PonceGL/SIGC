package com.poncegl.sigc.ui.feature.patients.presentation.register

import com.poncegl.sigc.ui.feature.patients.domain.model.MedicationPresentation
import java.time.LocalDate
import java.time.LocalTime

sealed interface RegisterPatientEvent {
    // Navegación
    data object NextStep : RegisterPatientEvent
    data object PreviousStep : RegisterPatientEvent
    data object Submit : RegisterPatientEvent
    data object ClearError : RegisterPatientEvent

    // Paso 1: Paciente
    data class NameChanged(val name: String) : RegisterPatientEvent
    data class DobChanged(val date: LocalDate) : RegisterPatientEvent
    data class AgeChanged(val age: String) : RegisterPatientEvent
    data class DobUnknownChanged(val isUnknown: Boolean) : RegisterPatientEvent
    data class DiagnosisChanged(val diagnosis: String) : RegisterPatientEvent

    // Paso 2: Medicamentos (Gestión)
    data object StartAddingMedication : RegisterPatientEvent
    data object CancelAddingMedication : RegisterPatientEvent
    data object SaveMedicationToList : RegisterPatientEvent
    data class RemoveMedicationFromList(val tempId: String) : RegisterPatientEvent

    // Paso 2: Medicamentos (Formulario Inputs)
    data class MedNameChanged(val name: String) : RegisterPatientEvent
    data class MedPresentationChanged(val presentation: MedicationPresentation) : RegisterPatientEvent
    data class MedDoseChanged(val dose: String) : RegisterPatientEvent
    data class MedUnitChanged(val unit: String) : RegisterPatientEvent
    data class MedAddFrequencyTime(val time: LocalTime) : RegisterPatientEvent
    data class MedRemoveFrequencyTime(val time: LocalTime) : RegisterPatientEvent
    data class MedDurationChanged(val days: String) : RegisterPatientEvent
    data class MedUnitsPerPackageChanged(val value: String) : RegisterPatientEvent
    data class MedPackageCountChanged(val value: String) : RegisterPatientEvent
    data class MedInstructionsChanged(val text: String) : RegisterPatientEvent
    data class MedReasonChanged(val reason: String) : RegisterPatientEvent
    data class MedAlertSwitchToggled(val enabled: Boolean) : RegisterPatientEvent

    // Paso 3: Doctor
    data class DoctorNameChanged(val name: String) : RegisterPatientEvent
    data class DoctorPhoneChanged(val phone: String) : RegisterPatientEvent

    // Paso 4: Historial
    data class EventDescriptionChanged(val desc: String) : RegisterPatientEvent
    data class EventDateChanged(val date: LocalDate) : RegisterPatientEvent
    data object AddHistoryEvent : RegisterPatientEvent
    data class RemoveHistoryEvent(val logId: String) : RegisterPatientEvent
}