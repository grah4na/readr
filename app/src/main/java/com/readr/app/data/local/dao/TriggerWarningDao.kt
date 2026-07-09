package com.readr.app.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.readr.app.data.model.TriggerWarning
import kotlinx.coroutines.flow.Flow

@Dao
interface TriggerWarningDao {
    @Query("SELECT * FROM trigger_warnings WHERE entryId = :entryId ORDER BY startPercent ASC")
    fun getWarningsForEntry(entryId: Long): Flow<List<TriggerWarning>>

    @Query("SELECT * FROM trigger_warnings ORDER BY dateAdded DESC")
    fun getAllWarnings(): Flow<List<TriggerWarning>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWarning(warning: TriggerWarning): Long

    @Update
    suspend fun updateWarning(warning: TriggerWarning)

    @Delete
    suspend fun deleteWarning(warning: TriggerWarning)
}