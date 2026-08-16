package com.example.roundtimer.data.workmanager

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.roundtimer.domain.controller.FocusReminderScheduler
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WorkManagerFocusReminderScheduler @Inject constructor(
    @param:ApplicationContext private val context: Context,
) : FocusReminderScheduler {
    private companion object {
        const val UNIQUE_WORK_NAME = "daily_focus_reminder"
    }
    override fun scheduleDailyReminder() {
        val reminderRequest = PeriodicWorkRequestBuilder<DailyFocusReminderWorker>(
            repeatInterval = 24,
            repeatIntervalTimeUnit = TimeUnit.HOURS,
        ).build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            uniqueWorkName = UNIQUE_WORK_NAME,
            existingPeriodicWorkPolicy = ExistingPeriodicWorkPolicy.UPDATE,
            request = reminderRequest
        )
    }

    override fun cancelDailyReminder() {
        WorkManager.getInstance(context).cancelUniqueWork(
            UNIQUE_WORK_NAME,
        )
    }
}