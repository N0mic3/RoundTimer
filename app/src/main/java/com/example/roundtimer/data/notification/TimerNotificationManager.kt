package com.example.roundtimer.data.notification

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import com.example.roundtimer.MainActivity
import com.example.roundtimer.R
import com.example.roundtimer.domain.model.TimeState
import com.example.roundtimer.domain.model.TimerPhase
import com.example.roundtimer.utils.Utils
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TimerNotificationManager @Inject constructor(
    @param:ApplicationContext private val context: Context
) {
    companion object {
        const val CHANNEL_ID = "round_timer_channel"
        const val NOTIFICATION_ID = 1001
    }

    fun createChannel() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
        val channel = NotificationChannel(
            CHANNEL_ID,
            "round Timer",
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = "Shows the active round timer"
        }

        context.getSystemService<NotificationManager>()
            ?.createNotificationChannel(channel)
    }

    fun buildTimerNotification(
        phase: TimerPhase,
        secondsLeft: Int,
    ) = NotificationCompat.Builder(context, CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentTitle("Round Timer")
        .setContentText("${phase.name} • ${Utils.formatTime(secondsLeft)} remaining")
        .setContentIntent(createOpenAppPendingIntent())
        .setOngoing(true)
        .setOnlyAlertOnce(true)
        .setCategory(NotificationCompat.CATEGORY_SERVICE)
        .build()

    private fun createOpenAppPendingIntent(): PendingIntent {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or
                    Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        return PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )
    }

    fun updateTimerNotification(
        timeState: TimeState,
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        val notification = buildTimerNotification(
            phase = timeState.phase,
            secondsLeft = timeState.secondsLeft,
        )

        NotificationManagerCompat.from(context).notify(
            NOTIFICATION_ID,
            notification,
        )
    }
}