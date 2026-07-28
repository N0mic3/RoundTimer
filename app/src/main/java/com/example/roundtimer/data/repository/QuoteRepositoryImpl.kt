package com.example.roundtimer.data.repository

import com.example.roundtimer.data.remote.QuoteApiService
import com.example.roundtimer.domain.repository.QuoteRepository

class QuoteRepositoryImpl(
    private val quoteApiService: QuoteApiService
) : QuoteRepository {
    override suspend fun getQuoteOfTheDay() = quoteApiService.getQuoteOfTheDay().firstOrNull()?.toQuote()
}