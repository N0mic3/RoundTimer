package com.example.roundtimer.domain.controller

import com.example.roundtimer.domain.model.TimeState

interface TimerSessionController {

    fun startTimerSession()

    fun stopTimerSession()

    fun onTimeStateChanged(
        previousTimeState: TimeState,
        currentTimeState: TimeState,
    )
}