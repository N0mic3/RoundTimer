package com.example.roundtimer.ui.screens.aiCoachScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.roundtimer.domain.controller.CoachTextToSpeechController
import com.example.roundtimer.domain.controller.OnDeviceCoachAvailability
import com.example.roundtimer.domain.model.CoachMode
import com.example.roundtimer.domain.model.CoachRequest
import com.example.roundtimer.domain.model.CoachResponseState
import com.example.roundtimer.domain.model.OnDeviceCoachStatus
import com.example.roundtimer.domain.model.TextToSpeechPlaybackEvent
import com.example.roundtimer.domain.usecase.AuthUseCase
import com.example.roundtimer.domain.usecase.GetAiCoachReplyUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

@HiltViewModel
class AiCoachViewModel @Inject constructor(
    private val getAiCoachReplyUseCase: GetAiCoachReplyUseCase,
    private val authUseCase: AuthUseCase,
    private val onDeviceCoachAvailability: OnDeviceCoachAvailability,
    private val coachTextToSpeechController: CoachTextToSpeechController,
) : ViewModel() {

    private val _aiCoachUiState = MutableStateFlow(AiCoachUiState())
    val aiCoachUiState = _aiCoachUiState.asStateFlow()

    private val _streamingReply = MutableStateFlow<String?>(null)
    val streamingReply = _streamingReply.asStateFlow()

    init {
        viewModelScope.launch {
            coachTextToSpeechController.playbackEvents.collect {
                when(it) {
                    TextToSpeechPlaybackEvent.FINISHED,
                    TextToSpeechPlaybackEvent.ERROR -> {
                        _aiCoachUiState.update {
                            it.copy(
                                speakingReply = null
                            )
                        }
                    }
                    TextToSpeechPlaybackEvent.STARTED -> {}
                }
            }
        }
    }

    fun onIntent(intent: AiCoachIntent) {
        when (intent) {
            is AiCoachIntent.InputChanged -> {
                _aiCoachUiState.value = _aiCoachUiState.value.copy(
                    input = intent.text
                )
            }
            is AiCoachIntent.SendClicked -> {
                val canSend = when (intent.coachMode) {
                    CoachMode.CLOUD -> _aiCoachUiState.value.isSignedIn

                    CoachMode.ON_DEVICE ->
                        _aiCoachUiState.value.onDeviceCoachStatus ==
                                OnDeviceCoachStatus.AVAILABLE
                }
                if (canSend) {
                    val messageText = _aiCoachUiState.value.input.trim()
                    if (!messageText.isBlank() && !_aiCoachUiState.value.isLoading) {
                        _streamingReply.value = null
                        _aiCoachUiState.update {
                            it.copy(
                                input = "",
                                messages = it.messages + CoachMessage(
                                    text = messageText,
                                    isFromUser = true
                                ),
                                isLoading = true,
                                errorMessage = null,
                            )
                        }
                        viewModelScope.launch {
                            getAiCoachReplyUseCase.getReply(
                                coachRequest = CoachRequest(
                                    userMessage = messageText,
                                    coachMode = intent.coachMode
                                )
                            ).collect { coachResponseState ->
                                when (coachResponseState) {
                                    CoachResponseState.Generating -> {

                                    }
                                    is CoachResponseState.Completed -> {
                                        _streamingReply.value = null
                                        _aiCoachUiState.update {
                                            it.copy(
                                                input = "",
                                                messages = it.messages + CoachMessage(
                                                    text = coachResponseState.reply.message,
                                                    isFromUser = false,
                                                ),
                                                isLoading = false
                                            )
                                        }
                                    }
                                    is CoachResponseState.Error -> {
                                        _streamingReply.value = null
                                        _aiCoachUiState.update {
                                            it.copy(
                                                isLoading = false,
                                                errorMessage = coachResponseState.message,
                                            )
                                        }
                                    }
                                    is CoachResponseState.PartialResponse -> {
                                        _streamingReply.value = coachResponseState.text
                                    }
                                }
                            }
                        }
                    }
                } else {
                    val errorMessage = when (intent.coachMode) {
                        CoachMode.CLOUD ->
                            "Sign in to use Cloud AI Coach."

                        CoachMode.ON_DEVICE ->
                            "On-device AI is not ready yet."
                    }
                    _streamingReply.value = null
                    _aiCoachUiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = errorMessage,
                        )
                    }
                }
            }
            is AiCoachIntent.GoogleCredentialReceived -> {
                signInWithGoogle(intent.idToken)
            }
            is AiCoachIntent.GoogleCredentialFailed -> {
                _aiCoachUiState.update {
                    it.copy(
                        isSigningIn = false,
                        errorMessage = intent.message,
                    )
                }
            }

            is AiCoachIntent.CoachModeChanged -> {
                when(intent.mode) {
                    CoachMode.CLOUD -> {
                        _aiCoachUiState.update {
                            it.copy(
                                isSignedIn = authUseCase.getCurrentUser() != null,
                                errorMessage = null
                            )
                        }
                    }
                    CoachMode.ON_DEVICE -> {
                        viewModelScope.launch {
                            try {
                                val state = onDeviceCoachAvailability.getStatus()
                                _aiCoachUiState.update {
                                    it.copy(
                                        onDeviceCoachStatus = state,
                                        errorMessage = null
                                    )
                                }
                            } catch (exception: CancellationException) {
                                throw exception
                            } catch (_ : Exception) {
                                _aiCoachUiState.update {
                                    it.copy(
                                        onDeviceCoachStatus = OnDeviceCoachStatus.UNAVAILABLE,
                                        errorMessage = "Unable to check on-device AI availability.",
                                    )
                                }
                            }
                        }
                    }
                }
            }
            AiCoachIntent.DownloadOnDeviceModelClicked -> {
                if (
                    _aiCoachUiState.value.onDeviceCoachStatus !=
                    OnDeviceCoachStatus.DOWNLOADABLE
                ) return
                _aiCoachUiState.update {
                    it.copy(
                        onDeviceCoachStatus = OnDeviceCoachStatus.DOWNLOADING,
                        errorMessage = null,
                    )
                }
                viewModelScope.launch {
                    try {
                        onDeviceCoachAvailability.downloadModel()
                        val status = onDeviceCoachAvailability.getStatus()
                        _aiCoachUiState.update {
                            it.copy(
                                onDeviceCoachStatus = status,
                                errorMessage = null
                            )
                        }
                    } catch (exception: CancellationException) {
                        throw exception
                    } catch (_ : Exception) {
                        _aiCoachUiState.update {
                            it.copy(
                                onDeviceCoachStatus = OnDeviceCoachStatus.DOWNLOADABLE,
                                errorMessage = "Download failed, please retry later",
                            )
                        }
                    }
                }
            }
            is AiCoachIntent.PlayReplyClicked -> {
                coachTextToSpeechController.speak(intent.text)
                _aiCoachUiState.update {
                    it.copy(
                          speakingReply = intent.text
                    )
                }
            }

            AiCoachIntent.StopSpeechClicked -> {
                coachTextToSpeechController.stop()
                _aiCoachUiState.update {
                    it.copy(
                        speakingReply = null
                    )
                }
            }
        }
    }

    private fun signInWithGoogle(idToken : String) {
        viewModelScope.launch {
            _aiCoachUiState.update {
                it.copy(
                    isSigningIn = true,
                    errorMessage = null,
                )
            }
            try {
                authUseCase.signInWithGoogle(idToken)
                _aiCoachUiState.update {
                    it.copy(
                        isSignedIn = true,
                        isSigningIn = false,
                    )
                }
            } catch (exception: CancellationException) {
                throw exception
            } catch (_ : Exception) {
                _aiCoachUiState.update {
                    it.copy(
                        isSigningIn = false,
                        errorMessage = "Google sign-in failed. Please try again."
                    )
                }
            }

        }
    }

    override fun onCleared() {
        coachTextToSpeechController.release()
        super.onCleared()
    }
}