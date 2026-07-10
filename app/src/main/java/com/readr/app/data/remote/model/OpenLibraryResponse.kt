package com.readr.app.data.remote.model

import com.google.gson.annotations.SerializedName

data class OpenLibraryResponse(
    val docs: List<OpenLibraryDoc>? = null
)

data class OpenLibraryDoc(
    val key: String? = null,
    val title: String? = null,
    @SerializedName("author_name")
    val authorName: List<String>? = null,
    @SerializedName("isbn")
    val isbn: List<String>? = null,
    @SerializedName("number_of_pages_median")
    val pages: Int? = null,
    @SerializedName("cover_i")
    val coverId: Long? = null
)

data class OpenLibraryWorkResponse(
    val title: String? = null,
    val description: Any? = null
) {
    fun descriptionText(): String {
        return when (val d = description) {
            is String -> d
            is Map<*, *> -> (d["value"] as? String) ?: ""
            else -> ""
        }
    }
}

data class OpenLibraryBookResponse(
    val title: String? = null,
    val authors: List<OpenLibraryAuthor>? = null,
    val covers: List<Long>? = null,
    @SerializedName("number_of_pages")
    val pages: Int? = null,
    val description: Any? = null
) {
    fun descriptionText(): String {
        return when (val d = description) {
            is String -> d
            is Map<*, *> -> (d["value"] as? String) ?: ""
            else -> ""
        }
    }
}

data class OpenLibraryAuthor(
    val name: String? = null
)