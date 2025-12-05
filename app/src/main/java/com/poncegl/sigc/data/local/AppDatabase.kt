package com.poncegl.sigc.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
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

@Database(
    entities = [
        User::class,
        Patient::class,
        Medication::class,
        Dose::class,
        CareLog::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun patientDao(): PatientDao
    abstract fun medicationDao(): MedicationDao
    abstract fun doseDao(): DoseDao
    abstract fun careLogDao(): CareLogDao
}
