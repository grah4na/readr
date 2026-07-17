package com.readr.app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.readr.app.ReadrApp
import com.readr.app.data.model.EntryType
import com.readr.app.data.model.ReadingEntry
import com.readr.app.data.model.SearchResult
import com.readr.app.data.remote.model.GoogleBookItem
import com.readr.app.data.remote.model.OpenLibraryDoc
import com.readr.app.data.repository.ReadrRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModel(application: Application) : AndroidViewModel(application) {
    private val repo: ReadrRepository = (application as ReadrApp).repository

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    private val _searchResults = MutableStateFlow<List<SearchResult>>(emptyList())
    val searchResults: StateFlow<List<SearchResult>> = _searchResults.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching: StateFlow<Boolean> = _isSearching.asStateFlow()

    init {
        viewModelScope.launch {
            _query
                .debounce(100)
                .filter { it.length >= 2 }
                .distinctUntilChanged()
                .flatMapLatest { q ->
                    flow {
                        try {
                            _isSearching.value = true
                            val openLib = repo.searchOpenLibrary(q)
                            emit(combineResults(openLib, emptyList()))
                            val google = repo.searchGoogleBooks(q)
                            emit(combineResults(openLib, google))
                        } finally {
                            _isSearching.value = false
                        }
                    }
                }
                .collect { results ->
                    _searchResults.value = results
                }
        }
    }

    fun updateQuery(query: String) {
        _query.value = query
    }

    suspend fun addToWantToRead(result: SearchResult): Long {
        val enriched = enrichWithDescription(result)
        val entry = ReadingEntry(
            type = EntryType.BOOK,
            title = enriched.title,
            author = enriched.author,
            coverUrl = enriched.coverUrl,
            isbn = enriched.isbn,
            pages = enriched.pages,
            description = enriched.description,
            previewUrl = enriched.previewUrl
        )
        return repo.insertEntry(entry)
    }

    suspend fun addToCurrentlyReading(result: SearchResult): Long {
        val enriched = enrichWithDescription(result)
        val entry = ReadingEntry(
            type = EntryType.BOOK,
            title = enriched.title,
            author = enriched.author,
            coverUrl = enriched.coverUrl,
            isbn = enriched.isbn,
            pages = enriched.pages,
            description = enriched.description,
            previewUrl = enriched.previewUrl,
            progress = 0.01f,
            dateStarted = System.currentTimeMillis()
        )
        return repo.insertEntry(entry)
    }

    suspend fun addToFinished(result: SearchResult): Long {
        val enriched = enrichWithDescription(result)
        val now = System.currentTimeMillis()
        val entry = ReadingEntry(
            type = EntryType.BOOK,
            title = enriched.title,
            author = enriched.author,
            coverUrl = enriched.coverUrl,
            isbn = enriched.isbn,
            pages = enriched.pages,
            description = enriched.description,
            previewUrl = enriched.previewUrl,
            progress = 1f,
            dateStarted = now,
            dateFinished = now
        )
        return repo.insertEntry(entry)
    }

    private suspend fun enrichWithDescription(result: SearchResult): SearchResult {
        if (result.description.isNotBlank()) return result
        val description = if (result.isbn.isNotBlank()) {
            repo.fetchBookByIsbnGoogleBooks(result.isbn)?.description
                ?: repo.fetchBookByIsbnOpenLibrary(result.isbn)?.description
                ?: ""
        } else {
            repo.fetchDescriptionBySearch(result.title, result.author, result.isbn)
        }
        return result.copy(description = description)
    }

    private fun combineResults(
        openLib: List<OpenLibraryDoc>,
        google: List<GoogleBookItem>
    ): List<SearchResult> {
        val isbnSet = mutableSetOf<String>()
        val results = mutableListOf<SearchResult>()

        for (doc in openLib) {
            val isbn = doc.isbn?.firstOrNull() ?: ""
            if (isbn.isNotEmpty() && !isbnSet.add(isbn)) continue
            if (isbn.isEmpty() && results.any { it.title == doc.title }) continue
            results.add(
                SearchResult(
                    title = doc.title ?: "Unknown",
                    author = doc.authorName?.joinToString(", ") ?: "Unknown",
                    coverUrl = if (doc.coverId != null)
                        "https://covers.openlibrary.org/b/id/${doc.coverId}-M.jpg" else "",
                    isbn = isbn,
                    pages = doc.pages ?: 0,
                    source = "OpenLibrary"
                )
            )
        }

        for (item in google) {
            val info = item.volumeInfo ?: continue
            val isbn = info.industryIdentifiers
                ?.firstOrNull { it.type == "ISBN_13" || it.type == "ISBN_10" }
                ?.identifier ?: ""
            if (isbn.isNotEmpty() && !isbnSet.add(isbn)) continue
            if (isbn.isEmpty() && results.any { it.title == info.title }) continue
            results.add(
                SearchResult(
                    title = info.title ?: "Unknown",
                    author = info.authors?.joinToString(", ") ?: "Unknown",
                    coverUrl = info.imageLinks?.thumbnail?.replace("http://", "https://") ?: "",
                    isbn = isbn,
                    pages = info.pageCount ?: 0,
                    description = info.description ?: "",
                    previewUrl = info.previewLink ?: "",
                    source = "GoogleBooks"
                )
            )
        }

        return results
    }
}
