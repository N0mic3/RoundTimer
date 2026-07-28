package com.example.roundtimer.model


enum class Phase(val message: String) {
    Ready("Get Ready"),
    Work("Work"),
    Rest("Rest"),
    Complete("Done")
}

data class TimerUiState(
    val phase: Phase = Phase.Ready,
    val isRunning : Boolean = true,
    val secondLeft: Int = 5,
    val currentRoundIndex: Int = 0
)
