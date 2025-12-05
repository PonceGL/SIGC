package com.poncegl.sigc.domain.repository

import com.poncegl.sigc.data.local.entity.CareLog
import com.poncegl.sigc.data.local.entity.Dose
import com.poncegl.sigc.data.local.entity.Medication
import com.poncegl.sigc.data.local.entity.Patient
import com.poncegl.sigc.data.local.entity.User
import kotlinx.coroutines.flow.Flow

interface SigcRepository {
    // User
    fun getAllUsers(): Flow<List<User>>
    suspend fun getUserById(userId: String): User?
    suspend fun getUserByEmail(email: String): User?
    suspend fun insertUser(user: User)
    suspend fun updateUser(user: User)
    suspend fun deleteUser(user: User)

    // Patient
    fun getAllPatients(): Flow<List<Patient>>
    suspend fun getPatientById(patientId: String): Patient?
    suspend fun insertPatient(patient: Patient)
    suspend fun updatePatient(patient: Patient)
    suspend fun deletePatient(patient: Patient)

    // Medication
    fun getMedicationsForPatient(patientId: String): Flow<List<Medication>>
    suspend fun getMedicationById(medicationId: String): Medication?
    suspend fun insertMedication(medication: Medication)
    suspend fun updateMedication(medication: Medication)
    suspend fun deleteMedication(medication: Medication)

    // Dose
    fun getDosesForMedication(medicationId: String): Flow<List<Dose>>
    fun getDosesInTimeRange(startTime: Long, endTime: Long): Flow<List<Dose>>
    suspend fun getDoseById(doseId: String): Dose?
    suspend fun insertDose(dose: Dose)
    suspend fun updateDose(dose: Dose)
    suspend fun deleteDose(dose: Dose)

    // CareLog
    fun getLogsForPatient(patientId: String): Flow<List<CareLog>>
    suspend fun insertLog(log: CareLog)
    suspend fun updateLog(log: CareLog)
    suspend fun deleteLog(log: CareLog)
}
