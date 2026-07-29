package com.example.roundtimer.data.repository

import com.example.roundtimer.data.local.QuoteCacheDataSource
import com.example.roundtimer.data.local.model.QuoteCache
import com.example.roundtimer.data.remote.QuoteApiService
import com.example.roundtimer.domain.model.Quote
import com.example.roundtimer.domain.repository.QuoteRepository
import java.text.SimpleDateFormat
import java.util.Calendar
import javax.inject.Inject

class QuoteRepositoryImpl @Inject constructor(
    private val quoteApiService: QuoteApiService,
    private val quoteCacheDataSource: QuoteCacheDataSource
) : QuoteRepository {
    override suspend fun getQuoteOfTheDay() : Quote?  {
        val today = SimpleDateFormat(
            "yyyy-MM-dd",
            java.util.Locale.US
        ).format(
            Calendar.getInstance().time
        )
        val quoteCache = quoteCacheDataSource.getCache()
        return if (today == quoteCache.date && quoteCache.quote.isNotBlank()) {
            Quote(
                quote = quoteCache.quote,
                author = quoteCache.author
            )
        } else {
            quoteApiService.getQuoteOfTheDay().firstOrNull()?.toQuote()?.also {
                quoteCacheDataSource.saveCache(
                    QuoteCache(
                        date = today,
                        quote = it.quote,
                        author = it.author
                    )
                )
            }
        }
    }
}