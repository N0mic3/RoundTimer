package com.example.roundtimer.data.remote

import com.example.roundtimer.data.remote.model.QuoteDto
import retrofit2.http.GET


interface QuoteApiService {

    @GET("today")
    suspend fun getQuoteOfTheDay(): List<QuoteDto>
}