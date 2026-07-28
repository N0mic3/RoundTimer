package com.example.roundtimer.domain.usecase

import com.example.roundtimer.domain.repository.QuoteRepository

class QuoteUseCase(
    private val quoteRepository: QuoteRepository
) {
    suspend fun getQuoteForTheDay() = quoteRepository.getQuoteOfTheDay()
}