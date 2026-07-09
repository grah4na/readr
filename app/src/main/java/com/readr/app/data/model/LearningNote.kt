package com.readr.app.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "learning_notes",
    foreignKeys = [ForeignKey(
        entity = ReadingEntry::class,
        parentColumns = ["id"],
        childColumns = ["entryId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("entryId")]
)
data class LearningNote(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val entryId: Long,
    val content: String,
    val pageNumber: Int = 0,
    val tags: String = "",
    val dateAdded: Long = System.currentTimeMillis()
)