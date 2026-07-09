package com.readr.app.data.remote.api

import com.readr.app.data.remote.model.OpenGraphData
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenGraphApi {
    @GET("https://opengraph.io/api/1.1/site")
    suspend fun fetchMetadata(
        @Query("url") url: String,
        @Query("app_id") appId: String = "readr"
    ): Map<String, Any>
}