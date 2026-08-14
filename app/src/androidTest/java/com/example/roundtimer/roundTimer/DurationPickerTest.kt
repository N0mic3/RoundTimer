package com.example.roundtimer.roundTimer

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.roundtimer.ui.components.DurationPicker
import com.example.roundtimer.ui.theme.RoundTimerTheme
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

class DurationPickerTest {

    @get:Rule
    val composeTestRule = createComposeRule()


    @Test
    fun selecting_updates_displayedValue_andCallsCallback() {
        var selectValue: Int? = null

        composeTestRule.setContent {
            RoundTimerTheme {
                var currentValue by remember {
                    mutableIntStateOf(5)
                }

                DurationPicker(
                    title = "Work Duration: ",
                    startValue = 5,
                    endValue = 60,
                    currentValue = currentValue,
                    step = 5,
                    units = "seconds",
                    onClick = { newValue ->
                        currentValue = newValue
                        selectValue = newValue
                    },
                )
            }
        }
        composeTestRule
            .onNodeWithText("5 seconds")
            .performClick()

        composeTestRule
            .onNodeWithText("10 seconds")
            .assertIsDisplayed()
            .performClick()


        composeTestRule
            .onNodeWithText("10 seconds")
            .assertIsDisplayed()

        composeTestRule.runOnIdle {
            Assert.assertEquals(10, selectValue)
        }
    }
}