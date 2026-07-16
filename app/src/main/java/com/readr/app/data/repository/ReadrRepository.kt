package com.readr.app.data.repository

import com.readr.app.data.local.ReadrDatabase
import com.readr.app.data.local.dao.CommunityNoteDao
import com.readr.app.data.local.dao.LearningNoteDao
import com.readr.app.data.local.dao.NewNoteDao
import com.readr.app.data.local.dao.NewQuoteDao
import com.readr.app.data.local.dao.NewReviewDao
import com.readr.app.data.local.dao.QuoteDao
import com.readr.app.data.local.dao.ReadingEntryDao
import com.readr.app.data.local.dao.ReadingSessionDao
import com.readr.app.data.local.dao.ReviewDao
import com.readr.app.data.local.dao.TriggerWarningDao
import com.readr.app.data.local.dao.UserProfileDao
import com.readr.app.data.local.entity.CommunityNoteEntity
import com.readr.app.data.local.entity.NoteEntity
import com.readr.app.data.local.entity.QuoteEntity
import com.readr.app.data.local.entity.ReviewEntity
import com.readr.app.data.local.entity.UserProfileEntity
import com.readr.app.data.model.EntryType
import com.readr.app.data.model.LearningNote
import com.readr.app.data.model.Quote
import com.readr.app.data.model.ReadingEntry
import com.readr.app.data.model.ReadingSession
import com.readr.app.data.model.Review
import com.readr.app.data.model.TriggerWarning
import com.readr.app.data.remote.NetworkModule
import com.readr.app.data.remote.api.GoogleBooksApi
import com.readr.app.data.remote.api.OpenLibraryApi
import com.readr.app.data.remote.api.WikipediaApi
import com.readr.app.data.remote.model.GoogleBookItem
import com.readr.app.data.remote.model.OpenLibraryDoc
import kotlinx.coroutines.flow.Flow
import java.net.URLEncoder

