package com.poncegl.sigc.ui.feature.tracking.data.model

import java.util.Date

object LogFields {
    const val ID = "id"
    const val PATIENT_ID = "patientId"
    const val CATEGORY = "category"
    const val TIMESTAMP = "timestamp"
    const val AUTHOR_ID = "authorId"
    const val IS_BACKDATED = "isBackdated"
    const val DATA = "data"

    const val DATA_TEXT = "text"
    const val DATA_DATE = "occurredDate"
}

data class LogDto(
    val id: String = "",
    val patientId: String = "",
    val category: String = "",
    val timestamp: Date? = null,
    val authorId: String = "",
    val isBackdated: Boolean = false,
    val data: Map<String, Any> = emptyMap()
) {
    fun toMap(): Map<String, Any?> = mapOf(
        LogFields.ID to id,
        LogFields.PATIENT_ID to patientId,
        LogFields.CATEGORY to category,
        LogFields.TIMESTAMP to timestamp,
        LogFields.AUTHOR_ID to authorId,
        LogFields.IS_BACKDATED to isBackdated,
        LogFields.DATA to data
    )
}