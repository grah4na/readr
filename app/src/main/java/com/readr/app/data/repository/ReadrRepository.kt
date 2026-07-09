package com.readr.app.data.repository

import com.readr.app.data.local.ReadrDatabase
import com.readr.app.data.local.dao.LearningNoteDao
import com.readr.app.data.local.dao.QuoteDao
import com.readr.app.data.local.dao.ReadingEntryDao
import com.readr.app.data.local.dao.ReadingSessionDao
import com.readr.app.data.local.dao.ReviewDao
import com.readr.app.data.local.dao.TriggerWarningDao
import com.readr.app.data.model.EntryType
import com.readr.app.data.model.LearningNote
import com.readr.app.data.model.Quote
import com.readr.app.data.model.ReadingEntry
import com.readr.app.data.model.ReadingSession
import com.readr.app.data.model.Review
import com.readr.app.data.model.TriggerWarning
import com.readr.app.data.remote.NetworkModule
import com.readr.app.data.remote.api.OpenLibraryApi
import com.readr.app.data.remote.api.GoogleBooksApi
import com.readr.app.data.remote.model.GoogleBookItem
import com.readr.app.data.remote.model.OpenLibraryDoc
import kotlinx.coroutines.flow.Flow

class ReadrRepository(
    database: ReadrDatabase
) {
    private val entryDao: ReadingEntryDao = database.readingEntryDao()
    private val sessionDao: ReadingSessionDao = database.readingSessionDao()
    private val quoteDao: QuoteDao = database.quoteDao()
    private val reviewDao: ReviewDao = database.reviewDao()
    private val noteDao: LearningNoteDao = database.learningNoteDao()
    private val warningDao: TriggerWarningDao = database.triggerWarningDao()

    private val openLibraryApi: OpenLibraryApi = NetworkModule.openLibraryApi
    private val googleBooksApi: GoogleBooksApi = NetworkModule.googleBooksApi

    fun getAllEntries(): Flow<List<ReadingEntry>> = entryDao.getAllEntries()
    fun getEntriesByType(type: EntryType): Flow<List<ReadingEntry>> = entryDao.getEntriesByType(type)
    fun getCurrentlyReading(): Flow<List<ReadingEntry>> = entryDao.getCurrentlyReading()
    fun getFinishedEntries(): Flow<List<ReadingEntry>> = entryDao.getFinishedEntries()
    fun getWantToRead(): Flow<List<ReadingEntry>> = entryDao.getWantToRead()

    suspend fun getEntryById(id: Long): ReadingEntry? = entryDao.getEntryById(id)
    suspend fun insertEntry(entry: ReadingEntry): Long = entryDao.insertEntry(entry)
    suspend fun updateEntry(entry: ReadingEntry) = entryDao.updateEntry(entry)
    suspend fun deleteEntry(entry: ReadingEntry) = entryDao.deleteEntry(entry)
    suspend fun deleteEntryById(id: Long) = entryDao.deleteEntryById(id)

    fun getSessionsForEntry(entryId: Long): Flow<List<ReadingSession>> = sessionDao.getSessionsForEntry(entryId)
    suspend fun insertSession(session: ReadingSession): Long = sessionDao.insertSession(session)
    suspend fun updateSession(session: ReadingSession) = sessionDao.updateSession(session)
    suspend fun deleteSession(session: ReadingSession) = sessionDao.deleteSession(session)

    fun getQuotesForEntry(entryId: Long): Flow<List<Quote>> = quoteDao.getQuotesForEntry(entryId)
    fun getAllQuotes(): Flow<List<Quote>> = quoteDao.getAllQuotes()
    suspend fun insertQuote(quote: Quote): Long = quoteDao.insertQuote(quote)
    suspend fun updateQuote(quote: Quote) = quoteDao.updateQuote(quote)
    suspend fun deleteQuote(quote: Quote) = quoteDao.deleteQuote(quote)

    fun getReviewForEntry(entryId: Long): Flow<Review?> = reviewDao.getReviewForEntry(entryId)
    suspend fun insertReview(review: Review): Long = reviewDao.insertReview(review)
    suspend fun updateReview(review: Review) = reviewDao.updateReview(review)
    suspend fun deleteReview(review: Review) = reviewDao.deleteReview(review)

    fun getNotesForEntry(entryId: Long): Flow<List<LearningNote>> = noteDao.getNotesForEntry(entryId)
    fun getAllNotes(): Flow<List<LearningNote>> = noteDao.getAllNotes()
    suspend fun insertNote(note: LearningNote): Long = noteDao.insertNote(note)
    suspend fun updateNote(note: LearningNote) = noteDao.updateNote(note)
    suspend fun deleteNote(note: LearningNote) = noteDao.deleteNote(note)

    fun getWarningsForEntry(entryId: Long): Flow<List<TriggerWarning>> = warningDao.getWarningsForEntry(entryId)
    suspend fun insertWarning(warning: TriggerWarning): Long = warningDao.insertWarning(warning)
    suspend fun updateWarning(warning: TriggerWarning) = warningDao.updateWarning(warning)
    suspend fun deleteWarning(warning: TriggerWarning) = warningDao.deleteWarning(warning)

    suspend fun searchOpenLibrary(query: String): List<OpenLibraryDoc> {
        return try {
            openLibraryApi.searchBooks(query).docs.orEmpty()
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun searchGoogleBooks(query: String): List<GoogleBookItem> {
        return try {
            googleBooksApi.searchBooks(query).items.orEmpty()
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun fetchBookByIsbnOpenLibrary(isbn: String): ReadingEntry? {
        return try {
            val response = openLibraryApi.getBookByIsbn(isbn)
            if (response.title != null) {
                ReadingEntry(
                    type = EntryType.BOOK,
                    title = response.title,
                    author = response.authors?.firstOrNull()?.name ?: "Unknown",
                    isbn = isbn,
                    pages = response.pages ?: 0
                )
            } else null
        } catch (e: Exception) {
            null
        }
    }

    suspend fun fetchBookByIsbnGoogleBooks(isbn: String): ReadingEntry? {
        return try {
            val response = googleBooksApi.getBookByIsbn("isbn:$isbn")
            val item = response.items?.firstOrNull() ?: return null
            val info = item.volumeInfo ?: return null
            ReadingEntry(
                type = EntryType.BOOK,
                title = info.title ?: "Unknown",
                author = info.authors?.joinToString(", ") ?: "Unknown",
                coverUrl = info.imageLinks?.thumbnail?.replace("http://", "https://") ?: "",
                isbn = isbn,
                pages = info.pageCount ?: 0
            )
        } catch (e: Exception) {
            null
        }
    }
}