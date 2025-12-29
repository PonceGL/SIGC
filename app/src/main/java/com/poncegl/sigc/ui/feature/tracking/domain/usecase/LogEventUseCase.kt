package com.poncegl.sigc.ui.feature.tracking.domain.usecase

import com.poncegl.sigc.ui.feature.tracking.domain.model.LogEntry
import com.poncegl.sigc.ui.feature.tracking.domain.repository.TrackingRepository
import javax.inject.Inject

class LogEventUseCase @Inject constructor(
    private val repository: TrackingRepository
) {
    suspend operator fun invoke(log: LogEntry): Result<String> {
        if (log.patientId.isBlank()) {
            return Result.failure(IllegalArgumentException("El evento debe estar asociado a un paciente"))
        }
        return repository.createLog(log)
    }
}