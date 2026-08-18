package com.example.roundtimer.roundTimer

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.roundtimer.ui.screens.settings.SettingsItemType
import com.example.roundtimer.ui.screens.settings.SettingsScreen
import com.example.roundtimer.ui.screens.settings.SettingsUiItem
import com.example.roundtimer.ui.screens.settings.SettingsUiState
import com.example.roundtimer.ui.theme.RoundTimerTheme
import org.junit.Rule
import org.junit.Test

class SettingsScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun initialContent_isDisplayed() {
        val settingsUiState = SettingsUiState(
            settingsUiItems = listOf(
                SettingsUiItem(
                    settingType = SettingsItemType.DAILY_FOCUS_REMINDER,
                    activeState = false
                )
            )
        )
        composeTestRule.setContent {
            RoundTimerTheme {
                SettingsScreen(
                    settingsUiState = settingsUiState,
                ) { _, _ ->

                }
            }
        }

        composeTestRule
            .onNodeWithText(SettingsItemType.DAILY_FOCUS_REMINDER.displayName)
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithTag("DAILY_FOCUS_REMINDER_switch")
            .assertIsOff()
    }

    @Test
    fun toggleSwitch_updatesSwitchState() {
        composeTestRule.setContent {
            var settingsUiState by remember {
                mutableStateOf(
                    SettingsUiState(
                        settingsUiItems = listOf(
                            SettingsUiItem(
                                settingType = SettingsItemType.DAILY_FOCUS_REMINDER,
                                activeState = false
                            )
                        )
                    )
                )
            }
            RoundTimerTheme {
                SettingsScreen(
                    settingsUiState = settingsUiState,
                ) { type, isEnabled ->
                    settingsUiState = settingsUiState.copy(
                        settingsUiItems = settingsUiState.settingsUiItems.map {
                            if (type == it.settingType) {
                                it.copy(
                                    activeState = isEnabled
                                )
                            } else {
                                it
                            }
                        }
                    )
                }
            }
        }
        composeTestRule
            .onNodeWithTag("DAILY_FOCUS_REMINDER_switch")
            .assertIsOff()
            .performClick()
            .assertIsOn()
    }
}