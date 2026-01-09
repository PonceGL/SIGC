package com.poncegl.sigc.ui.feature.patients.domain.usecase

import com.poncegl.sigc.ui.feature.patients.domain.model.Medication
import com.poncegl.sigc.ui.feature.patients.domain.repository.CarePlanRepository
import javax.inject.Inject

class AddMedicationUseCase @Inject constructor(
    private val repository: CarePlanRepository
) {
    /**
     * @param patientId ID del paciente (contexto necesario para Firestore)
     * @param medication Objeto medicamento a guardar
     */
    suspend operator fun invoke(patientId: String, medication: Medication): Result<String> {
        if (medication.carePlanId.isBlank()) {
            return Result.failure(IllegalArgumentException("El medicamento debe pertenecer a un plan de cuidados"))
        }
        if (medication.name.isBlank()) {
            return Result.failure(IllegalArgumentException("El nombre del medicamento es obligatorio"))
        }

        return repository.addMedication(patientId, medication)
    }
}