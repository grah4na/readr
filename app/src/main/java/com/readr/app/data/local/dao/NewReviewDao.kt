package com.readr.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.readr.app.data.local.entity.ReviewEntity

@Dao
interface NewReviewDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(review: ReviewEntity)

    @Query("SELECT * FROM reviews WHERE readingLogId = :logId LIMIT 1")
    suspend fun getByLogId(logId: String): ReviewEntity?
}
