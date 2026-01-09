package com.poncegl.sigc.ui.feature.patients.domain.usecase

import com.poncegl.sigc.ui.feature.patients.domain.model.CarePlan
import com.poncegl.sigc.ui.feature.patients.domain.repository.CarePlanRepository
import javax.inject.Inject

class UpdateCarePlanUseCase @Inject constructor(
    private val repository: CarePlanRepository
) {
    suspend operator fun invoke(carePlan: CarePlan): Result<Unit> {
        return repository.updateCarePlan(carePlan)
    }
}