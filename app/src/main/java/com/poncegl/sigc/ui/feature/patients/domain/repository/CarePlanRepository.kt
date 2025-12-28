package com.poncegl.sigc.ui.feature.patients.domain.repository

import com.poncegl.sigc.ui.feature.patients.domain.model.CarePlan
import com.poncegl.sigc.ui.feature.patients.domain.model.Medication
import kotlinx.coroutines.flow.Flow

interface CarePlanRepository {
    // --- Care Plans (Diagnósticos) ---

    /**
     * Obtiene todos los planes de cuidado activos de un paciente.
     */
    fun getCarePlansByPatient(patientId: String): Flow<List<CarePlan>>

    /**
     * Crea un plan inicial (ej. el diagnóstico del Wizard).
     */
    suspend fun createCarePlan(carePlan: CarePlan): Result<String>

    /**
     * Actualiza un plan (ej. agregar doctor o finalizar tratamiento).
     */
    suspend fun updateCarePlan(carePlan: CarePlan): Result<Unit>


    // --- Medications (Medicamentos) ---

    /**
     * Obtiene los medicamentos asociados a un plan específico.
     * @param patientId ID del paciente (necesario para la ruta).
     */
    fun getMedicationsByPlan(patientId: String, carePlanId: String): Flow<List<Medication>>

    /**
     * Agrega un medicamento al inventario/plan del paciente.
     * @param patientId ID del paciente (necesario para la ruta).
     * @param medication El objeto medicamento a guardar.
     */
    suspend fun addMedication(patientId: String, medication: Medication): Result<String>

    /**
     * Actualiza un medicamento (ej. cambiar dosis o stock).
     * @param patientId ID del paciente (necesario para la ruta).
     */
    suspend fun updateMedication(patientId: String, medication: Medication): Result<Unit>

    /**
     * Elimina un medicamento.
     * @param patientId ID del paciente (necesario para la ruta).
     */
    suspend fun deleteMedication(
        patientId: String,
        carePlanId: String,
        medicationId: String
    ): Result<Unit>
}