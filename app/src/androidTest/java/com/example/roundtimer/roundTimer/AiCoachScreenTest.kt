package com.example.roundtimer.roundTimer

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.example.roundtimer.domain.model.CoachMode
import com.example.roundtimer.ui.screens.aiCoachScreen.AiCoachIntent
import com.example.roundtimer.ui.screens.aiCoachScreen.AiCoachScreen
import com.example.roundtimer.ui.screens.aiCoachScreen.AiCoachUiState
import com.example.roundtimer.ui.screens.aiCoachScreen.CoachMessage
import com.example.roundtimer.ui.theme.RoundTimerTheme
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

class AiCoachScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun initialContent_isDisplayed() {
        val aiStateUiState = AiCoachUiState(
            input = "test",
            messages = listOf(
                CoachMessage(
                    text = "Hello",
                    isFromUser = false
                )
            )
        )
        composeTestRule.setContent {
            RoundTimerTheme {
                AiCoachScreen(
                    aiCoachUiState = aiStateUiState,
                    selectedCoachMode = CoachMode.CLOUD,
                ) {

                }
            }
        }

        composeTestRule
            .onNodeWithTag("google_sign_in_button")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Hello")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("test")
            .assertIsDisplayed()
    }

    @Test
    fun send_message() {
        val messages = listOf(
            CoachMessage(
                text = "Hello",
                isFromUser = false
            )
        )
        var intent: AiCoachIntent? = null
        composeTestRule.setContent {
            var aiCoachUiState by remember {
                mutableStateOf(
                    AiCoachUiState(
                        messages = messages,
                        isSignedIn = true,
                    ),
                )
            }
            RoundTimerTheme {
                AiCoachScreen(
                    aiCoachUiState = aiCoachUiState,
                    selectedCoachMode = CoachMode.CLOUD,
                ) {
                    intent = it
                    if (it is AiCoachIntent.InputChanged) {
                        aiCoachUiState = aiCoachUiState.copy(
                            input = it.text,
                        )
                    }
                }
            }
        }

        composeTestRule
            .onNodeWithTag("user_send_text_field")
            .performTextInput("testing")

        composeTestRule.runOnIdle {
            Assert.assertTrue(
                (intent as? AiCoachIntent.InputChanged)?.text == "testing"
            )
        }

        composeTestRule
            .onNodeWithTag("send_icon")
            .performClick()

        composeTestRule.runOnIdle {
            Assert.assertTrue(
                intent is AiCoachIntent.SendClicked
            )
        }

        composeTestRule
            .onNodeWithText("testing")
            .assertIsDisplayed()
    }

    @Test
    fun loadingState_isDisplayed() {
        composeTestRule.setContent {
            RoundTimerTheme {
                AiCoachScreen(
                    aiCoachUiState = AiCoachUiState(
                        isLoading = true,
                    ),
                    selectedCoachMode = CoachMode.CLOUD,
                    onIntent = {},
                )
            }
        }

        composeTestRule
            .onNodeWithText("AI Coach is thinking...")
            .assertIsDisplayed()
    }

    @Test
    fun errorMessage_isDisplayed() {
        composeTestRule.setContent {
            RoundTimerTheme {
                AiCoachScreen(
                    aiCoachUiState = AiCoachUiState(
                        errorMessage = "AI Coach is temporarily unavailable.",
                    ),
                    selectedCoachMode = CoachMode.CLOUD,
                    onIntent = {},
                )
            }
        }

        composeTestRule
            .onNodeWithText("AI Coach is temporarily unavailable.")
            .assertIsDisplayed()
    }
}