package com.poncegl.sigc.ui.feature.patients.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import com.poncegl.sigc.core.constants.FirestoreConstants
import com.poncegl.sigc.ui.feature.patients.data.mapper.toDomain
import com.poncegl.sigc.ui.feature.patients.data.mapper.toDto
import com.poncegl.sigc.ui.feature.patients.data.model.PatientDto
import com.poncegl.sigc.ui.feature.patients.data.model.PatientFields
import com.poncegl.sigc.ui.feature.patients.domain.model.Patient
import com.poncegl.sigc.ui.feature.patients.domain.repository.PatientRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class PatientRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : PatientRepository {

    override fun getPatientsByTeam(teamId: String): Flow<List<Patient>> {
        return firestore.collection(FirestoreConstants.PATIENTS_COLLECTION)
            .whereEqualTo(PatientFields.PRIMARY_TEAM_ID, teamId)
            .snapshots()
            .map { snapshot ->
                snapshot.documents.mapNotNull { doc ->
                    doc.toObject(PatientDto::class.java)?.toDomain()
                }
            }
    }

    override fun getPatientById(patientId: String): Flow<Patient?> {
        return firestore.collection(FirestoreConstants.PATIENTS_COLLECTION)
            .document(patientId)
            .snapshots()
            .map { doc ->
                doc.toObject(PatientDto::class.java)?.toDomain()
            }
    }

    override suspend fun createPatient(patient: Patient): Result<String> = try {
        val documentId = patient.id.ifBlank {
            firestore.collection(FirestoreConstants.PATIENTS_COLLECTION).document().id
        }
        val patientToSave = patient.copy(id = documentId)

        firestore.collection(FirestoreConstants.PATIENTS_COLLECTION)
            .document(documentId)
            .set(patientToSave.toDto())
            .await()

        Result.success(documentId)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun updatePatient(patient: Patient): Result<Unit> = try {
        firestore.collection(FirestoreConstants.PATIENTS_COLLECTION)
            .document(patient.id)
            .set(patient.toDto())
            .await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun deletePatient(patientId: String): Result<Unit> = try {
        firestore.collection(FirestoreConstants.PATIENTS_COLLECTION)
            .document(patientId)
            .delete()
            .await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }
}