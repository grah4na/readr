package com.readr.app.data.remote.model

import com.google.gson.annotations.SerializedName

data class WikiSummaryResponse(
    val title: String? = null,
    val extract: String? = null,
    val thumbnail: WikiThumbnail? = null,
    @SerializedName("pageid")
    val pageId: Long? = null
)

data class WikiThumbnail(
    val source: String? = null
)
