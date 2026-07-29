package com.example.roundtimer.di

import com.example.roundtimer.data.repository.QuoteRepositoryImpl
import com.example.roundtimer.domain.repository.QuoteRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun provideQuoteRepository(
        implementation : QuoteRepositoryImpl
    ) : QuoteRepository
}