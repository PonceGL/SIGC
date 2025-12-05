package com.poncegl.sigc.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.poncegl.sigc.data.local.entity.CareLog
import kotlinx.coroutines.flow.Flow

@Dao
interface CareLogDao {
    @Query("SELECT * FROM care_logs WHERE patientId = :patientId ORDER BY timestamp DESC")
    fun getLogsForPatient(patientId: String): Flow<List<CareLog>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: CareLog)

    @Update
    suspend fun updateLog(log: CareLog)

    @Delete
    suspend fun deleteLog(log: CareLog)
}
