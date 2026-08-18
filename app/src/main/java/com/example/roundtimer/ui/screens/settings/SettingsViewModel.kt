package com.example.roundtimer.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.roundtimer.domain.usecase.ManageAppSettingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val manageAppSettingsUseCase: ManageAppSettingsUseCase
) : ViewModel() {

    private val _settingsUiState = MutableStateFlow(SettingsUiState())
    val settingsUiState = _settingsUiState.asStateFlow()


    init {
        viewModelScope.launch {
            manageAppSettingsUseCase.observeAppSettings().collect { settings ->
                _settingsUiState.value = SettingsUiState(
                    settingsUiItems = listOf(
                        SettingsUiItem(
                            settingType = SettingsItemType.DAILY_FOCUS_REMINDER,
                            activeState = settings.isDailyReminderEnabled,
                        ),
                    )
                )
            }
        }
    }

    fun toggleAction(settingType : SettingsItemType, newActiveState : Boolean) {
        when(settingType) {
            SettingsItemType.DAILY_FOCUS_REMINDER -> {
                viewModelScope.launch {
                    manageAppSettingsUseCase.setReminderEnabled(newActiveState)
                }
            }
        }
    }
}
