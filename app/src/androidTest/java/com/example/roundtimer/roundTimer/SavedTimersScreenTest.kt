package com.example.roundtimer.roundTimer

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextReplacement
import com.example.roundtimer.domain.model.SavedTimer
import com.example.roundtimer.domain.model.TimeSettings
import com.example.roundtimer.ui.navigation.RoundInfoModel
import com.example.roundtimer.ui.screens.savedTimers.SavedTimerScreen
import com.example.roundtimer.ui.screens.savedTimers.SavedTimersUiState
import com.example.roundtimer.ui.theme.RoundTimerTheme
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

class SavedTimersScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun lazyColumnLoading() {
        val savedTimerList = listOf(
            SavedTimer(
                name = "Test",
                timeSettings = TimeSettings(
                    workDuration = 20,
                    restDuration = 20,
                    roundCount = 5
                )
            )
        )
        val saveTimeUiState = SavedTimersUiState(
            savedTimeList = savedTimerList
        )
        composeTestRule.setContent {
            RoundTimerTheme {
                SavedTimerScreen(
                    navigateToRunningScreen = {},
                    saveTimeUiState = saveTimeUiState,
                    updateSavedTimeList = {

                    },
                    deleteSavedTimer = {

                    }
                )
            }
        }
        composeTestRule
            .onAllNodesWithTag("timer_row")
            .assertCountEquals(savedTimerList.size)
        composeTestRule
            .onNodeWithText("Work duration: 20")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText("rest duration: 20")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText("roundCount: 5")
            .assertIsDisplayed()
    }

    @Test
    fun navigateToRunningScreen_click() {
        val savedTimerList = listOf(
            SavedTimer(
                name = "Test",
                timeSettings = TimeSettings(
                    workDuration = 20,
                    restDuration = 20,
                    roundCount = 5
                )
            )
        )
        val saveTimeUiState = SavedTimersUiState(
            savedTimeList = savedTimerList
        )
        var roundInfoModel : RoundInfoModel? = null
        composeTestRule.setContent {
            RoundTimerTheme {
                SavedTimerScreen(
                    navigateToRunningScreen = {
                        roundInfoModel = it
                    },
                    saveTimeUiState = saveTimeUiState,
                    updateSavedTimeList = {

                    },
                    deleteSavedTimer = {

                    }
                )
            }
        }
        composeTestRule
            .onNodeWithText("Start")
            .performClick()
        composeTestRule.runOnIdle {
            Assert.assertNotNull(roundInfoModel)
            Assert.assertEquals(
                roundInfoModel?.toTimeSettings(),
                savedTimerList.firstOrNull()?.timeSettings
            )
        }
    }

    @Test
    fun updateSavedTimeList_click() {
        val savedTimerList = listOf(
            SavedTimer(
                name = "Test",
                timeSettings = TimeSettings(
                    workDuration = 20,
                    restDuration = 20,
                    roundCount = 5
                )
            )
        )
        val saveTimeUiState = SavedTimersUiState(
            savedTimeList = savedTimerList
        )
        var updatedSavedTimer : SavedTimer? = null
        composeTestRule.setContent {
            RoundTimerTheme {
                SavedTimerScreen(
                    navigateToRunningScreen = {},
                    saveTimeUiState = saveTimeUiState,
                    updateSavedTimeList = {
                        updatedSavedTimer = it
                    },
                    deleteSavedTimer = {}
                )
            }
        }
        composeTestRule
            .onNodeWithText("Update")
            .performClick()

        composeTestRule
            .onNodeWithTag("update_name_text_field")
            .performTextReplacement("update")

        composeTestRule
            .onNodeWithText("Save")
            .performClick()

        composeTestRule.runOnIdle {
            Assert.assertEquals(
                "update",
                updatedSavedTimer?.name
            )
        }
    }

    @Test
    fun deleteSavedTimeList_click() {
        val savedTimerList = listOf(
            SavedTimer(
                name = "Test",
                timeSettings = TimeSettings(
                    workDuration = 20,
                    restDuration = 20,
                    roundCount = 5
                )
            )
        )
        var deletedSavedTimer : SavedTimer? = null
        composeTestRule.setContent {
            var timers by remember {
                mutableStateOf(savedTimerList)
            }
            RoundTimerTheme {
                SavedTimerScreen(
                    navigateToRunningScreen = {},
                    saveTimeUiState = SavedTimersUiState(
                        savedTimeList = timers
                    ),
                    updateSavedTimeList = {},
                    deleteSavedTimer = { deletedTimer ->
                        deletedSavedTimer = deletedTimer
                        timers = timers.filterNot { it.id == deletedTimer.id }
                    }
                )
            }
        }
        composeTestRule
            .onNodeWithText("Delete")
            .performClick()

        composeTestRule
            .onNodeWithText("Confirm")
            .performClick()

        composeTestRule
            .onAllNodesWithTag("timer_row")
            .assertCountEquals(savedTimerList.size - 1)

        composeTestRule.runOnIdle {
            Assert.assertEquals(
                savedTimerList.first(),
                deletedSavedTimer,
            )
        }
    }
}