package com.example.roundtimer.ui.savedTimers

import com.example.roundtimer.domain.model.SavedTimer

data class SavedTimersUiState(
    val savedTimeList: List<SavedTimer> = emptyList()
)
