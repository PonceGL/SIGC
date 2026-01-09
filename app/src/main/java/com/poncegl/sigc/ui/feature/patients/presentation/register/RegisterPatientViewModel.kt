package com.poncegl.sigc.ui.feature.patients.presentation.register

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poncegl.sigc.core.util.NetworkMonitor
import com.poncegl.sigc.ui.feature.patients.domain.model.CarePlan
import com.poncegl.sigc.ui.feature.patients.domain.model.Medication
import com.poncegl.sigc.ui.feature.patients.domain.model.MedicationConfig
import com.poncegl.sigc.ui.feature.patients.domain.model.MedicationInventory
import com.poncegl.sigc.ui.feature.patients.domain.model.MedicationPresentation
import com.poncegl.sigc.ui.feature.patients.domain.model.Patient
import com.poncegl.sigc.ui.feature.patients.domain.model.StockStrategy
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
                if (_uiState.value.currentStep > 0) {
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
                val age = try {
                    Period.between(event.date, LocalDate.now()).years.toString()
                } catch (e: Exception) {
                    ""
                }
                it.copy(patientDob = event.date, isDobUnknown = false, patientAgeInput = age)
            }

            is RegisterPatientEvent.AgeChanged -> _uiState.update {
                val date = if (event.age.isNotBlank()) LocalDate.now()
                    .minusYears(event.age.toLongOrNull() ?: 0) else null
                it.copy(patientAgeInput = event.age, patientDob = date, isDobUnknown = true)
            }

            // --- Paso 2: Gestión Medicamentos ---
            is RegisterPatientEvent.StartAddingMedication -> _uiState.update {
                it.copy(isAddingMedication = true, medicationForm = MedicationFormState())
            }

            is RegisterPatientEvent.CancelAddingMedication -> _uiState.update {
                it.copy(isAddingMedication = false, medicationForm = MedicationFormState())
            }

            is RegisterPatientEvent.RemoveMedicationFromList -> {
                _uiState.update { state -> state.copy(addedMedications = state.addedMedications.filterNot { it.tempId == event.tempId }) }
            }

            is RegisterPatientEvent.SaveMedicationToList -> addMedicationToDraft()

            // --- Paso 2: Formulario ---
            is RegisterPatientEvent.MedPresentationChanged -> handlePresentationChange(event.presentation)

            is RegisterPatientEvent.MedNameChanged -> updateMedForm { it.copy(name = event.name) }
            is RegisterPatientEvent.MedConcentrationChanged -> updateMedForm { it.copy(concentration = event.value) }

            is RegisterPatientEvent.MedDoseChanged -> updateMedForm {
                recalculateAlertState(it.copy(dose = event.dose))
            }

            is RegisterPatientEvent.MedUnitChanged -> updateMedForm { it.copy(unit = event.unit) }

            is RegisterPatientEvent.MedAddFrequencyTime -> updateMedForm {
                recalculateAlertState(
                    it.copy(
                        frequencyTimes = (it.frequencyTimes + event.time).distinct().sorted()
                    )
                )
            }

            is RegisterPatientEvent.MedRemoveFrequencyTime -> updateMedForm {
                recalculateAlertState(it.copy(frequencyTimes = it.frequencyTimes - event.time))
            }

            is RegisterPatientEvent.MedToggleFrequencyDay -> updateMedForm { form ->
                val newDays = if (form.frequencyDays.contains(event.dayIndex)) {
                    form.frequencyDays - event.dayIndex
                } else {
                    form.frequencyDays + event.dayIndex
                }
                recalculateAlertState(form.copy(frequencyDays = newDays))
            }

            is RegisterPatientEvent.MedIndefiniteToggled -> updateMedForm { it.copy(isIndefinite = event.isIndefinite) }
            is RegisterPatientEvent.MedDurationChanged -> updateMedForm { it.copy(durationDays = event.days) }

            is RegisterPatientEvent.MedInstructionsChanged -> updateMedForm { it.copy(instructions = event.text) }
            is RegisterPatientEvent.MedReasonChanged -> updateMedForm { it.copy(usageReason = event.reason) }

            // Inventario
            is RegisterPatientEvent.MedUnitsPerPackageChanged -> updateMedForm {
                recalculateAlertState(it.copy(unitsPerPackage = event.value))
            }

            is RegisterPatientEvent.MedPackageCountChanged -> updateMedForm {
                recalculateAlertState(it.copy(packageCount = event.value))
            }

            is RegisterPatientEvent.MedAlertSwitchToggled -> updateMedForm {
                it.copy(isStockAlertEnabled = event.enabled)
            }

            // --- Paso 3 y 4 (Sin cambios) ---
            is RegisterPatientEvent.DoctorNameChanged -> _uiState.update { it.copy(doctorName = event.name) }
            is RegisterPatientEvent.DoctorPhoneChanged -> _uiState.update { it.copy(doctorPhone = event.phone) }
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

    private fun handlePresentationChange(presentation: MedicationPresentation) {
        val inventoryConfig = presentation.getInventoryConfig()

        updateMedForm { form ->
            form.copy(
                presentation = presentation,
                unit = inventoryConfig.defaultUnit,
                unitsPerPackage = "",
                packageCount = "",
                stockAlertThreshold = "",
                stockAlertDescription = ""
            )
        }
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

    private fun recalculateAlertState(form: MedicationFormState): MedicationFormState {
        val strategy = form.presentation.strategy
        val contentSize = form.unitsPerPackage.toDoubleOrNull() ?: 0.0
        val packCount = form.packageCount.toDoubleOrNull() ?: 0.0

        // 1. Cálculo de Stock Real según estrategia
        val currentTotalStock = when (strategy) {
            StockStrategy.CALCULATED -> contentSize * packCount
            StockStrategy.BY_CONTAINER -> packCount
        }

        // Si no hay stock, desactivamos alerta o mostramos vacío
        if (currentTotalStock <= 0.0) {
            return form.copy(
                stockAlertThreshold = "",
                stockAlertDescription = "Ingresa inventario para configurar alerta"
            )
        }

        // 2. Cálculo del Umbral (Threshold)
        var newThreshold = ""
        var newDescription = ""

        when (strategy) {
            StockStrategy.CALCULATED -> {
                // Necesitamos saber consumo diario para estimar 2 días
                val dose = form.dose.toDoubleOrNull() ?: 0.0
                val intakesPerDay = form.frequencyTimes.size
                val activeDaysRatio = form.frequencyDays.size / 7.0

                val dailyConsumption = dose * intakesPerDay * activeDaysRatio

                if (dailyConsumption > 0) {
                    // Regla: Avisar cuando queden 2 días de suministro
                    val threeDaysSupply = dailyConsumption * 2
                    // Redondeamos hacia arriba para seguridad
                    newThreshold = kotlin.math.ceil(threeDaysSupply).toInt().toString()
                    newDescription =
                        "Avisar cuando queden $newThreshold ${form.unit} (aprox. 2 días)"
                } else {
                    // Fallback si faltan datos de dosis/frecuencia
                    newThreshold = "3"
                    newDescription = "Configura dosis para alerta inteligente"
                }
            }

            StockStrategy.BY_CONTAINER -> {
                // Regla fija: Avisar en el último envase
                newThreshold = "1"
                newDescription = "Avisar cuando quede el último envase"
            }
        }

        return form.copy(
            stockAlertThreshold = newThreshold,
            stockAlertDescription = newDescription,
            isStockAlertEnabled = true
        )
    }

    private fun addMedicationToDraft() {
        val form = _uiState.value.medicationForm
        if (form.name.isBlank()) {
            _uiState.update { it.copy(error = "Nombre requerido") }
            return
        }

        val strategy = form.presentation.strategy
        val contentSize = form.unitsPerPackage.toDoubleOrNull() ?: 0.0
        val packCount = form.packageCount.toDoubleOrNull() ?: 0.0

        // --- 1. Construcción del Objeto Inventario ---
        val inventoryObj = if (packCount > 0.0) {
            val quantity = when (strategy) {
                StockStrategy.CALCULATED -> contentSize * packCount
                StockStrategy.BY_CONTAINER -> packCount
            }

            MedicationInventory(
                quantityCurrent = quantity,
                itemSize = contentSize,
                unit = if(strategy == StockStrategy.BY_CONTAINER) "envases" else form.unit,
                alertThreshold = form.stockAlertThreshold.toIntOrNull() ?: 0
            )
        } else null

        // --- 2. Construcción de Configuración ---
        val doseQty = if (strategy == StockStrategy.CALCULATED) form.dose.toDoubleOrNull() else null

        // Descripción amigable de la dosis
        val doseDesc = if (strategy == StockStrategy.CALCULATED) {
            "${form.dose} ${form.unit}"
        } else {
            "Aplicación por envase"
        }

        val freqDesc = if (form.frequencyTimes.isNotEmpty()) {
            val times = form.frequencyTimes.joinToString(", ")
            val daysStr = if(form.frequencyDays.size == 7) "Todos los días" else "Días específicos"
            "$daysStr a las: $times"
        } else "Según indicación"

        val configObj = MedicationConfig(
            doseQuantity = doseQty,
            doseDescription = doseDesc,
            frequencyDescription = freqDesc,
            frequencyDays = form.frequencyDays.toList(),
            isIndefinite = form.isIndefinite
        )

        // --- 3. Creación del Draft ---
        val draft = DraftMedication(
            tempId = UUID.randomUUID().toString(),
            name = form.name,
            presentation = form.presentation,
            config = configObj,
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

        Log.i("ViewModel addMedicationToDraft", "=================================")
        Log.i("ViewModel addMedicationToDraft", "Added medication to draft: ${_uiState.value.patientName}")
        Log.i("ViewModel addMedicationToDraft", "Added medication to draft: ${_uiState.value.addedMedications}")
        Log.i("ViewModel addMedicationToDraft", "=================================")
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
                    presentation = draft.presentation,
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