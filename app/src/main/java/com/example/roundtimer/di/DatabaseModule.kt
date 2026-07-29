package com.example.roundtimer.di

import android.content.Context
import androidx.room.Room
import com.example.roundtimer.data.local.room.RoundTimerDatabase
import com.example.roundtimer.data.local.room.SavedTimerDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideRoundTimerDataBase(
        @ApplicationContext context: Context
    ) : RoundTimerDatabase {
        return Room.databaseBuilder(
            context = context,
            klass = RoundTimerDatabase::class.java,
            name = RoundTimerDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideSavedTimerDao(
        roundTimerDatabase : RoundTimerDatabase
    ) : SavedTimerDao {
        return roundTimerDatabase.getSavedTimerDao()
    }
}