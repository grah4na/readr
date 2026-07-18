package com.readr.app.ui.screens.profile

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.readr.app.ReadrApp
import com.readr.app.data.local.entity.UserProfileEntity
import com.readr.app.data.model.ProfileStats
import com.readr.app.data.model.ReadingEntry
import com.readr.app.data.repository.ReadrRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.File

private data class BasicStats(
    val pagesRead: Int,
    val finished: Int,
    val reading: Int,
    val wantToRead: Int,
    val avgRating: Float
)

class ProfileViewModel(application: Application) : AndroidViewModel(application) {
    private val repo: ReadrRepository = (application as ReadrApp).repository

    val userProfile: StateFlow<UserProfileEntity?> = repo.getUserProfile()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val stats: StateFlow<ProfileStats> = combine(
        combine(
            repo.getTotalPagesRead(),
            repo.getFinishedCount(),
            repo.getCurrentlyReadingCount(),
            repo.getWantToReadCount(),
            repo.getAverageRating()
        ) { pagesRead, finished, reading, wantToRead, avgRating ->
            BasicStats(pagesRead, finished, reading, wantToRead, avgRating)
        },
        repo.getFinishedEntries()
    ) { basic, finishedEntries ->
        val longest = finishedEntries.maxByOrNull { it.pages }
        val hours = finishedEntries.sumOf { entry ->
            if (entry.dateStarted > 0 && entry.dateFinished > entry.dateStarted) {
                ((entry.dateFinished - entry.dateStarted) / (1000.0 * 60 * 60)).toLong()
            } else if (entry.pages > 0) {
                ((entry.pages / 300.0) * 5).toLong()
            } else 0L
        }.toInt()
        ProfileStats(
            pagesRead = basic.pagesRead,
            hoursSpent = hours,
            avgRating = basic.avgRating,
            booksFinished = basic.finished,
            booksReading = basic.reading,
            booksWantToRead = basic.wantToRead,
            longestBookTitle = longest?.title,
            longestBookPages = longest?.pages ?: 0
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ProfileStats())

    val finishedBooks: StateFlow<List<ReadingEntry>> = repo.getFinishedEntries()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val wantToReadBooks: StateFlow<List<ReadingEntry>> = repo.getWantToRead()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val currentlyReading: StateFlow<List<ReadingEntry>> = repo.getCurrentlyReading()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _isEditing = MutableStateFlow(false)
    val isEditing: StateFlow<Boolean> = _isEditing.asStateFlow()

    fun showEditProfile() {
        _isEditing.value = true
    }

    fun hideEditProfile() {
        _isEditing.value = false
    }

    fun updateProfile(name: String, bio: String, pronouns: String) {
        viewModelScope.launch {
            val current = userProfile.value
            if (current != null) {
                repo.insertUserProfile(current.copy(displayName = name, bio = bio, pronouns = pronouns))
            } else {
                repo.insertUserProfile(UserProfileEntity(displayName = name, bio = bio, pronouns = pronouns))
            }
        }
    }

    fun updateProfilePhoto(uri: Uri) {
        viewModelScope.launch {
            try {
                val context = getApplication<Application>()
                val dir = File(context.filesDir, "profile_photos")
                dir.mkdirs()
                val file = File(dir, "profile_${System.currentTimeMillis()}.jpg")
                context.contentResolver.openInputStream(uri)?.use { input ->
                    file.outputStream().use { output ->
                        input.copyTo(output)
                    }
                }
                repo.updateProfilePhotoUri(file.toURI().toString())
            } catch (e: Exception) {
                repo.updateProfilePhotoUri(uri.toString())
            }
        }
    }

    fun startReading(entryId: Long) {
        viewModelScope.launch {
            val entry = repo.getEntryById(entryId) ?: return@launch
            repo.updateEntry(entry.copy(progress = 0.01f, dateStarted = System.currentTimeMillis()))
        }
    }
}
