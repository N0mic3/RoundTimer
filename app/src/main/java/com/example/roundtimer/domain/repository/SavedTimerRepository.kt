package com.example.roundtimer.domain.repository

import com.example.roundtimer.domain.model.SavedTimer
import com.example.roundtimer.domain.model.TimeSettings
import kotlinx.coroutines.flow.Flow

interface SavedTimerRepository {
    suspend fun insertSavedTimeSetting(
        timeSettings: TimeSettings,
        name: String,
    )

    fun getSavedTimerList() : Flow<List<SavedTimer>>
}