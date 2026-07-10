package com.readr.app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.readr.app.ReadrApp
import com.readr.app.data.model.ReadingEntry
import com.readr.app.data.repository.ReadrRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val repo: ReadrRepository = (application as ReadrApp).repository

    val currentlyReading: StateFlow<List<ReadingEntry>> = repo.getCurrentlyReading()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val wantToRead: StateFlow<List<ReadingEntry>> = repo.getWantToRead()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val finished: StateFlow<List<ReadingEntry>> = repo.getFinishedEntries()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allEntries: StateFlow<List<ReadingEntry>> = repo.getAllEntries()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    suspend fun deleteEntry(entry: ReadingEntry) = repo.deleteEntry(entry)

    suspend fun updateEntry(entry: ReadingEntry) = repo.updateEntry(entry)
}
