package com.example.roundtimer.di

import com.example.roundtimer.data.controller.AndroidCoachSpeechRecognizerController
import com.example.roundtimer.data.controller.AndroidCoachTextToSpeechController
import com.example.roundtimer.data.controller.CoachEngineCoordinator
import com.example.roundtimer.data.controller.TimerSessionControllerImpl
import com.example.roundtimer.data.ondevice.MlKitOnDeviceCoachAvailability
import com.example.roundtimer.data.workmanager.WorkManagerFocusReminderScheduler
import com.example.roundtimer.domain.controller.CoachEngine
import com.example.roundtimer.domain.controller.CoachSpeechRecognizerController
import com.example.roundtimer.domain.controller.CoachTextToSpeechController
import com.example.roundtimer.domain.controller.FocusReminderScheduler
import com.example.roundtimer.domain.controller.OnDeviceCoachAvailability
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

    @Binds
    @Singleton
    abstract fun bindCoachEngine(
        implementation: CoachEngineCoordinator
    ) : CoachEngine

    @Binds
    @Singleton
    abstract fun bindOnDeviceCoachAvailability(
        implementation: MlKitOnDeviceCoachAvailability,
    ): OnDeviceCoachAvailability

    @Binds
    abstract fun bindCoachTextToSpeechController(
        implementation: AndroidCoachTextToSpeechController,
    ): CoachTextToSpeechController

    @Binds
    abstract fun bindCoachSpeechRecognizerController(
        implementation: AndroidCoachSpeechRecognizerController,
    ): CoachSpeechRecognizerController
}