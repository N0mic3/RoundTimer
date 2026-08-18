package com.example.roundtimer.di

import com.example.roundtimer.data.controller.TimerSessionControllerImpl
import com.example.roundtimer.data.workmanager.WorkManagerFocusReminderScheduler
import com.example.roundtimer.domain.controller.FocusReminderScheduler
import com.example.roundtimer.domain.controller.TimerSessionController
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ControllerModule {

    @Binds
    @Singleton
    abstract fun provideTimeSessionController(
        implementation: TimerSessionControllerImpl
    ) : TimerSessionController


    @Binds
    @Singleton
    abstract fun provideFocusReminderScheduler(
        implementation: WorkManagerFocusReminderScheduler
    ) : FocusReminderScheduler
}