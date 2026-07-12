package com.readr.app.ui.share

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream

object ShareHelper {
    private const val FILE_PROVIDER_AUTHORITY = "com.readr.app.fileprovider"

    fun shareToInstagramStories(context: Context, bitmap: Bitmap): Boolean {
        val uri = saveAndGetUri(context, bitmap) ?: return false

        return try {
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "image/png"
                setPackage("com.instagram.android")
                putExtra(Intent.EXTRA_STREAM, uri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            context.startActivity(intent)
            true
        } catch (e: Exception) {
            false
        }
    }

    fun shareGeneric(context: Context, bitmap: Bitmap) {
        val uri = saveAndGetUri(context, bitmap) ?: return

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "image/png"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(intent, "Share via"))
    }

    private fun saveAndGetUri(context: Context, bitmap: Bitmap): Uri? {
        return try {
            val cacheDir = File(context.cacheDir, "shared_images")
            cacheDir.mkdirs()

            val file = File(cacheDir, "readr_share_${System.currentTimeMillis()}.png")
            FileOutputStream(file).use { out ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
                out.flush()
            }

            FileProvider.getUriForFile(context, FILE_PROVIDER_AUTHORITY, file)
        } catch (e: Exception) {
            null
        }
    }
}
