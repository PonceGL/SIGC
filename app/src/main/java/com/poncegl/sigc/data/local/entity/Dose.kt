package com.poncegl.sigc.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(
    tableName = "doses",
    foreignKeys = [
        ForeignKey(
            entity = Medication::class,
            parentColumns = ["id"],
            childColumns = ["medicationId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("medicationId")]
)
data class Dose(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val medicationId: String,
    val scheduledTime: Long,
    val status: String, // PENDING, TAKEN, SKIPPED
    val takenTime: Long? = null,
    val takenByUserId: String? = null
)
