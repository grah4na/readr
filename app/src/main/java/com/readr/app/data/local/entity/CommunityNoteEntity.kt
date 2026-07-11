package com.readr.app.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "community_notes",
    indices = [Index("workId")]
)
data class CommunityNoteEntity(
    @PrimaryKey val id: String,
    val workId: String,
    val type: String,
    val startPercent: Float,
    val endPercent: Float,
    val noteText: String,
    val createdAt: Long
)
