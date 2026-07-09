package com.readr.app.data.remote.api

import com.readr.app.data.remote.model.OpenLibraryBookResponse
import com.readr.app.data.remote.model.OpenLibraryResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface OpenLibraryApi {
    @GET("search.json")
    suspend fun searchBooks(
        @Query("q") query: String,
        @Query("limit") limit: Int = 20
    ): OpenLibraryResponse

    @GET("isbn/{isbn}.json")
    suspend fun getBookByIsbn(
        @Path("isbn") isbn: String
    ): OpenLibraryBookResponse
}