package com.example.roundtimer.di

import com.example.roundtimer.data.remote.RetrofitInstance
import com.example.roundtimer.data.repository.QuoteRepositoryImpl
import com.example.roundtimer.domain.repository.QuoteRepository
import com.example.roundtimer.domain.usecase.QuoteUseCase
import com.example.roundtimer.domain.usecase.TimeUseCase

object AppContainer {
    private val quoteRepository : QuoteRepository by lazy {
        QuoteRepositoryImpl(
            quoteApiService = RetrofitInstance.quoteAPiService
        )
    }

    val quoteUseCase : QuoteUseCase by lazy {
        QuoteUseCase(
            quoteRepository = quoteRepository
        )
    }

    val timeUseCase : TimeUseCase by lazy {
        TimeUseCase()
    }
}