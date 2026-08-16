package com.example.roundtimer.data.workmanager

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.roundtimer.data.notification.DailyFocusReminderNotificationManager
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class DailyFocusReminderWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters,
    private val dailyFocusReminderNotificationManager: DailyFocusReminderNotificationManager,
) : CoroutineWorker(
    appContext, params
) {
    override suspend fun doWork() = try {
        dailyFocusReminderNotificationManager.showReminder()
        Result.success()
    } catch (_: Exception) {
        Result.retry()
    }
}