package com.readr.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reading_entries")
data class ReadingEntry(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val type: EntryType,
    val title: String,
    val author: String,
    val coverUrl: String = "",
    val progress: Float = 0f,
    val rating: Int = 0,
    val isbn: String = "",
    val pages: Int = 0,
    val articleUrl: String = "",
    val siteName: String = "",
    val dateStarted: Long = 0L,
    val dateFinished: Long = 0L,
    val dateAdded: Long = System.currentTimeMillis()
)

enum class EntryType {
    BOOK, ARTICLE
}