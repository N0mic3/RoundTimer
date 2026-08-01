package com.example.roundtimer.data.repository

import com.example.roundtimer.data.local.datastore.QuoteCacheDataSource
import com.example.roundtimer.data.local.datastore.model.QuoteCache
import com.example.roundtimer.data.remote.QuoteApiService
import com.example.roundtimer.domain.model.Quote
import com.example.roundtimer.domain.repository.QuoteRepository
import com.example.roundtimer.utils.Utils
import javax.inject.Inject

class QuoteRepositoryImpl @Inject constructor(
    private val quoteApiService: QuoteApiService,
    private val quoteCacheDataSource: QuoteCacheDataSource
) : QuoteRepository {
    override suspend fun getQuoteOfTheDay() : Quote?  {
        val quoteCache = quoteCacheDataSource.getCache()
        return if (Utils.isToday(quoteCache.date) && quoteCache.quote.isNotBlank()) {
            Quote(
                quote = quoteCache.quote,
                author = quoteCache.author
            )
        } else {
            quoteApiService.getQuoteOfTheDay().firstOrNull()?.also {
                quoteCacheDataSource.saveCache(
                    it.toQuoteCache()
                )
            }?.toQuote()
        }
    }
}