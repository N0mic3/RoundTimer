package com.example.roundtimer.di

import com.example.roundtimer.data.repository.AiCoachRepositoryImpl
import com.example.roundtimer.data.repository.AuthRepositoryImpl
import com.example.roundtimer.data.repository.AppSettingsRepositoryImpl
import com.example.roundtimer.data.repository.QuoteRepositoryImpl
import com.example.roundtimer.data.repository.SavedTimerRepositoryImpl
import com.example.roundtimer.domain.repository.AiCoachRepository
import com.example.roundtimer.domain.repository.AuthRepository
import com.example.roundtimer.domain.repository.AppSettingsRepository
import com.example.roundtimer.domain.repository.QuoteRepository
import com.example.roundtimer.domain.repository.SavedTimerRepository
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

    @Binds
    @Singleton
    abstract fun provideSavedTimeRepository(
        implementation: SavedTimerRepositoryImpl
    ) : SavedTimerRepository

    @Binds
    @Singleton
    abstract fun provideAiCoachRepository(
        implementation: AiCoachRepositoryImpl
    ) : AiCoachRepository


    @Binds
    @Singleton
    abstract fun provideFirebaseAuthRepository(
        implementation: AuthRepositoryImpl
    ) : AuthRepository

    @Binds
    @Singleton
    abstract fun provideAppSettingsRepository(
        implementation: AppSettingsRepositoryImpl
    ) : AppSettingsRepository
}
