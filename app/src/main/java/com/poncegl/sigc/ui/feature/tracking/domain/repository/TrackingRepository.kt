package com.poncegl.sigc.ui.feature.tracking.domain.repository

import com.poncegl.sigc.ui.feature.tracking.domain.model.LogCategory
import com.poncegl.sigc.ui.feature.tracking.domain.model.LogEntry
import kotlinx.coroutines.flow.Flow

interface TrackingRepository {
    /**
     * Guarda un nuevo registro en la bitácora.
     * Puede ser una Nota, un Signo Vital, un Evento Histórico, etc.
     * @return Result con el ID del log creado.
     */
    suspend fun createLog(log: LogEntry): Result<String>

    /**
     * Obtiene todos los logs de un paciente ordenados por fecha (del más reciente al más antiguo).
     * Útil para la vista general de "Bitácora".
     */
    fun getAllLogsByPatient(patientId: String): Flow<List<LogEntry>>

    /**
     * Obtiene logs filtrados por categoría.
     * Ej: Solo traer VITAL_SIGN para graficar la presión arterial.
     */
    fun getLogsByCategory(patientId: String, category: LogCategory): Flow<List<LogEntry>>

    /**
     * Elimina un registro (si el usuario tiene permisos).
     */
    suspend fun deleteLog(logId: String): Result<Unit>
}