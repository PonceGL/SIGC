package com.poncegl.sigc.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(
    tableName = "care_logs",
    foreignKeys = [
        ForeignKey(
            entity = Patient::class,
            parentColumns = ["id"],
            childColumns = ["patientId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("patientId"), Index("userId")]
)
data class CareLog(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val patientId: String,
    val userId: String,
    val timestamp: Long,
    val type: String, // VITAL, NOTE, INCIDENT
    val data: String // JSON or structured text
)
