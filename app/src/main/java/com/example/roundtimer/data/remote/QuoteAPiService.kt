package com.example.roundtimer.data.remote

import com.example.roundtimer.model.QuoteModel
import retrofit2.http.GET


interface QuoteAPiService {

    @GET("today")
    suspend fun getQuoteOfTheDay(): List<QuoteModel>
}