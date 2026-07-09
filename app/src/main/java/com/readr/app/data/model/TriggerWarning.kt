package com.readr.app.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "trigger_warnings",
    foreignKeys = [ForeignKey(
        entity = ReadingEntry::class,
        parentColumns = ["id"],
        childColumns = ["entryId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("entryId")]
)
data class TriggerWarning(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val entryId: Long,
    val label: String,
    val description: String = "",
    val startPercent: Float = 0f,
    val endPercent: Float = 1f,
    val dateAdded: Long = System.currentTimeMillis()
)