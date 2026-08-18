package com.example.roundtimer.data.local.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.example.roundtimer.domain.model.AppSettings
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton


private val Context.appSettingsDataStore by preferencesDataStore(
    name = "app_settings"
)

@Singleton
class AppSettingsPreferencesDataSource @Inject constructor(
    @param:ApplicationContext private val context: Context
) {

    private companion object {
        val DAILY_REMINDER_ENABLED =
            booleanPreferencesKey("daily_reminder_enabled")
    }

    private val dataStore = context.appSettingsDataStore

    fun observeAppSettings(): Flow<AppSettings> {
        return dataStore.data.map {
            AppSettings(
                isDailyReminderEnabled = it[DAILY_REMINDER_ENABLED] ?: false
            )
        }
    }

    suspend fun setReminderEnabled(isEnabled: Boolean) {
        dataStore.edit {
            it[DAILY_REMINDER_ENABLED] = isEnabled
        }
    }


}
