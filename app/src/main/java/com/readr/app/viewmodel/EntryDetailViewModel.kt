package com.readr.app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.readr.app.ReadrApp
import com.readr.app.data.model.ReadingEntry
import com.readr.app.data.model.ReadingSession
import com.readr.app.data.repository.ReadrRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class EntryDetailViewModel(application: Application) : AndroidViewModel(application) {
    private val repo: ReadrRepository = (application as ReadrApp).repository

    private val _entry = MutableStateFlow<ReadingEntry?>(null)
    val entry: StateFlow<ReadingEntry?> = _entry.asStateFlow()

    private val _sessions = MutableStateFlow<List<ReadingSession>>(emptyList())
    val sessions: StateFlow<List<ReadingSession>> = _sessions.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun loadEntry(entryId: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            var e = repo.getEntryById(entryId)
            if (e != null && e.description.isBlank()) {
                val desc = repo.fetchDescriptionBySearch(e.title, e.author, e.isbn)
                if (desc.isNotBlank()) {
                    e = e.copy(description = desc)
                    repo.updateEntry(e)
                }
            }
            _entry.value = e
            _isLoading.value = false
        }
        viewModelScope.launch {
            repo.getSessionsForEntry(entryId).collect { sessionList ->
                _sessions.value = sessionList
            }
        }
    }

    fun updateProgress(progress: Float) {
        val e = _entry.value ?: return
        viewModelScope.launch {
            repo.updateEntry(e.copy(progress = progress.coerceIn(0f, 1f)))
            _entry.value = repo.getEntryById(e.id)
        }
    }

    fun updateRating(rating: Int) {
        val e = _entry.value ?: return
        viewModelScope.launch {
            repo.updateEntry(e.copy(rating = rating.coerceIn(0, 5)))
            _entry.value = repo.getEntryById(e.id)
        }
    }

    fun markAsStarted() {
        val e = _entry.value ?: return
        viewModelScope.launch {
            repo.updateEntry(e.copy(dateStarted = System.currentTimeMillis()))
            _entry.value = repo.getEntryById(e.id)
        }
    }

    fun markAsFinished() {
        val e = _entry.value ?: return
        viewModelScope.launch {
            repo.updateEntry(e.copy(
                dateFinished = System.currentTimeMillis(),
                progress = 1f
            ))
            _entry.value = repo.getEntryById(e.id)
        }
    }

    fun addSession(pagesRead: Int, durationMinutes: Int, notes: String) {
        val e = _entry.value ?: return
        viewModelScope.launch {
            val session = ReadingSession(
                entryId = e.id,
                pagesRead = pagesRead,
                durationMinutes = durationMinutes,
                notes = notes
            )
            repo.insertSession(session)
            updateProgressFromSessions()
        }
    }

    fun deleteSession(session: ReadingSession) {
        viewModelScope.launch {
            repo.deleteSession(session)
            updateProgressFromSessions()
        }
    }

    private suspend fun updateProgressFromSessions() {
        val e = _entry.value ?: return
        val currentSessions = repo.getSessionsForEntry(e.id).first()
        val totalPagesRead = currentSessions.sumOf { it.pagesRead }
        if (e.pages > 0) {
            val newProgress = (totalPagesRead.toFloat() / e.pages).coerceIn(0f, 1f)
            repo.updateEntry(e.copy(progress = newProgress))
        }
        _entry.value = repo.getEntryById(e.id)
    }

    fun deleteEntry() {
        val e = _entry.value ?: return
        viewModelScope.launch {
            repo.deleteEntry(e)
        }
    }
}
