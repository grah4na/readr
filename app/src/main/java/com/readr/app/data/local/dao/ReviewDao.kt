package com.readr.app.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.readr.app.data.model.Review
import kotlinx.coroutines.flow.Flow

@Dao
interface ReviewDao {
    @Query("SELECT * FROM reviews_old WHERE entryId = :entryId LIMIT 1")
    fun getReviewForEntry(entryId: Long): Flow<Review?>

    @Query("SELECT * FROM reviews_old ORDER BY dateWritten DESC")
    fun getAllReviews(): Flow<List<Review>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReview(review: Review): Long

    @Update
    suspend fun updateReview(review: Review)

    @Delete
    suspend fun deleteReview(review: Review)
}