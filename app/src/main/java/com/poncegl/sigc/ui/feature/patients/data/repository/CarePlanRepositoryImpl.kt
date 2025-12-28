package com.poncegl.sigc.ui.feature.patients.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import com.poncegl.sigc.core.constants.FirestoreConstants
import com.poncegl.sigc.ui.feature.patients.data.mapper.toDomain
import com.poncegl.sigc.ui.feature.patients.data.mapper.toDto
import com.poncegl.sigc.ui.feature.patients.data.model.CarePlanDto
import com.poncegl.sigc.ui.feature.patients.data.model.MedicationDto
import com.poncegl.sigc.ui.feature.patients.domain.model.CarePlan
import com.poncegl.sigc.ui.feature.patients.domain.model.Medication
import com.poncegl.sigc.ui.feature.patients.domain.repository.CarePlanRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class CarePlanRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : CarePlanRepository {

    private fun getCarePlansCollection(patientId: String) = firestore
        .collection(FirestoreConstants.PATIENTS_COLLECTION)
        .document(patientId)
        .collection(FirestoreConstants.CARE_PLANS_COLLECTION)

    // --- Care Plans ---

    override fun getCarePlansByPatient(patientId: String): Flow<List<CarePlan>> {
        return getCarePlansCollection(patientId)
            .snapshots()
            .map { snapshot ->
                snapshot.documents.mapNotNull { it.toObject(CarePlanDto::class.java)?.toDomain() }
            }
    }

    override suspend fun createCarePlan(carePlan: CarePlan): Result<String> = try {
        val collection = getCarePlansCollection(carePlan.patientId)
        val id = carePlan.id.ifBlank { collection.document().id }
        val finalPlan = carePlan.copy(id = id)

        collection.document(id).set(finalPlan.toDto()).await()
        Result.success(id)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun updateCarePlan(carePlan: CarePlan): Result<Unit> = try {
        getCarePlansCollection(carePlan.patientId)
            .document(carePlan.id)
            .set(carePlan.toDto())
            .await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    // --- Medications ---

    private fun getMedicationsCollection(patientId: String, carePlanId: String) =
        getCarePlansCollection(patientId)
            .document(carePlanId)
            .collection(FirestoreConstants.MEDICATIONS_COLLECTION)

    override fun getMedicationsByPlan(
        patientId: String,
        carePlanId: String
    ): Flow<List<Medication>> {
        return getMedicationsCollection(patientId, carePlanId)
            .snapshots()
            .map { snapshot ->
                snapshot.documents.mapNotNull { it.toObject(MedicationDto::class.java)?.toDomain() }
            }
    }

    override suspend fun addMedication(patientId: String, medication: Medication): Result<String> =
        try {
            val collection = getMedicationsCollection(patientId, medication.carePlanId)
            val id = medication.id.ifBlank { collection.document().id }
            val finalMedication = medication.copy(id = id)

            collection.document(id).set(finalMedication.toDto()).await()
            Result.success(id)
        } catch (e: Exception) {
            Result.failure(e)
        }

    override suspend fun updateMedication(patientId: String, medication: Medication): Result<Unit> =
        try {
            getMedicationsCollection(patientId, medication.carePlanId)
                .document(medication.id)
                .set(medication.toDto())
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }

    override suspend fun deleteMedication(
        patientId: String,
        carePlanId: String,
        medicationId: String
    ): Result<Unit> = try {
        getMedicationsCollection(patientId, carePlanId)
            .document(medicationId)
            .delete()
            .await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }
}