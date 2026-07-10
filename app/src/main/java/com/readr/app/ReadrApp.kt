package com.readr.app

import android.app.Application
import com.readr.app.data.local.ReadrDatabase
import com.readr.app.data.repository.ReadrRepository

class ReadrApp : Application() {
    lateinit var repository: ReadrRepository
        private set

    override fun onCreate() {
        super.onCreate()
        instance = this
        val database = ReadrDatabase.getInstance(this)
        repository = ReadrRepository(database)
    }

    companion object {
        lateinit var instance: ReadrApp
            private set
    }
}
