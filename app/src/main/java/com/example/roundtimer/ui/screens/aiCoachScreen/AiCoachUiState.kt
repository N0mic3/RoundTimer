package com.example.roundtimer.ui.screens.aiCoachScreen

data class AiCoachUiState(
    val input: String = "",
    val messages: List<CoachMessage> = listOf(
        CoachMessage(
            text = "Hi! Tell me your goal and how much time you have.",
            isFromUser = false,
        )
    ),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)