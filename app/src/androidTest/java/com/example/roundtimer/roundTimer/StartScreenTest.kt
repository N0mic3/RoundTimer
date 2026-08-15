package com.example.roundtimer.roundTimer

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.example.roundtimer.domain.model.TimeSettings
import com.example.roundtimer.ui.navigation.RoundInfoModel
import com.example.roundtimer.ui.screens.start.QuoteUiState
import com.example.roundtimer.ui.screens.start.StartScreen
import com.example.roundtimer.ui.theme.RoundTimerTheme
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

class StartScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun initialContent_isDisplayed() {
        val quoteUiState = QuoteUiState(
            data = "Hello"
        )
        composeTestRule.setContent {
            RoundTimerTheme {
                StartScreen(
                    navigateToRunningScreen = {},
                    quoteUiState = quoteUiState
                ) { _, _ ->

                }
            }
        }
        composeTestRule
            .onNodeWithText("Quote For the Day")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText("Hello")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText("Work Duration: ")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText("Rest Duration: ")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText("Rounds: ")
            .assertIsDisplayed()

        composeTestRule
            .onAllNodesWithTag("duration_picker")
            .assertCountEquals(3)

        composeTestRule
            .onNodeWithText("Start")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Save")
            .assertIsDisplayed()
    }

    @Test
    fun start_invokesRunningNavigation() {
        val quoteUiState = QuoteUiState(
            data = "Hello"
        )
        var navigatedTimer: RoundInfoModel? = null
        composeTestRule.setContent {
            RoundTimerTheme {
                StartScreen(
                    navigateToRunningScreen = {
                        navigatedTimer = it
                    },
                    quoteUiState = quoteUiState
                ) { _, _ ->
                }
            }
        }

        composeTestRule
            .onNodeWithText("Start")
            .performClick()

        composeTestRule.runOnIdle {
            Assert.assertEquals(
                RoundInfoModel(
                    workDuration = 5,
                    restDuration = 5,
                    roundCount = 1,
                ),
                navigatedTimer,
            )
        }
    }

    @Test
    fun savingTimer_invokesCallbackWithNameAndSettings() {
        val quoteUiState = QuoteUiState(
            data = "Hello"
        )
        var savedName: String? = null
        var savedSettings: TimeSettings? = null
        composeTestRule.setContent {
            RoundTimerTheme {
                StartScreen(
                    navigateToRunningScreen = {},
                    quoteUiState = quoteUiState
                ) { timeSetting, name ->
                    savedName = name
                    savedSettings = timeSetting
                }
            }
        }

        composeTestRule.onNodeWithText("Save").performClick()

        composeTestRule
            .onNodeWithTag("timer_name_input")
            .performTextInput("Morning Focus")


        composeTestRule.onNodeWithTag("alert_confirm_button").performClick()

        composeTestRule.runOnIdle {
            Assert.assertEquals("Morning Focus", savedName)
            Assert.assertEquals(
                TimeSettings(
                    workDuration = 5,
                    restDuration = 5,
                    roundCount = 1,
                ),
                savedSettings,
            )
        }
    }
}