package com.readr.app.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "reviews_old",
    foreignKeys = [ForeignKey(
        entity = ReadingEntry::class,
        parentColumns = ["id"],
        childColumns = ["entryId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("entryId")]
)
data class Review(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val entryId: Long,
    val content: String,
    val spoilerStartPage: Int = 0,
    val spoilerEndPage: Int = 0,
    val hasSpoilers: Boolean = false,
    val dateWritten: Long = System.currentTimeMillis()
)