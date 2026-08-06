package com.example.roundtimer.data.controller

import android.content.Context
import com.example.roundtimer.data.audio.PhaseSoundPlayer
import com.example.roundtimer.data.notification.TimerNotificationManager
import com.example.roundtimer.data.service.TimerForegroundService
import com.example.roundtimer.domain.controller.TimerSessionController
import com.example.roundtimer.domain.model.TimeState
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class TimerSessionControllerImpl @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val phaseSoundPlayer: PhaseSoundPlayer,
    private val timerNotificationManager: TimerNotificationManager,
) : TimerSessionController {
    override fun startTimerSession() {
        TimerForegroundService.start(
            context
        )
    }

    override fun stopTimerSession() {
        TimerForegroundService.stop(
            context
        )
    }

    override fun onTimeStateChanged(
        previousTimeState: TimeState,
        currentTimeState: TimeState
    ) {
        timerNotificationManager.updateTimerNotification(currentTimeState)
        if (previousTimeState.phase != currentTimeState.phase) {
            phaseSoundPlayer.playFor(currentTimeState.phase)
        }
    }
}