package com.example.roundtimer.di

import android.content.Context
import androidx.datastore.core.DataStore
import com.example.roundtimer.data.local.model.QuoteCache
import com.example.roundtimer.data.local.quoteCacheDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Provides
    @Singleton
    fun getQuoteCacheDataSource(
        @ApplicationContext context: Context
    ) : DataStore<QuoteCache> {
        return context.quoteCacheDataStore
    }
}