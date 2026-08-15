package com.example.roundtimer.roundTimer

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.example.roundtimer.ui.components.GoogleSignInButton
import org.junit.Rule
import org.junit.Test

class GoogleSignInButtonTest {

    @get:Rule
    val composeTestRule = createComposeRule()


    @Test
    fun googleSignButtonTest() {
        composeTestRule.setContent {
            GoogleSignInButton(
                isSigningIn = false,
                onCredentialReceived = {
                }
            ) { }
        }

        composeTestRule
            .onNodeWithText("Sign in with Google")
            .assertIsDisplayed()
    }
}