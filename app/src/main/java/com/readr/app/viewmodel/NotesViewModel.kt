package com.readr.app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.readr.app.ReadrApp
import com.readr.app.data.local.entity.NoteEntity
import com.readr.app.data.model.ReadingEntry
import com.readr.app.data.repository.ReadrRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NotesViewModel(application: Application) : AndroidViewModel(application) {
    private val repo: ReadrRepository = (application as ReadrApp).repository

    private val _notes = MutableStateFlow<List<NoteEntity>>(emptyList())
    val notes: StateFlow<List<NoteEntity>> = _notes.asStateFlow()

    private val _entries = MutableStateFlow<List<ReadingEntry>>(emptyList())
    val entries: StateFlow<List<ReadingEntry>> = _entries.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadEntries()
        loadNotes()
    }

    fun loadNotes() {
        viewModelScope.launch {
            _isLoading.value = true
            _notes.value = repo.getAllNewNotes()
            _isLoading.value = false
        }
    }

    private fun loadEntries() {
        viewModelScope.launch {
            repo.getAllEntries().collect { entryList ->
                _entries.value = entryList
            }
        }
    }

    fun addNote(readingLogId: String, text: String, pageNumber: Int?, tags: String, type: String) {
        viewModelScope.launch {
            repo.addNote(readingLogId, text, pageNumber, tags, type)
            _notes.value = repo.getAllNewNotes()
        }
    }

    fun searchNotes(query: String) {
        viewModelScope.launch {
            _notes.value = if (query.isBlank()) {
                repo.getAllNewNotes()
            } else {
                repo.searchAllNewNotes(query)
            }
        }
    }
}
