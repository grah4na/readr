package com.readr.app.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "notes",
    indices = [Index("readingLogId")]
)
data class NoteEntity(
    @PrimaryKey val id: String,
    val readingLogId: String,
    val text: String,
    val pageNumber: Int?,
    val tags: String?,
    val type: String,
    val createdAt: Long
)
