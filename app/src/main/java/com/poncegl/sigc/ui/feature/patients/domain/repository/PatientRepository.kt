package com.poncegl.sigc.ui.feature.patients.domain.repository

import com.poncegl.sigc.ui.feature.patients.domain.model.Patient
import kotlinx.coroutines.flow.Flow

interface PatientRepository {
    /**
     * Obtiene la lista de pacientes de un equipo específico en tiempo real.
     */
    fun getPatientsByTeam(teamId: String): Flow<List<Patient>>

    /**
     * Obtiene un paciente específico por su ID.
     */
    fun getPatientById(patientId: String): Flow<Patient?>

    /**
     * Crea un nuevo paciente.
     * @return Result con el ID del paciente creado o error.
     */
    suspend fun createPatient(patient: Patient): Result<String>

    /**
     * Actualiza datos parciales de un paciente.
     */
    suspend fun updatePatient(patient: Patient): Result<Unit>

    /**
     * Borrado lógico o físico (dependiendo de la regla de negocio).
     */
    suspend fun deletePatient(patientId: String): Result<Unit>
}