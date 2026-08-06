package com.example.roundtimer.data.service

import android.Manifest
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import androidx.core.app.ActivityCompat
import androidx.core.app.ServiceCompat
import androidx.core.content.ContextCompat
import com.example.roundtimer.data.notification.TimerNotificationManager
import com.example.roundtimer.domain.model.TimerPhase
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TimerForegroundService : Service() {

    companion object {

        fun start(context: Context) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            val intent = Intent(context, TimerForegroundService::class.java)
            ContextCompat.startForegroundService(context, intent)
        }

        fun stop(context: Context) {
            context.stopService(
                Intent(context, TimerForegroundService::class.java)
            )
        }

    }

    @Inject
    lateinit var timerNotificationManager : TimerNotificationManager

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notification = timerNotificationManager.buildTimerNotification(
            phase = TimerPhase.Ready,
            secondsLeft = 5,
        )

        val serviceType =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE
            } else {
                0
            }

        ServiceCompat.startForeground(
            this,
            TimerNotificationManager.NOTIFICATION_ID,
            notification,
            serviceType,
        )

        return START_NOT_STICKY
    }

    override fun onBind(p0: Intent?): IBinder? = null
}