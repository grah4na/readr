package com.readr.app.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.readr.app.data.model.EntryType
import com.readr.app.data.model.ReadingEntry
import kotlinx.coroutines.flow.Flow

@Dao
interface ReadingEntryDao {
    @Query("SELECT * FROM reading_entries ORDER BY dateAdded DESC")
    fun getAllEntries(): Flow<List<ReadingEntry>>

    @Query("SELECT * FROM reading_entries WHERE type = :type ORDER BY dateAdded DESC")
    fun getEntriesByType(type: EntryType): Flow<List<ReadingEntry>>

    @Query("SELECT * FROM reading_entries WHERE id = :id")
    suspend fun getEntryById(id: Long): ReadingEntry?

    @Query("SELECT * FROM reading_entries WHERE progress > 0 AND progress < 1 ORDER BY dateAdded DESC")
    fun getCurrentlyReading(): Flow<List<ReadingEntry>>

    @Query("SELECT * FROM reading_entries WHERE progress = 1 ORDER BY dateFinished DESC")
    fun getFinishedEntries(): Flow<List<ReadingEntry>>

    @Query("SELECT * FROM reading_entries WHERE progress = 0 ORDER BY dateAdded DESC")
    fun getWantToRead(): Flow<List<ReadingEntry>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntry(entry: ReadingEntry): Long

    @Update
    suspend fun updateEntry(entry: ReadingEntry)

    @Delete
    suspend fun deleteEntry(entry: ReadingEntry)

    @Query("DELETE FROM reading_entries WHERE id = :id")
    suspend fun deleteEntryById(id: Long)
}