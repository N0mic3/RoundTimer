package com.example.roundtimer.domain.model

enum class TimerPhase {
    Ready,
    Work,
    Rest,
    Complete
}

data class TimeState(
    val phase: TimerPhase,
    val secondsLeft: Int,
    val currentRoundIndex: Int,
    val isRunning : Boolean = true,
)