class ReadrRepository(
    database: ReadrDatabase
) {
    private val entryDao: ReadingEntryDao = database.readingEntryDao()
    private val sessionDao: ReadingSessionDao = database.readingSessionDao()
    private val quoteDao: QuoteDao = database.quoteDao()
    private val reviewDao: ReviewDao = database.reviewDao()
    private val noteDao: LearningNoteDao = database.learningNoteDao()
    private val warningDao: TriggerWarningDao = database.triggerWarningDao()

    private val newQuoteDao: NewQuoteDao = database.newQuoteDao()
    private val newReviewDao: NewReviewDao = database.newReviewDao()
    private val newNoteDao: NewNoteDao = database.newNoteDao()
    private val communityNoteDao: CommunityNoteDao = database.communityNoteDao()
    private val userProfileDao: UserProfileDao = database.userProfileDao()

    private val openLibraryApi: OpenLibraryApi = NetworkModule.openLibraryApi
    private val googleBooksApi: GoogleBooksApi = NetworkModule.googleBooksApi
    private val wikipediaApi: WikipediaApi = NetworkModule.wikipediaApi

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
                    pages = response.pages ?: 0,
                    description = response.descriptionText()
                )
            } else null
        } catch (e: Exception) {
            null
        }
    }

    suspend fun fetchDescriptionBySearch(title: String, author: String, isbn: String = ""): String {
        val query = if (author.isNotBlank() && author != "Unknown") "$title ${author.take(30)}" else title
        return try {
            if (isbn.isNotBlank()) {
                val fromIsbn = fetchBookByIsbnOpenLibrary(isbn)?.description?.takeIf { it.isNotBlank() }
                if (fromIsbn != null) return fromIsbn
            }
            val docs = openLibraryApi.searchBooks(query, limit = 5).docs.orEmpty()
            docs.firstOrNull { !it.isbn.isNullOrEmpty() }?.isbn?.firstOrNull()?.let { i ->
                val fromIsbn = fetchBookByIsbnOpenLibrary(i)?.description?.takeIf { it.isNotBlank() }
                if (fromIsbn != null) return fromIsbn
            }
            docs.firstOrNull { it.key != null }?.key?.let { key ->
                try {
                    openLibraryApi.getWork(key.trimStart('/')).descriptionText().takeIf { it.isNotBlank() }
                } catch (e: Exception) { null }
            }?.also { return it }
            fetchWikipediaDescription(title) ?: ""
        } catch (e: Exception) { "" }
    }

    suspend fun fetchWikipediaDescription(title: String): String? {
        val mainTitle = title.substringBefore(":").substringBefore(" (").trim().take(100)
        return try {
            val encoded = URLEncoder.encode(mainTitle, "UTF-8")
            val response = wikipediaApi.getSummary(encoded)
            response.extract?.take(500)
        } catch (e: Exception) { null }
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
                pages = info.pageCount ?: 0,
                description = info.description ?: "",
                previewUrl = info.previewLink ?: ""
            )
        } catch (e: Exception) {
            null
        }
    }

    suspend fun addQuote(readingLogId: String, text: String, pageNumber: Int?) {
        newQuoteDao.insert(
            QuoteEntity(
                id = java.util.UUID.randomUUID().toString(),
                readingLogId = readingLogId,
                text = text,
                pageNumber = pageNumber,
                createdAt = System.currentTimeMillis()
            )
        )
    }

    suspend fun getQuotes(readingLogId: String): List<QuoteEntity> =
        newQuoteDao.getByLogId(readingLogId)

    suspend fun addReview(
        readingLogId: String,
        rating: Int,
        reviewText: String?,
        spoilerPercent: Float?,
        whatILearned: String?
    ) {
        newReviewDao.insert(
            ReviewEntity(
                id = java.util.UUID.randomUUID().toString(),
                readingLogId = readingLogId,
                rating = rating,
                reviewText = reviewText,
                spoilerPercent = spoilerPercent,
                whatILearned = whatILearned,
                createdAt = System.currentTimeMillis()
            )
        )
    }

    suspend fun getReview(readingLogId: String): ReviewEntity? =
        newReviewDao.getByLogId(readingLogId)

    suspend fun addNote(
        readingLogId: String,
        text: String,
        pageNumber: Int?,
        tags: String?,
        type: String
    ) {
        newNoteDao.insert(
            NoteEntity(
                id = java.util.UUID.randomUUID().toString(),
                readingLogId = readingLogId,
                text = text,
                pageNumber = pageNumber,
                tags = tags,
                type = type,
                createdAt = System.currentTimeMillis()
            )
        )
    }

    suspend fun getNotes(readingLogId: String): List<NoteEntity> =
        newNoteDao.getByLogId(readingLogId)

    suspend fun getAllNewNotes(): List<NoteEntity> =
        newNoteDao.getAll()

    suspend fun searchAllNewNotes(query: String): List<NoteEntity> =
        newNoteDao.searchAll(query)

    suspend fun searchNotes(readingLogId: String, query: String): List<NoteEntity> =
        newNoteDao.searchInLog(readingLogId, query)

    suspend fun addCommunityNote(
        workId: String,
        type: String,
        startPercent: Float,
        endPercent: Float,
        noteText: String
    ) {
        communityNoteDao.insert(
            CommunityNoteEntity(
                id = java.util.UUID.randomUUID().toString(),
                workId = workId,
                type = type,
                startPercent = startPercent,
                endPercent = endPercent,
                noteText = noteText,
                createdAt = System.currentTimeMillis()
            )
        )
    }

    suspend fun getCommunityNotes(workId: String): List<CommunityNoteEntity> =
        communityNoteDao.getByWorkId(workId)

    fun getUserProfile(): Flow<UserProfileEntity?> = userProfileDao.getProfile()

    suspend fun insertUserProfile(profile: UserProfileEntity) = userProfileDao.insert(profile)

    suspend fun updateDisplayName(name: String) = userProfileDao.updateDisplayName(name)

    suspend fun updateBio(bio: String) = userProfileDao.updateBio(bio)

    suspend fun updatePronouns(pronouns: String) = userProfileDao.updatePronouns(pronouns)

    suspend fun updateProfilePhotoUri(uri: String) = userProfileDao.updateProfilePhotoUri(uri)

    fun getTotalPagesRead(): Flow<Int> = entryDao.getTotalPagesRead()
    fun getFinishedCount(): Flow<Int> = entryDao.getFinishedCount()
    fun getCurrentlyReadingCount(): Flow<Int> = entryDao.getCurrentlyReadingCount()
    fun getWantToReadCount(): Flow<Int> = entryDao.getWantToReadCount()
    suspend fun getLongestBookRead(): ReadingEntry? = entryDao.getLongestBookRead()
    fun getLongestBookReadFlow(): Flow<ReadingEntry?> = entryDao.getLongestBookReadFlow()
    fun getAverageRating(): Flow<Float> = newReviewDao.getAverageRating()
    fun getAllReviews(): Flow<List<ReviewEntity>> = newReviewDao.getAllReviews()
}