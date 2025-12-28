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
     */
    fun getMedicationsByPlan(carePlanId: String): Flow<List<Medication>>

    /**
     * Agrega un medicamento al inventario/plan del paciente.
     */
    suspend fun addMedication(medication: Medication): Result<String>

    /**
     * Actualiza un medicamento (ej. cambiar dosis o stock).
     */
    suspend fun updateMedication(medication: Medication): Result<Unit>

    /**
     * Elimina un medicamento.
     */
    suspend fun deleteMedication(medicationId: String, carePlanId: String): Result<Unit>
}