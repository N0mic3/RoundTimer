package com.example.roundtimer.ui.screens.aiCoachScreen

sealed interface AiCoachIntent {
    data class InputChanged(val text: String) : AiCoachIntent
    data object SendClicked : AiCoachIntent
}