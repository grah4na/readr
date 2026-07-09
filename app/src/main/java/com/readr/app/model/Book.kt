package com.readr.app.model

data class Book(
    val title: String,
    val author: String,
    val coverUrl: String,
    val progress: Float = 0f
)
