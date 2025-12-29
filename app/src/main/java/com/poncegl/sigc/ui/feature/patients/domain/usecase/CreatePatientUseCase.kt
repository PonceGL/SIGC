package com.poncegl.sigc.ui.feature.patients.domain.usecase

import com.poncegl.sigc.ui.feature.patients.domain.model.Patient
import com.poncegl.sigc.ui.feature.patients.domain.repository.PatientRepository
import javax.inject.Inject

class CreatePatientUseCase @Inject constructor(
    private val repository: PatientRepository
) {
    suspend operator fun invoke(patient: Patient): Result<String> {
        if (patient.name.isBlank()) {
            return Result.failure(IllegalArgumentException("El nombre del paciente es obligatorio"))
        }
        if (patient.primaryTeamId.isBlank()) {
            return Result.failure(IllegalArgumentException("El paciente debe pertenecer a un equipo"))
        }
        return repository.createPatient(patient)
    }
}