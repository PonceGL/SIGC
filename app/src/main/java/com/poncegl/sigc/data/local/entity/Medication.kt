package com.poncegl.sigc.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(
    tableName = "medications",
    foreignKeys = [
        ForeignKey(
            entity = Patient::class,
            parentColumns = ["id"],
            childColumns = ["patientId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("patientId")]
)
data class Medication(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val patientId: String,
    val name: String,
    val dosage: String,
    val frequency: String,
    val stock: Int,
    val unit: String
)
