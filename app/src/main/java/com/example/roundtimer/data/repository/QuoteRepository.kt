package com.example.roundtimer.data.repository

import com.example.roundtimer.data.remote.RetrofitInstance

class QuoteRepository {
    suspend fun getQuoteOfTheDay() =
        RetrofitInstance.quoteAPiService.getQuoteOfTheDay().firstOrNull()
}