package com.poncegl.sigc.ui.feature.tracking.data.mapper

import com.poncegl.sigc.ui.feature.tracking.data.model.LogDto
import com.poncegl.sigc.ui.feature.tracking.data.model.LogFields
import com.poncegl.sigc.ui.feature.tracking.domain.model.HistoryLogEntry
import com.poncegl.sigc.ui.feature.tracking.domain.model.LogCategory
import com.poncegl.sigc.ui.feature.tracking.domain.model.LogEntry
import com.poncegl.sigc.ui.feature.tracking.domain.model.NoteLogEntry
import com.poncegl.sigc.ui.feature.tracking.domain.model.VitalSignLogEntry
import com.poncegl.sigc.ui.feature.tracking.domain.model.VitalSignType
import java.time.Instant
import java.time.ZoneId
import java.util.Date

fun LogDto.toDomain(): LogEntry? {
    val catEnum = try {
        LogCategory.valueOf(this.category)
    } catch (e: Exception) {
        return null
    }

    val instant = this.timestamp?.toInstant() ?: Instant.now()

    return when (catEnum) {
        LogCategory.HISTORY -> {
            val dateMillis =
                (this.data[LogFields.DATA_DATE] as? Number)?.toLong() ?: System.currentTimeMillis()
            HistoryLogEntry(
                id = this.id,
                patientId = this.patientId,
                timestamp = instant,
                authorId = this.authorId,
                description = (this.data[LogFields.DATA_TEXT] as? String) ?: "",
                occurredOn = Instant.ofEpochMilli(dateMillis).atZone(ZoneId.systemDefault())
                    .toLocalDate()
            )
        }

        LogCategory.NOTE -> {
            NoteLogEntry(
                id = this.id,
                patientId = this.patientId,
                timestamp = instant,
                authorId = this.authorId,
                text = (this.data[LogFields.DATA_TEXT] as? String) ?: ""
            )
        }

        LogCategory.VITAL_SIGN -> {
            val typeStr = (this.data["type"] as? String) ?: return null
            val type = try {
                VitalSignType.valueOf(typeStr)
            } catch (e: Exception) {
                return null
            }

            VitalSignLogEntry(
                id = this.id,
                patientId = this.patientId,
                timestamp = instant,
                authorId = this.authorId,
                type = type,
                value1 = (this.data["value1"] as? Number)?.toDouble() ?: 0.0,
                value2 = (this.data["value2"] as? Number)?.toDouble(),
                unit = (this.data["unit"] as? String) ?: ""
            )
        }
        // Para Incident u otros futuros, agregarlos aquí
        else -> null
    }
}

fun LogEntry.toDto(): LogDto {
    val dataMap = mutableMapOf<String, Any>()

    when (this) {
        is HistoryLogEntry -> {
            dataMap[LogFields.DATA_TEXT] = this.description
            dataMap[LogFields.DATA_DATE] =
                this.occurredOn.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        }

        is NoteLogEntry -> {
            dataMap[LogFields.DATA_TEXT] = this.text
        }

        is VitalSignLogEntry -> {
            dataMap["type"] = this.type.name
            dataMap["value1"] = this.value1
            this.value2?.let { dataMap["value2"] = it }
            dataMap["unit"] = this.unit
        }
    }

    return LogDto(
        id = this.id,
        patientId = this.patientId,
        category = this.category.name,
        timestamp = Date.from(this.timestamp),
        authorId = this.authorId,
        isBackdated = this.category == LogCategory.HISTORY,
        data = dataMap
    )
}