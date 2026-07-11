package com.readr.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.readr.app.data.local.entity.QuoteEntity

@Dao
interface NewQuoteDao {
    @Insert
    suspend fun insert(quote: QuoteEntity)

    @Query("SELECT * FROM quotes WHERE readingLogId = :logId ORDER BY createdAt DESC")
    suspend fun getByLogId(logId: String): List<QuoteEntity>
}
