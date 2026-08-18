package com.example.roundtimer.ui.screens.settings


enum class SettingsItemType(
    val displayName: String,
) {
    DAILY_FOCUS_REMINDER(displayName = "Daily focus reminder"),
}
data class SettingsUiItem(
    val settingType : SettingsItemType,
    val activeState: Boolean
)