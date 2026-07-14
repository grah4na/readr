package com.readr.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.readr.app.data.local.entity.UserProfileEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserProfileDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(profile: UserProfileEntity)

    @Query("SELECT * FROM user_profile WHERE id = 'me'")
    fun getProfile(): Flow<UserProfileEntity?>

    @Query("UPDATE user_profile SET displayName = :name WHERE id = 'me'")
    suspend fun updateDisplayName(name: String)

    @Query("UPDATE user_profile SET bio = :bio WHERE id = 'me'")
    suspend fun updateBio(bio: String)

    @Query("UPDATE user_profile SET pronouns = :pronouns WHERE id = 'me'")
    suspend fun updatePronouns(pronouns: String)

    @Query("UPDATE user_profile SET profilePhotoUri = :uri WHERE id = 'me'")
    suspend fun updateProfilePhotoUri(uri: String)
}
