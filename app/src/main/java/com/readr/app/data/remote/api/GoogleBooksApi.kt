package com.readr.app.data.remote.api

import com.readr.app.data.remote.model.GoogleBooksResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleBooksApi {
    @GET("volumes")
    suspend fun searchBooks(
        @Query("q") query: String,
        @Query("maxResults") maxResults: Int = 20
    ): GoogleBooksResponse

    @GET("volumes")
    suspend fun getBookByIsbn(
        @Query("q") query: String
    ): GoogleBooksResponse
}