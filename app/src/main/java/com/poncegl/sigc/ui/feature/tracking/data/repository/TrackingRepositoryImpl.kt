package com.poncegl.sigc.ui.feature.tracking.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.snapshots
import com.poncegl.sigc.core.constants.FirestoreConstants
import com.poncegl.sigc.ui.feature.tracking.data.mapper.toDomain
import com.poncegl.sigc.ui.feature.tracking.data.mapper.toDto
import com.poncegl.sigc.ui.feature.tracking.data.model.LogDto
import com.poncegl.sigc.ui.feature.tracking.data.model.LogFields
import com.poncegl.sigc.ui.feature.tracking.domain.model.LogCategory
import com.poncegl.sigc.ui.feature.tracking.domain.model.LogEntry
import com.poncegl.sigc.ui.feature.tracking.domain.repository.TrackingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class TrackingRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : TrackingRepository {

    private fun getLogsCollection(patientId: String) = firestore
        .collection(FirestoreConstants.PATIENTS_COLLECTION)
        .document(patientId)
        .collection(FirestoreConstants.LOGS_COLLECTION)

    override suspend fun createLog(log: LogEntry): Result<String> = try {
        val collection = getLogsCollection(log.patientId)
        val id = log.id.ifBlank { collection.document().id }

        val dto = log.toDto().copy(id = id)

        collection.document(id).set(dto).await()
        Result.success(id)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override fun getAllLogsByPatient(patientId: String): Flow<List<LogEntry>> {
        return getLogsCollection(patientId)
            .orderBy(LogFields.TIMESTAMP, Query.Direction.DESCENDING)
            .snapshots()
            .map { snapshot ->
                snapshot.documents.mapNotNull { it.toObject(LogDto::class.java)?.toDomain() }
            }
    }

    override fun getLogsByCategory(patientId: String, category: LogCategory): Flow<List<LogEntry>> {
        return getLogsCollection(patientId)
            .whereEqualTo(LogFields.CATEGORY, category.name)
            .orderBy(LogFields.TIMESTAMP, Query.Direction.DESCENDING)
            .snapshots()
            .map { snapshot ->
                snapshot.documents.mapNotNull { it.toObject(LogDto::class.java)?.toDomain() }
            }
    }

    override suspend fun deleteLog(patientId: String, logId: String): Result<Unit> = try {
        getLogsCollection(patientId)
            .document(logId)
            .delete()
            .await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }
}