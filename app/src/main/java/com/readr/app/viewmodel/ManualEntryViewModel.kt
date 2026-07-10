package com.readr.app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.readr.app.ReadrApp
import com.readr.app.data.model.EntryType
import com.readr.app.data.model.ReadingEntry
import com.readr.app.data.repository.ReadrRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ManualEntryState(
    val title: String = "",
    val author: String = "",
    val isbn: String = "",
    val pages: String = "",
    val coverUrl: String = "",
    val isAutoFetching: Boolean = false,
    val isSaving: Boolean = false,
    val savedEntryId: Long? = null,
    val error: String? = null
)

class ManualEntryViewModel(application: Application) : AndroidViewModel(application) {
    private val repo: ReadrRepository = (application as ReadrApp).repository

    private val _state = MutableStateFlow(ManualEntryState())
    val state: StateFlow<ManualEntryState> = _state.asStateFlow()

    fun updateTitle(title: String) {
        _state.value = _state.value.copy(title = title)
    }

    fun updateAuthor(author: String) {
        _state.value = _state.value.copy(author = author)
    }

    fun updateIsbn(isbn: String) {
        _state.value = _state.value.copy(isbn = isbn)
        val cleaned = isbn.filter { it.isDigit() }
        if (cleaned.length == 10 || cleaned.length == 13) {
            autoFetch(cleaned)
        }
    }

    fun updatePages(pages: String) {
        _state.value = _state.value.copy(pages = pages)
    }

    private fun autoFetch(isbn: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isAutoFetching = true, error = null)
            val result = repo.fetchBookByIsbnGoogleBooks(isbn)
                ?: repo.fetchBookByIsbnOpenLibrary(isbn)
            if (result != null) {
                _state.value = _state.value.copy(
                    title = result.title.ifBlank { _state.value.title },
                    author = result.author.ifBlank { _state.value.author },
                    coverUrl = result.coverUrl,
                    pages = if (result.pages > 0) result.pages.toString() else _state.value.pages,
                    isAutoFetching = false
                )
            } else {
                _state.value = _state.value.copy(
                    isAutoFetching = false,
                    error = "Could not auto-fetch metadata for ISBN $isbn"
                )
            }
        }
    }

    fun save() {
        val s = _state.value
        if (s.title.isBlank()) {
            _state.value = s.copy(error = "Title is required")
            return
        }
        viewModelScope.launch {
            _state.value = _state.value.copy(isSaving = true, error = null)
            val entry = ReadingEntry(
                type = EntryType.BOOK,
                title = s.title.trim(),
                author = s.author.trim().ifBlank { "Unknown" },
                isbn = s.isbn.trim(),
                pages = s.pages.trim().toIntOrNull() ?: 0,
                coverUrl = s.coverUrl.trim()
            )
            val id = repo.insertEntry(entry)
            _state.value = _state.value.copy(isSaving = false, savedEntryId = id)
        }
    }

    fun clearError() {
        _state.value = _state.value.copy(error = null)
    }
}
