package com.readr.app.data.remote.api

import com.readr.app.data.remote.model.WikiSummaryResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface WikipediaApi {
    @GET("/api/rest_v1/page/summary/{title}")
    suspend fun getSummary(
        @Path("title", encoded = true) title: String
    ): WikiSummaryResponse
}
