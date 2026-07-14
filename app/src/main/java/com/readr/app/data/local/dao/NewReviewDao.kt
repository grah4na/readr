package com.readr.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.readr.app.data.local.entity.ReviewEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NewReviewDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(review: ReviewEntity)

    @Query("SELECT * FROM reviews WHERE readingLogId = :logId LIMIT 1")
    suspend fun getByLogId(logId: String): ReviewEntity?

    @Query("SELECT COALESCE(AVG(CAST(rating AS FLOAT)), 0) FROM reviews WHERE rating > 0")
    fun getAverageRating(): Flow<Float>

    @Query("SELECT * FROM reviews")
    fun getAllReviews(): Flow<List<ReviewEntity>>
}
