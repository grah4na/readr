package com.readr.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.readr.app.data.local.entity.NoteEntity

@Dao
interface NewNoteDao {
    @Insert
    suspend fun insert(note: NoteEntity)

    @Query("SELECT * FROM notes WHERE readingLogId = :logId ORDER BY createdAt DESC")
    suspend fun getByLogId(logId: String): List<NoteEntity>

    @Query("SELECT * FROM notes WHERE readingLogId = :logId AND text LIKE '%' || :query || '%'")
    suspend fun searchInLog(logId: String, query: String): List<NoteEntity>
}
