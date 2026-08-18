package com.example.roundtimer.domain.usecase

import com.example.roundtimer.domain.controller.FocusReminderScheduler
import com.example.roundtimer.domain.model.AppSettings
import com.example.roundtimer.domain.repository.AppSettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ManageAppSettingsUseCase @Inject constructor(
    private val settingsRepository: AppSettingsRepository,
    private val scheduler: FocusReminderScheduler,
) {

    fun observeAppSettings(): Flow<AppSettings> {
        return settingsRepository.observeAppSettings()
    }

    suspend fun setReminderEnabled(isEnabled: Boolean) {
        settingsRepository.setDailyReminderEnabled(isEnabled)

        if (isEnabled) {
            scheduler.scheduleDailyReminder()
        } else {
            scheduler.cancelDailyReminder()
        }
    }
}
