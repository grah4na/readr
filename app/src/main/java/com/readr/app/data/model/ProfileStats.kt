package com.readr.app.data.model

data class ProfileStats(
    val pagesRead: Int = 0,
    val hoursSpent: Int = 0,
    val avgRating: Float = 0f,
    val booksFinished: Int = 0,
    val booksReading: Int = 0,
    val booksWantToRead: Int = 0,
    val longestBookTitle: String? = null,
    val longestBookPages: Int = 0
)
