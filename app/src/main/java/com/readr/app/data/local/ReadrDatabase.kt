package com.readr.app.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.readr.app.data.local.converter.Converters
import com.readr.app.data.local.dao.LearningNoteDao
import com.readr.app.data.local.dao.QuoteDao
import com.readr.app.data.local.dao.ReadingEntryDao
import com.readr.app.data.local.dao.ReadingSessionDao
import com.readr.app.data.local.dao.ReviewDao
import com.readr.app.data.local.dao.TriggerWarningDao
import com.readr.app.data.model.LearningNote
import com.readr.app.data.model.Quote
import com.readr.app.data.model.ReadingEntry
import com.readr.app.data.model.ReadingSession
import com.readr.app.data.model.Review
import com.readr.app.data.model.TriggerWarning

@Database(
    entities = [
        ReadingEntry::class,
        ReadingSession::class,
        Quote::class,
        Review::class,
        LearningNote::class,
        TriggerWarning::class
    ],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class ReadrDatabase : RoomDatabase() {
    abstract fun readingEntryDao(): ReadingEntryDao
    abstract fun readingSessionDao(): ReadingSessionDao
    abstract fun quoteDao(): QuoteDao
    abstract fun reviewDao(): ReviewDao
    abstract fun learningNoteDao(): LearningNoteDao
    abstract fun triggerWarningDao(): TriggerWarningDao

    companion object {
        @Volatile
        private var INSTANCE: ReadrDatabase? = null

        fun getInstance(context: Context): ReadrDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ReadrDatabase::class.java,
                    "readr_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}