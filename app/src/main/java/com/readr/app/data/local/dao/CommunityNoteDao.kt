package com.readr.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.readr.app.data.local.entity.CommunityNoteEntity

@Dao
interface CommunityNoteDao {
    @Insert
    suspend fun insert(note: CommunityNoteEntity)

    @Query("SELECT * FROM community_notes WHERE workId = :workId")
    suspend fun getByWorkId(workId: String): List<CommunityNoteEntity>
}
