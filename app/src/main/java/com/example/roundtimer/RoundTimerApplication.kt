package com.example.roundtimer

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.example.roundtimer.data.notification.DailyFocusReminderNotificationManager
import com.example.roundtimer.data.notification.TimerNotificationManager
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class RoundTimerApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    @Inject
    lateinit var timerNotificationManager: TimerNotificationManager

    @Inject
    lateinit var dailyFocusReminderNotificationManager: DailyFocusReminderNotificationManager

    override fun onCreate() {
        super.onCreate()
        timerNotificationManager.createChannel()
        dailyFocusReminderNotificationManager.createChannel()
    }
}