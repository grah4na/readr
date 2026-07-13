package com.readr.app.ui.share

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

object CoverBitmapLoader {
    suspend fun load(context: Context, url: String?): Bitmap? {
        if (url.isNullOrBlank()) return null
        val largeUrl = transformToLargeUrl(url)
        return withContext(Dispatchers.IO) {
            try {
                val connection = URL(largeUrl).openConnection() as HttpURLConnection
                connection.connectTimeout = 8000
                connection.readTimeout = 8000
                connection.instanceFollowRedirects = true
                connection.setRequestProperty("User-Agent", "Readr/1.0")
                val inputStream = connection.inputStream
                val bitmap = BitmapFactory.decodeStream(inputStream)
                inputStream.close()
                connection.disconnect()
                bitmap
            } catch (e: Exception) {
                null
            }
        }
    }

    private fun transformToLargeUrl(url: String): String {
        return when {
            url.contains("covers.openlibrary.org") -> {
                url.replace(Regex("-(S|M)\\."), "-L.")
                    .replace("&size=small", "&size=large")
                    .replace("&size=medium", "&size=large")
            }
            url.contains("books.google.com") -> {
                url.replace("zoom=1", "zoom=3")
                    .replace("zoom=2", "zoom=3")
                    .replace("&edge=curl", "")
                    .replace("&pg=", "&pg=PP1&")
            }
            url.contains("googleapis.com/books") -> {
                url.replace("zoom=1", "zoom=3")
                    .replace("zoom=2", "zoom=3")
            }
            else -> url
        }
    }
}
