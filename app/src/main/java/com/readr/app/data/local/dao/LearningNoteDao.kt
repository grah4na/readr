package com.readr.app.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.readr.app.data.model.LearningNote
import kotlinx.coroutines.flow.Flow

@Dao
interface LearningNoteDao {
    @Query("SELECT * FROM learning_notes WHERE entryId = :entryId ORDER BY pageNumber ASC")
    fun getNotesForEntry(entryId: Long): Flow<List<LearningNote>>

    @Query("SELECT * FROM learning_notes WHERE tags LIKE '%' || :tag || '%' ORDER BY dateAdded DESC")
    fun getNotesByTag(tag: String): Flow<List<LearningNote>>

    @Query("SELECT * FROM learning_notes ORDER BY dateAdded DESC")
    fun getAllNotes(): Flow<List<LearningNote>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: LearningNote): Long

    @Update
    suspend fun updateNote(note: LearningNote)

    @Delete
    suspend fun deleteNote(note: LearningNote)
}