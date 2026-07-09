package com.readr.app.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.readr.app.data.model.ReadingSession
import kotlinx.coroutines.flow.Flow

@Dao
interface ReadingSessionDao {
    @Query("SELECT * FROM reading_sessions WHERE entryId = :entryId ORDER BY date DESC")
    fun getSessionsForEntry(entryId: Long): Flow<List<ReadingSession>>

    @Query("SELECT * FROM reading_sessions ORDER BY date DESC")
    fun getAllSessions(): Flow<List<ReadingSession>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: ReadingSession): Long

    @Update
    suspend fun updateSession(session: ReadingSession)

    @Delete
    suspend fun deleteSession(session: ReadingSession)

    @Query("DELETE FROM reading_sessions WHERE entryId = :entryId")
    suspend fun deleteSessionsForEntry(entryId: Long)
}