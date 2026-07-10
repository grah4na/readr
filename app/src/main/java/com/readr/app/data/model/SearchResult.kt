package com.readr.app.data.model

data class SearchResult(
    val title: String,
    val author: String,
    val coverUrl: String = "",
    val isbn: String = "",
    val pages: Int = 0,
    val description: String = "",
    val source: String = ""
)
