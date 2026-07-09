package com.readr.app.data.remote.model

import com.google.gson.annotations.SerializedName

data class GoogleBooksResponse(
    val items: List<GoogleBookItem>? = null
)

data class GoogleBookItem(
    val id: String? = null,
    val volumeInfo: GoogleVolumeInfo? = null
)

data class GoogleVolumeInfo(
    val title: String? = null,
    val authors: List<String>? = null,
    val publisher: String? = null,
    @SerializedName("publishedDate")
    val publishedDate: String? = null,
    val description: String? = null,
    @SerializedName("pageCount")
    val pageCount: Int? = null,
    val categories: List<String>? = null,
    @SerializedName("imageLinks")
    val imageLinks: GoogleImageLinks? = null,
    val industryIdentifiers: List<IndustryIdentifier>? = null,
    @SerializedName("previewLink")
    val previewLink: String? = null
)

data class GoogleImageLinks(
    val smallThumbnail: String? = null,
    val thumbnail: String? = null,
    @SerializedName("extra_large")
    val extraLarge: String? = null
)

data class IndustryIdentifier(
    val type: String? = null,
    val identifier: String? = null
)