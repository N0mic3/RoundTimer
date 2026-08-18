package com.example.roundtimer.data.repository

import com.example.roundtimer.data.local.datastore.AppSettingsPreferencesDataSource
import com.example.roundtimer.domain.model.AppSettings
import com.example.roundtimer.domain.repository.AppSettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppSettingsRepositoryImpl @Inject constructor(
    private val appSettingsPreferencesDataSource: AppSettingsPreferencesDataSource
) : AppSettingsRepository {
    override fun observeAppSettings(): Flow<AppSettings> =
        appSettingsPreferencesDataSource.observeAppSettings()

    override suspend fun setDailyReminderEnabled(isEnabled: Boolean) {
        appSettingsPreferencesDataSource.setReminderEnabled(isEnabled)
    }
}
