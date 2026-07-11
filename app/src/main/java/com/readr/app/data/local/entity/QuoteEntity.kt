package com.readr.app.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "quotes",
    indices = [Index("readingLogId")]
)
data class QuoteEntity(
    @PrimaryKey val id: String,
    val readingLogId: String,
    val text: String,
    val pageNumber: Int?,
    val createdAt: Long
)
