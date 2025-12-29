package com.poncegl.sigc.ui.feature.patients.presentation.register

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poncegl.sigc.core.util.NetworkMonitor
import com.poncegl.sigc.ui.feature.patients.domain.model.CarePlan
import com.poncegl.sigc.ui.feature.patients.domain.model.Medication
import com.poncegl.sigc.ui.feature.patients.domain.model.MedicationConfig
import com.poncegl.sigc.ui.feature.patients.domain.model.MedicationInventory
import com.poncegl.sigc.ui.feature.patients.domain.model.Patient
import com.poncegl.sigc.ui.feature.patients.domain.usecase.AddMedicationUseCase
import com.poncegl.sigc.ui.feature.patients.domain.usecase.CreateCarePlanUseCase
import com.poncegl.sigc.ui.feature.patients.domain.usecase.CreatePatientUseCase
import com.poncegl.sigc.ui.feature.patients.domain.usecase.UpdateCarePlanUseCase
import com.poncegl.sigc.ui.feature.tracking.domain.model.HistoryLogEntry
import com.poncegl.sigc.ui.feature.tracking.domain.usecase.LogEventUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.Period
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class RegisterPatientViewModel @Inject constructor(
    private val createPatientUseCase: CreatePatientUseCase,
    private val createCarePlanUseCase: CreateCarePlanUseCase,
    private val updateCarePlanUseCase: UpdateCarePlanUseCase,
    private val addMedicationUseCase: AddMedicationUseCase,
    private val logEventUseCase: LogEventUseCase,
    private val networkMonitor: NetworkMonitor
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterPatientUiState())
    val uiState = _uiState.asStateFlow()

    fun onEvent(event: RegisterPatientEvent) {
        when (event) {
            is RegisterPatientEvent.NextStep -> {
                if (validateCurrentStep()) {
                    _uiState.update { it.copy(currentStep = it.currentStep + 1) }
                }
            }

            is RegisterPatientEvent.PreviousStep -> {
                if (_uiState.value.currentStep > 1) {
                    _uiState.update { it.copy(currentStep = it.currentStep - 1) }
                }
            }

            is RegisterPatientEvent.Submit -> submitData()
            is RegisterPatientEvent.ClearError -> _uiState.update { it.copy(error = null) }

            // --- Paso 1 ---
            is RegisterPatientEvent.NameChanged -> _uiState.update { it.copy(patientName = event.name) }
            is RegisterPatientEvent.DiagnosisChanged -> _uiState.update { it.copy(diagnosisName = event.diagnosis) }
            is RegisterPatientEvent.DobUnknownChanged -> _uiState.update { it.copy(isDobUnknown = event.isUnknown) }

            is RegisterPatientEvent.DobChanged -> _uiState.update {
                val calculatedAge = try {
                    Period.between(event.date, LocalDate.now()).years.toString()
                } catch (e: Exception) {
                    ""
                }

                it.copy(
                    patientDob = event.date,
                    isDobUnknown = false,
                    patientAgeInput = calculatedAge
                )
            }

            is RegisterPatientEvent.AgeChanged -> _uiState.update {
                val calculatedDate = if (event.age.isNotBlank()) {
                    val ageInt = event.age.toLongOrNull() ?: 0
                    LocalDate.now().minusYears(ageInt)
                } else null
                it.copy(
                    patientAgeInput = event.age,
                    patientDob = calculatedDate,
                    isDobUnknown = true
                )
            }

            // --- Paso 2: Gestión Medicamentos ---
            is RegisterPatientEvent.StartAddingMedication -> _uiState.update {
                it.copy(
                    isAddingMedication = true,
                    medicationForm = MedicationFormState()
                )
            }

            is RegisterPatientEvent.CancelAddingMedication -> _uiState.update {
                it.copy(
                    isAddingMedication = false,
                    medicationForm = MedicationFormState()
                )
            }

            is RegisterPatientEvent.SaveMedicationToList -> addMedicationToDraft()
            is RegisterPatientEvent.RemoveMedicationFromList -> {
                _uiState.update { state ->
                    state.copy(addedMedications = state.addedMedications.filterNot { it.tempId == event.tempId })
                }
            }

            // --- Paso 2: Inputs Formulario ---
            is RegisterPatientEvent.MedNameChanged -> updateMedForm { it.copy(name = event.name) }
            is RegisterPatientEvent.MedDoseChanged -> updateMedForm { it.copy(dose = event.dose) }
            is RegisterPatientEvent.MedUnitChanged -> updateMedForm { it.copy(unit = event.unit) }
            is RegisterPatientEvent.MedDurationChanged -> updateMedForm { it.copy(durationDays = event.days) }
            is RegisterPatientEvent.MedInstructionsChanged -> updateMedForm { it.copy(instructions = event.text) }
            is RegisterPatientEvent.MedReasonChanged -> updateMedForm { it.copy(usageReason = event.reason) }

            is RegisterPatientEvent.MedAddFrequencyTime -> updateMedForm {
                val newTimes = (it.frequencyTimes + event.time).sorted()
                it.copy(frequencyTimes = newTimes)
            }

            is RegisterPatientEvent.MedRemoveFrequencyTime -> updateMedForm {
                it.copy(frequencyTimes = it.frequencyTimes - event.time)
            }

            is RegisterPatientEvent.MedUnitsPerPackageChanged -> updateMedForm {
                it.copy(unitsPerPackage = event.value)
            }
            is RegisterPatientEvent.MedPackageCountChanged -> updateMedForm {
                it.copy(packageCount = event.value)
            }

            is RegisterPatientEvent.MedAlertSwitchToggled -> updateMedForm {
                val newThreshold = if (event.enabled) "5" else "0"
                it.copy(stockAlertThreshold = newThreshold)
            }

            // --- Paso 3: Doctor ---
            is RegisterPatientEvent.DoctorNameChanged -> _uiState.update { it.copy(doctorName = event.name) }
            is RegisterPatientEvent.DoctorPhoneChanged -> _uiState.update { it.copy(doctorPhone = event.phone) }

            // --- Paso 4: Historial ---
            is RegisterPatientEvent.EventDescriptionChanged -> _uiState.update {
                it.copy(
                    eventDescription = event.desc
                )
            }

            is RegisterPatientEvent.EventDateChanged -> _uiState.update { it.copy(eventDate = event.date) }
            is RegisterPatientEvent.AddHistoryEvent -> addHistoryToDraft()
            is RegisterPatientEvent.RemoveHistoryEvent -> {
                _uiState.update { state -> state.copy(historyEvents = state.historyEvents.filterNot { it.id == event.logId }) }
            }
        }
    }

    private fun updateMedForm(updater: (MedicationFormState) -> MedicationFormState) {
        _uiState.update { it.copy(medicationForm = updater(it.medicationForm)) }
    }

    private fun validateCurrentStep(): Boolean {
        val state = _uiState.value
        return when (state.currentStep) {
            1 -> {
                var isValid = true
                if (state.patientName.isBlank()) isValid = false
                if (state.patientDob == null) isValid = false
                if (state.diagnosisName.isBlank()) isValid = false

                if (!isValid) _uiState.update { it.copy(error = "Completa los campos obligatorios") }
                isValid
            }

            else -> true
        }
    }

    private fun addMedicationToDraft() {
        val form = _uiState.value.medicationForm
        if (form.name.isBlank()) {
            _uiState.update { it.copy(error = "Nombre del medicamento requerido") }
            return
        }

        val freqDesc = if (form.frequencyTimes.isNotEmpty()) {
            "A las: " + form.frequencyTimes.joinToString(", ") { it.toString() }
        } else {
            "Según indicación"
        }

        val units = form.unitsPerPackage.toDoubleOrNull() ?: 0.0
        val packs = form.packageCount.toDoubleOrNull() ?: 0.0
        val totalStock = units * packs

        val inventoryObj = if (totalStock > 0.0) {
            MedicationInventory(
                currentStock = totalStock,
                // Aquí podríamos usar form.unit (ej: "ampolletas") si queremos ser específicos,
                // o "Unidades" como genérico. Usaremos la unidad que definió el usuario arriba (mg, ml, tabs).
                // Pero ojo: Si la unidad arriba es "mg" (dosis), el stock no son "mg", son "Tabletas".
                // Para este MVP, usaremos "Unidades" genérico para no complicar con otro input de texto.
                unit = "Unidades",
                alertThreshold = form.stockAlertThreshold.toIntOrNull() ?: 0
            )
        } else null

        val draft = DraftMedication(
            tempId = UUID.randomUUID().toString(),
            name = form.name,
            config = MedicationConfig(
                dose = "${form.dose} ${form.unit}",
                frequencyDescription = freqDesc,
                cronExpression = null
            ),
            inventory = inventoryObj,
            instructions = form.instructions.ifBlank { form.usageReason }
        )

        _uiState.update {
            it.copy(
                addedMedications = it.addedMedications + draft,
                isAddingMedication = false,
                medicationForm = MedicationFormState()
            )
        }
    }

    private fun addHistoryToDraft() {
        val state = _uiState.value
        if (state.eventDescription.isBlank()) return

        val log = HistoryLogEntry(
            id = UUID.randomUUID().toString(),
            patientId = "",
            timestamp = Instant.now(),
            authorId = "",
            description = state.eventDescription,
            occurredOn = state.eventDate
        )

        _uiState.update {
            it.copy(
                historyEvents = it.historyEvents + log,
                eventDescription = "",
                eventDate = LocalDate.now()
            )
        }
    }

    private fun submitData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            val state = _uiState.value

            val patientId = UUID.randomUUID().toString()
            val carePlanId = UUID.randomUUID().toString()

            val patient = Patient(
                id = patientId,
                name = state.patientName,
                dob = state.patientDob ?: LocalDate.now(),
                primaryTeamId = "TEMP_TEAM_ID" // TODO: Obtener de UserPreferences
            )

            val carePlan = CarePlan(
                id = carePlanId,
                patientId = patientId,
                name = state.diagnosisName,
                startDate = Instant.now(),
                doctorName = state.doctorName.ifBlank { null },
                doctorPhone = state.doctorPhone.ifBlank { null }
            )

            val medicationsToSave = state.addedMedications.map { draft ->
                Medication(
                    id = UUID.randomUUID().toString(),
                    carePlanId = carePlanId,
                    name = draft.name,
                    type = draft.type,
                    inventory = draft.inventory,
                    config = draft.config,
                    instructions = draft.instructions,
                    createdAt = Instant.now()
                )
            }

            val logsToSave = state.historyEvents.map { log ->
                (log as HistoryLogEntry).copy(
                    id = UUID.randomUUID().toString(),
                    patientId = patientId
                )
            }

            // 2. VERIFICAR CONEXIÓN (Branching Logic)
            if (networkMonitor.isOnline()) {
                // --- FLUJO ONLINE ---
                try {
                    val patientRes = createPatientUseCase(patient)
                    if (patientRes.isFailure) throw patientRes.exceptionOrNull()!!

                    val planRes = createCarePlanUseCase(carePlan)
                    if (planRes.isFailure) throw planRes.exceptionOrNull()!!

                    val medsDeferred = medicationsToSave.map { med ->
                        async { addMedicationUseCase(patientId, med) }
                    }
                    val logsDeferred = logsToSave.map { log ->
                        async { logEventUseCase(log) }
                    }
                    (medsDeferred + logsDeferred).awaitAll()

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isComplete = true,
                            isOfflineMode = false
                        )
                    }

                } catch (e: Exception) {
                    // Fallback: Si falla la red a mitad, podríamos intentar guardar local
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = "Error de red: ${e.message}"
                        )
                    }
                }

            } else {
                // --- FLUJO OFFLINE ---
                // TODO: Aquí implementaremos Room más adelante.

                // saveToLocalDb(patient, carePlan, medicationsToSave, logsToSave)

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isComplete = true,
                        isOfflineMode = true
                    )
                }
            }
        }
    }
}