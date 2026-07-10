package com.readr.app.data.remote

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object NetworkModule {
    private const val OPEN_LIBRARY_BASE = "https://openlibrary.org/"
    private const val GOOGLE_BOOKS_BASE = "https://www.googleapis.com/books/v1/"
    private const val WIKIPEDIA_BASE = "https://en.wikipedia.org/"
    private const val USER_AGENT = "Readr/1.0 (Android; book tracking app; mailto:readr@example.com)"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    private val wikipediaClient: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor(Interceptor { chain ->
            chain.proceed(
                chain.request().newBuilder()
                    .header("User-Agent", USER_AGENT)
                    .build()
            )
        })
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .build()

    private val openLibraryRetrofit: Retrofit = Retrofit.Builder()
        .baseUrl(OPEN_LIBRARY_BASE)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val googleBooksRetrofit: Retrofit = Retrofit.Builder()
        .baseUrl(GOOGLE_BOOKS_BASE)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val wikipediaRetrofit: Retrofit = Retrofit.Builder()
        .baseUrl(WIKIPEDIA_BASE)
        .client(wikipediaClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val openLibraryApi: com.readr.app.data.remote.api.OpenLibraryApi =
        openLibraryRetrofit.create(com.readr.app.data.remote.api.OpenLibraryApi::class.java)

    val googleBooksApi: com.readr.app.data.remote.api.GoogleBooksApi =
        googleBooksRetrofit.create(com.readr.app.data.remote.api.GoogleBooksApi::class.java)

    val wikipediaApi: com.readr.app.data.remote.api.WikipediaApi =
        wikipediaRetrofit.create(com.readr.app.data.remote.api.WikipediaApi::class.java)
}