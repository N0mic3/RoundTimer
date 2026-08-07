package com.example.roundtimer

import android.app.Application
import com.example.roundtimer.data.notification.TimerNotificationManager
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class RoundTimerApplication : Application() {

    @Inject
    lateinit var timerNotificationManager: TimerNotificationManager

    override fun onCreate() {
        super.onCreate()
        timerNotificationManager.createChannel()
    }
}