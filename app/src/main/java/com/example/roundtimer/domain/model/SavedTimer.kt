package com.example.roundtimer.domain.model

data class SavedTimer(
    val id: Long = 0,
    val name: String,
    val timeSettings: TimeSettings,
)
