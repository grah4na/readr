package com.readr.app.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "reviews",
    indices = [Index("readingLogId")]
)
data class ReviewEntity(
    @PrimaryKey val id: String,
    val readingLogId: String,
    val rating: Int,
    val reviewText: String?,
    val spoilerPercent: Float?,
    val whatILearned: String?,
    val createdAt: Long
)
