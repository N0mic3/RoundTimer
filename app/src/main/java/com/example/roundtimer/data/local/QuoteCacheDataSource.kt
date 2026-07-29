package com.example.roundtimer.data.local

import androidx.datastore.core.DataStore
import com.example.roundtimer.data.local.model.QuoteCache
import kotlinx.coroutines.flow.first

class QuoteCacheDataSource(
    private val quoteDataStore: DataStore<QuoteCache>
) {
    suspend fun getCache() : QuoteCache {
        return quoteDataStore.data.first()
    }

    suspend fun saveCache(cache : QuoteCache) {
        quoteDataStore.updateData {
            cache
        }
    }
}