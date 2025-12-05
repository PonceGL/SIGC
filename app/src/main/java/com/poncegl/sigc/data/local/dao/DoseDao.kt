package com.poncegl.sigc.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.poncegl.sigc.data.local.entity.Dose
import kotlinx.coroutines.flow.Flow

@Dao
interface DoseDao {
    @Query("SELECT * FROM doses WHERE medicationId = :medicationId")
    fun getDosesForMedication(medicationId: String): Flow<List<Dose>>

    @Query("SELECT * FROM doses WHERE scheduledTime BETWEEN :startTime AND :endTime")
    fun getDosesInTimeRange(startTime: Long, endTime: Long): Flow<List<Dose>>

    @Query("SELECT * FROM doses WHERE id = :doseId")
    suspend fun getDoseById(doseId: String): Dose?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDose(dose: Dose)

    @Update
    suspend fun updateDose(dose: Dose)

    @Delete
    suspend fun deleteDose(dose: Dose)
}
