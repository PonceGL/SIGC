package com.poncegl.sigc.data.repository

import com.poncegl.sigc.data.local.dao.CareLogDao
import com.poncegl.sigc.data.local.dao.DoseDao
import com.poncegl.sigc.data.local.dao.MedicationDao
import com.poncegl.sigc.data.local.dao.PatientDao
import com.poncegl.sigc.data.local.dao.UserDao
import com.poncegl.sigc.data.local.entity.CareLog
import com.poncegl.sigc.data.local.entity.Dose
import com.poncegl.sigc.data.local.entity.Medication
import com.poncegl.sigc.data.local.entity.Patient
import com.poncegl.sigc.data.local.entity.User
import com.poncegl.sigc.domain.repository.SigcRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SigcRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val patientDao: PatientDao,
    private val medicationDao: MedicationDao,
    private val doseDao: DoseDao,
    private val careLogDao: CareLogDao
) : SigcRepository {

    // User
    override fun getAllUsers(): Flow<List<User>> = userDao.getAllUsers()
    override suspend fun getUserById(userId: String): User? = userDao.getUserById(userId)
    override suspend fun getUserByEmail(email: String): User? = userDao.getUserByEmail(email)
    override suspend fun insertUser(user: User) = userDao.insertUser(user)
    override suspend fun updateUser(user: User) = userDao.updateUser(user)
    override suspend fun deleteUser(user: User) = userDao.deleteUser(user)

    // Patient
    override fun getAllPatients(): Flow<List<Patient>> = patientDao.getAllPatients()
    override suspend fun getPatientById(patientId: String): Patient? = patientDao.getPatientById(patientId)
    override suspend fun insertPatient(patient: Patient) = patientDao.insertPatient(patient)
    override suspend fun updatePatient(patient: Patient) = patientDao.updatePatient(patient)
    override suspend fun deletePatient(patient: Patient) = patientDao.deletePatient(patient)

    // Medication
    override fun getMedicationsForPatient(patientId: String): Flow<List<Medication>> = medicationDao.getMedicationsForPatient(patientId)
    override suspend fun getMedicationById(medicationId: String): Medication? = medicationDao.getMedicationById(medicationId)
    override suspend fun insertMedication(medication: Medication) = medicationDao.insertMedication(medication)
    override suspend fun updateMedication(medication: Medication) = medicationDao.updateMedication(medication)
    override suspend fun deleteMedication(medication: Medication) = medicationDao.deleteMedication(medication)

    // Dose
    override fun getDosesForMedication(medicationId: String): Flow<List<Dose>> = doseDao.getDosesForMedication(medicationId)
    override fun getDosesInTimeRange(startTime: Long, endTime: Long): Flow<List<Dose>> = doseDao.getDosesInTimeRange(startTime, endTime)
    override suspend fun getDoseById(doseId: String): Dose? = doseDao.getDoseById(doseId)
    override suspend fun insertDose(dose: Dose) = doseDao.insertDose(dose)
    override suspend fun updateDose(dose: Dose) = doseDao.updateDose(dose)
    override suspend fun deleteDose(dose: Dose) = doseDao.deleteDose(dose)

    // CareLog
    override fun getLogsForPatient(patientId: String): Flow<List<CareLog>> = careLogDao.getLogsForPatient(patientId)
    override suspend fun insertLog(log: CareLog) = careLogDao.insertLog(log)
    override suspend fun updateLog(log: CareLog) = careLogDao.updateLog(log)
    override suspend fun deleteLog(log: CareLog) = careLogDao.deleteLog(log)
}
