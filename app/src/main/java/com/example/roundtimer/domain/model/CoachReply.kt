package com.example.roundtimer.domain.model

data class CoachReply(
    val message: String,
    val suggestedTimerSettings: TimeSettings? = null,
)
