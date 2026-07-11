package com.readr.app.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "quotes_old",
    foreignKeys = [ForeignKey(
        entity = ReadingEntry::class,
        parentColumns = ["id"],
        childColumns = ["entryId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("entryId")]
)
data class Quote(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val entryId: Long,
    val text: String,
    val pageNumber: Int = 0,
    val dateAdded: Long = System.currentTimeMillis()
)