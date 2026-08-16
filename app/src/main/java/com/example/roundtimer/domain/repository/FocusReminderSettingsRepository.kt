package com.example.roundtimer.domain.repository

import kotlinx.coroutines.flow.Flow

interface FocusReminderSettingsRepository {
    fun isDailyReminderEnabled(): Flow<Boolean>

    suspend fun setDailyReminderEnabled(
        isEnabled: Boolean,
    )
}