package com.example.roundtimer.ui.screens.aiCoachScreen

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.roundtimer.R
import com.example.roundtimer.ui.components.GoogleSignInButton

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AiCoachScreen(
    aiCoachUiState : AiCoachUiState,
    onIntent: (AiCoachIntent) -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val isKeyboardVisible = WindowInsets.isImeVisible
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        if (!aiCoachUiState.isSignedIn) {
            GoogleSignInButton(
                isSigningIn = aiCoachUiState.isSigningIn,
                onCredentialReceived = { idToken ->
                    onIntent(
                        AiCoachIntent.GoogleCredentialReceived(idToken),
                    )
                },
                onCredentialFailed = { message ->
                    onIntent(
                        AiCoachIntent.GoogleCredentialFailed(message),
                    )
                },
            )
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 12.dp
                )
                .weight(1f)
                .clickable(
                    enabled = isKeyboardVisible,
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                ) {
                    keyboardController?.hide()
                    focusManager.clearFocus()
                }
        ) {
            items(aiCoachUiState.messages) { message ->
                Log.d("MMM_Testing", "AiCoachScreen: ${message.text}")
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = message.text,
                    textAlign = if (message.isFromUser) TextAlign.End else TextAlign.Start
                )
            }
            if (aiCoachUiState.isLoading) {
                item {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "AI Coach is thinking..."
                    )
                }
            }
            aiCoachUiState.errorMessage?.let { errorMessage ->
                item {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = errorMessage
                    )
                }
            }
        }
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .testTag("user_send_text_field"),
            value = aiCoachUiState.input,
            onValueChange = {
                onIntent(
                    AiCoachIntent.InputChanged(it)
                )
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Send
            ),
            keyboardActions = KeyboardActions(
                onSend = {
                    onIntent(
                        AiCoachIntent.SendClicked
                    )
                }
            ),
            trailingIcon = {
                IconButton(
                    modifier = Modifier.testTag("send_icon"),
                    enabled = !aiCoachUiState.isLoading && aiCoachUiState.input.isNotBlank(),
                    onClick = {
                        onIntent(
                            AiCoachIntent.SendClicked
                        )
                    }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.Send,
                        contentDescription = "Send message"
                    )
                }
            }
        )
    }
}