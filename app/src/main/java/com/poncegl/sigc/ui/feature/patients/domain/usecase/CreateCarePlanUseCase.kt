package com.poncegl.sigc.ui.feature.patients.domain.usecase

import com.poncegl.sigc.ui.feature.patients.domain.model.CarePlan
import com.poncegl.sigc.ui.feature.patients.domain.repository.CarePlanRepository
import javax.inject.Inject

class CreateCarePlanUseCase @Inject constructor(
    private val repository: CarePlanRepository
) {
    suspend operator fun invoke(carePlan: CarePlan): Result<String> {
        if (carePlan.patientId.isBlank()) {
            return Result.failure(IllegalArgumentException("El plan debe estar asociado a un paciente válido"))
        }
        // TODO: Agregar lógica para validar que startDate no sea posterior a endDate si este existe
        return repository.createCarePlan(carePlan)
    }
}