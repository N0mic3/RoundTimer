package com.example.roundtimer.ui.screens.running

import com.example.roundtimer.domain.model.TimeState
import com.example.roundtimer.domain.model.TimerPhase


data class TimerUiState(
    val timeState : TimeState = TimeState(
        phase = TimerPhase.Ready,
        secondsLeft = 5,
        currentRoundIndex = 0,
    ),
)
