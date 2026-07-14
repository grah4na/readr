package com.readr.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey val id: String = "me",
    val displayName: String? = null,
    val bio: String? = null,
    val pronouns: String? = null,
    val profilePhotoUri: String? = null
)
