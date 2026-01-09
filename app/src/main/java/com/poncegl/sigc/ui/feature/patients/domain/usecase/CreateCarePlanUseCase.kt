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

        if (carePlan.endDate != null && carePlan.startDate.isAfter(carePlan.endDate)) {
            return Result.failure(IllegalArgumentException("La fecha de inicio no puede ser posterior a la fecha de fin"))
        }

        return repository.createCarePlan(carePlan)
    }
}