package com.example.roundtimer.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.example.roundtimer.data.local.QuoteCacheDataSource
import com.example.roundtimer.data.local.QuoteCacheSerializer
import com.example.roundtimer.data.local.model.QuoteCache
import com.example.roundtimer.data.remote.RetrofitInstance
import com.example.roundtimer.data.repository.QuoteRepositoryImpl
import com.example.roundtimer.domain.repository.QuoteRepository
import com.example.roundtimer.domain.usecase.QuoteUseCase
import com.example.roundtimer.domain.usecase.TimeUseCase

class AppContainer(
    context: Context,
) {

    private val quoteCacheDataSource = QuoteCacheDataSource(
        quoteDataStore = context.applicationContext.quoteCacheDataStore
    )
    private val quoteRepository : QuoteRepository by lazy {
        QuoteRepositoryImpl(
            quoteApiService = RetrofitInstance.quoteAPiService,
            quoteCacheDataSource = quoteCacheDataSource
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

val Context.quoteCacheDataStore: DataStore<QuoteCache> by dataStore(
    fileName = "quote_cache.json",
    serializer = QuoteCacheSerializer
)