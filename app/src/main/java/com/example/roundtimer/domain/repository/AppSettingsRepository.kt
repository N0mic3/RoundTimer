package com.example.roundtimer.domain.repository

import com.example.roundtimer.domain.model.AppSettings
import kotlinx.coroutines.flow.Flow

interface AppSettingsRepository {
    fun observeAppSettings(): Flow<AppSettings>

    suspend fun setDailyReminderEnabled(
        isEnabled: Boolean,
    )
}
