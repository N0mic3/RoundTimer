package com.example.roundtimer.ui.screens.aiCoachScreen

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material.icons.outlined.Mic
import androidx.compose.material.icons.outlined.Stop
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.roundtimer.domain.model.CoachMode
import com.example.roundtimer.domain.model.OnDeviceCoachStatus
import com.example.roundtimer.ui.components.GoogleSignInButton
import kotlinx.coroutines.flow.StateFlow

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AiCoachScreen(
    aiCoachUiState : AiCoachUiState,
    selectedCoachMode: CoachMode,
    streamingReplyFlow: StateFlow<String?>,
    onIntent: (AiCoachIntent) -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val isKeyboardVisible = WindowInsets.isImeVisible
    val context = LocalContext.current
    val microphonePermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(), { granted ->
            if (!granted) {
                onIntent(
                    AiCoachIntent.SpeechRecognitionPermissionDenied
                )
            }
        }
    )
    LaunchedEffect(selectedCoachMode) {
        onIntent(
            AiCoachIntent.CoachModeChanged(selectedCoachMode)
        )
    }
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        when(selectedCoachMode) {
            CoachMode.CLOUD -> {
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
            }
            CoachMode.ON_DEVICE -> {
                if (aiCoachUiState.onDeviceCoachStatus != OnDeviceCoachStatus.AVAILABLE) {
                    Button(
                        onClick = {
                            onIntent(
                                AiCoachIntent.DownloadOnDeviceModelClicked
                            )
                        },
                        enabled = aiCoachUiState.onDeviceCoachStatus == OnDeviceCoachStatus.DOWNLOADABLE
                    ) {
                        Text(
                            text = aiCoachUiState.onDeviceCoachStatus.displayMessage
                        )
                    }
                }
            }
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
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = message.text,
                        textAlign = if (message.isFromUser) TextAlign.End else TextAlign.Start
                    )
                    if (!message.isFromUser) {
                        Button(
                            onClick = {
                                if (aiCoachUiState.speakingReply == message.text) {
                                    onIntent(
                                        AiCoachIntent.StopSpeechClicked
                                    )
                                } else {
                                    onIntent(
                                        AiCoachIntent.PlayReplyClicked(
                                            text = message.text,
                                        ),
                                    )
                                }
                            },
                        ) {
                            Text(
                                if (aiCoachUiState.speakingReply == message.text) "Stop" else "Play aloud"
                            )
                        }
                    }
                }
            }
            item {
                val streamingReply by streamingReplyFlow.collectAsStateWithLifecycle()
                streamingReply?.let { streamingReply ->
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = streamingReply,
                        textAlign = TextAlign.Start,
                    )
                } ?: run {
                    if (aiCoachUiState.isLoading) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = "AI Coach is thinking..."
                        )
                    }
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
            value = aiCoachUiState.input + if (aiCoachUiState.micInput.isNullOrBlank()) "" else {
                " ${aiCoachUiState.micInput}"
            },
            onValueChange = {
                onIntent(
                    AiCoachIntent.InputChanged(it)
                )
            },
            readOnly = aiCoachUiState.isRecognizing,
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Send
            ),
            keyboardActions = KeyboardActions(
                onSend = {
                    onIntent(
                        AiCoachIntent.SendClicked(
                            coachMode = selectedCoachMode
                        )
                    )
                }
            ),
            trailingIcon = {
                IconButton(
                    modifier = Modifier.testTag("send_icon"),
                    enabled = !aiCoachUiState.isLoading && aiCoachUiState.input.isNotBlank(),
                    onClick = {
                        onIntent(
                            AiCoachIntent.SendClicked(
                                coachMode = selectedCoachMode
                            )
                        )
                    }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.Send,
                        contentDescription = "Send message"
                    )
                }
            },
            leadingIcon = {
                IconButton(
                    onClick = {
                        if (aiCoachUiState.isRecognizing) {
                            onIntent(AiCoachIntent.StopSpeechRecognize)
                        } else {
                            val hasMicrophonePermission = ContextCompat.checkSelfPermission(
                                context,
                                Manifest.permission.RECORD_AUDIO
                            ) == PackageManager.PERMISSION_GRANTED
                            if (hasMicrophonePermission) {
                                onIntent(AiCoachIntent.StartSpeechRecognize)
                            } else {
                                microphonePermissionLauncher.launch(
                                    Manifest.permission.RECORD_AUDIO,
                                )
                            }
                        }
                    },
                ) {
                    Icon(
                        imageVector = if (aiCoachUiState.isRecognizing) {
                            Icons.Outlined.Stop
                        } else {
                            Icons.Outlined.Mic
                        },
                        contentDescription = if (aiCoachUiState.isRecognizing) {
                            "Stop listening"
                        } else {
                            "Start voice input"
                        },
                    )
                }
            }
        )
    }
}