package com.readr.app.ui.screens.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.readr.app.ReadrApp
import com.readr.app.data.model.ReadingEntry
import com.readr.app.data.repository.ReadrRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class SettingsViewModel(application: Application) : AndroidViewModel(application) {
    private val repo: ReadrRepository = (application as ReadrApp).repository

    val topBooks: StateFlow<List<ReadingEntry>> = repo.getTopRatedBooks()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}
