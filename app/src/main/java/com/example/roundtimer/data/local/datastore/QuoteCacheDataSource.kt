package com.example.roundtimer.data.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.example.roundtimer.data.local.datastore.model.QuoteCache
import javax.inject.Inject
import kotlinx.coroutines.flow.first

class QuoteCacheDataSource @Inject constructor(
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

val Context.quoteCacheDataStore: DataStore<QuoteCache> by dataStore(
    fileName = "quote_cache.json",
    serializer = QuoteCacheSerializer
)