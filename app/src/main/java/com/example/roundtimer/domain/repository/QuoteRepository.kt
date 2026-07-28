package com.example.roundtimer.domain.repository

import com.example.roundtimer.domain.model.Quote

interface QuoteRepository {
    suspend fun getQuoteOfTheDay(): Quote?
}