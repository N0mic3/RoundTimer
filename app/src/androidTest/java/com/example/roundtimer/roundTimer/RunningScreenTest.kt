package com.example.roundtimer.roundTimer

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.roundtimer.domain.model.TimeState
import com.example.roundtimer.domain.model.TimerPhase
import com.example.roundtimer.ui.navigation.RoundInfoModel
import com.example.roundtimer.ui.screens.running.RunningScreen
import com.example.roundtimer.ui.screens.running.TimerUiState
import com.example.roundtimer.ui.theme.RoundTimerTheme
import org.junit.Rule
import org.junit.Test

class RunningScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun initialContent_isDisplayed() {
        val roundInfoModel = RoundInfoModel(
            workDuration = 20,
            restDuration = 20,
            roundCount = 5
        )
        val timerUiState = TimerUiState()
        composeTestRule.setContent {
            RoundTimerTheme {
                RunningScreen(
                    roundInfoModel = roundInfoModel,
                    timerUiState = timerUiState
                ) { }
            }
        }

        composeTestRule
            .onNodeWithText("Get Ready")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Round ${timerUiState.timeState.currentRoundIndex + 1} / ${roundInfoModel.roundCount}")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("00:05")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Pause")
            .assertIsDisplayed()
    }

    @Test
    fun pauseClick_displaysResume() {
        val roundInfoModel = RoundInfoModel(
            workDuration = 20,
            restDuration = 20,
            roundCount = 5
        )
        composeTestRule.setContent {
            var timerUiState by remember {
                mutableStateOf(TimerUiState())
            }
            RoundTimerTheme {
                RunningScreen(
                    roundInfoModel = roundInfoModel,
                    timerUiState = timerUiState
                ) {
                    timerUiState = timerUiState.copy(
                        timeState = timerUiState.timeState.copy(
                            isRunning = false
                        )
                    )
                }
            }
        }
        composeTestRule
            .onNodeWithText("Pause")
            .performClick()

        composeTestRule
            .onNodeWithText("Resume")
            .assertIsDisplayed()
    }

    @Test
    fun completedTimer_displaysDoneAndReset() {
        val roundInfoModel = RoundInfoModel(
            workDuration = 20,
            restDuration = 20,
            roundCount = 5
        )
        composeTestRule.setContent {
            var timerUiState by remember {
                mutableStateOf(TimerUiState(
                    timeState = TimeState(
                        phase = TimerPhase.Complete,
                        secondsLeft = 0,
                        currentRoundIndex = 0,
                        isRunning = false,
                    )
                ))
            }
            RoundTimerTheme {
                RunningScreen(
                    roundInfoModel = roundInfoModel,
                    timerUiState = timerUiState
                ) {
                    timerUiState = timerUiState.copy(
                        timeState = timerUiState.timeState.copy(
                            phase = TimerPhase.Ready,
                            secondsLeft = 5,
                            currentRoundIndex = 0,
                            isRunning = true
                        )
                    )
                }
            }
        }
        composeTestRule
            .onNodeWithText("Done")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Reset")
            .assertIsDisplayed()
            .performClick()

        composeTestRule
            .onNodeWithText("Get Ready")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("00:05")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Pause")
            .assertIsDisplayed()

    }
}