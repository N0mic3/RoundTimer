package com.example.roundtimer.domain.usecase

import com.example.roundtimer.domain.repository.QuoteRepository
import javax.inject.Inject

class QuoteUseCase @Inject constructor(
    private val quoteRepository: QuoteRepository
) {
    suspend fun getQuoteForTheDay() = quoteRepository.getQuoteOfTheDay()
}