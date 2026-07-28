package com.example.roundtimer.data.remote

import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "https://zenquotes.io/api/"
    private val json = Json {
        ignoreUnknownKeys = true
    }
    val quoteAPiService: QuoteApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(
                json.asConverterFactory("application/json".toMediaType())
            )
            .build()
            .create(QuoteApiService::class.java)
    }
}