package com.example.roundtimer.ui.screens.aiCoachScreen

import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.roundtimer.domain.controller.CoachSpeechRecognizerController
import com.example.roundtimer.domain.controller.CoachTextToSpeechController
import com.example.roundtimer.domain.controller.OnDeviceCoachAvailability
import com.example.roundtimer.domain.model.CoachMode
import com.example.roundtimer.domain.model.CoachRequest
import com.example.roundtimer.domain.model.CoachResponseState
import com.example.roundtimer.domain.model.OnDeviceCoachStatus
import com.example.roundtimer.domain.model.SpeechRecognitionEvent
import com.example.roundtimer.domain.model.TextToSpeechPlaybackEvent
import com.example.roundtimer.domain.usecase.AuthUseCase
import com.example.roundtimer.domain.usecase.GetAiCoachReplyUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
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
    private val coachSpeechRecognizerController: CoachSpeechRecognizerController
) : ViewModel() {

    private val _aiCoachUiState = MutableStateFlow(AiCoachUiState())
    val aiCoachUiState = _aiCoachUiState.asStateFlow()

    private val _streamingReply = MutableStateFlow<String?>(null)
    val streamingReply = _streamingReply.asStateFlow()

    private var retryableCoachRequest: CoachRequest? = null

    private var coachReplyJob: Job? = null

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

        viewModelScope.launch {
            coachSpeechRecognizerController.recognitionEvents.collect { speechRecognitionEvent ->
                when(speechRecognitionEvent) {
                    is SpeechRecognitionEvent.Error -> {
                        _aiCoachUiState.update {
                            it.copy(
                                errorMessage = speechRecognitionEvent.message,
                                micInput = null,
                                isRecognizing = false,
                                canRetryAiRequest = false,
                            )
                        }
                    }
                    is SpeechRecognitionEvent.FinalResult -> {
                        _aiCoachUiState.update {
                            it.copy(
                                input = "${it.input} ${speechRecognitionEvent.text}".trim(),
                                micInput = null,
                                isRecognizing = false
                            )
                        }
                    }
                    is SpeechRecognitionEvent.PartialResult -> {
                        _aiCoachUiState.update {
                            it.copy(
                                micInput = speechRecognitionEvent.text
                            )
                        }
                    }
                }
            }
        }
    }

    private fun requestCoachReply(
        coachRequest: CoachRequest,
    ) {
        coachReplyJob?.cancel()
        coachReplyJob = viewModelScope.launch {
            getAiCoachReplyUseCase.getReply(
                coachRequest = coachRequest
            ).collect { coachResponseState ->
                when (coachResponseState) {
                    CoachResponseState.Generating -> {

                    }
                    is CoachResponseState.Completed -> {
                        retryableCoachRequest = null
                        _streamingReply.value = null
                        _aiCoachUiState.update {
                            it.copy(
                                input = "",
                                messages = it.messages + CoachMessage(
                                    text = coachResponseState.reply.message,
                                    isFromUser = false,
                                ),
                                isLoading = false,
                                canRetryAiRequest = false
                            )
                        }
                    }
                    is CoachResponseState.Error -> {
                        retryableCoachRequest = coachRequest
                        _streamingReply.value = null
                        _aiCoachUiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = coachResponseState.message,
                                canRetryAiRequest = true
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
                                canRetryAiRequest = false
                            )
                        }
                        requestCoachReply(
                            CoachRequest(
                                userMessage = messageText,
                                coachMode = intent.coachMode,
                            )
                        )
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
                            canRetryAiRequest = false,
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
                        canRetryAiRequest = false
                    )
                }
            }

            is AiCoachIntent.CoachModeChanged -> {
                coachReplyJob?.cancel()
                coachReplyJob = null
                _streamingReply.value = null
                retryableCoachRequest = null
                when(intent.mode) {
                    CoachMode.CLOUD -> {
                        _aiCoachUiState.update {
                            it.copy(
                                messages = AiCoachUiState().messages,
                                isSignedIn = authUseCase.getCurrentUser() != null,
                                errorMessage = null,
                                isLoading = false,
                                canRetryAiRequest = false
                            )
                        }
                    }
                    CoachMode.ON_DEVICE -> {
                        viewModelScope.launch {
                            try {
                                val state = onDeviceCoachAvailability.getStatus()
                                _aiCoachUiState.update {
                                    it.copy(
                                        messages = AiCoachUiState().messages,
                                        onDeviceCoachStatus = state,
                                        errorMessage = null,
                                        isLoading = false,
                                        canRetryAiRequest = false
                                    )
                                }
                            } catch (exception: CancellationException) {
                                throw exception
                            } catch (_ : Exception) {
                                _aiCoachUiState.update {
                                    it.copy(
                                        messages = AiCoachUiState().messages,
                                        onDeviceCoachStatus = OnDeviceCoachStatus.UNAVAILABLE,
                                        errorMessage = "Unable to check on-device AI availability.",
                                        canRetryAiRequest = false,
                                        isLoading = false,
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
                        canRetryAiRequest = false,
                    )
                }
                viewModelScope.launch {
                    try {
                        onDeviceCoachAvailability.downloadModel()
                        val status = onDeviceCoachAvailability.getStatus()
                        _aiCoachUiState.update {
                            it.copy(
                                onDeviceCoachStatus = status,
                                errorMessage = null,
                                canRetryAiRequest = false,
                            )
                        }
                    } catch (exception: CancellationException) {
                        throw exception
                    } catch (_ : Exception) {
                        _aiCoachUiState.update {
                            it.copy(
                                onDeviceCoachStatus = OnDeviceCoachStatus.DOWNLOADABLE,
                                errorMessage = "Download failed, please retry later",
                                canRetryAiRequest = false
                            )
                        }
                    }
                }
            }
            is AiCoachIntent.PlayReplyClicked -> {
                coachTextToSpeechController.speak(intent.text)
                _aiCoachUiState.update {
                    it.copy(
                        speakingReply = intent.text,
                        micInput = null,
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

            AiCoachIntent.StartSpeechRecognize -> {
                if (_aiCoachUiState.value.isRecognizing) return
                coachTextToSpeechController.stop()
                _aiCoachUiState.update {
                    it.copy(
                        speakingReply = null,
                        isRecognizing = true
                    )
                }
                coachSpeechRecognizerController.startListening()
            }
            AiCoachIntent.StopSpeechRecognize -> {
                coachSpeechRecognizerController.stopListening()
                _aiCoachUiState.update {
                    it.copy(
                        isRecognizing = false,
                        micInput = null,
                    )
                }
            }

            AiCoachIntent.SpeechRecognitionPermissionDenied -> {
                _aiCoachUiState.update {
                    it.copy(
                        errorMessage = "Microphone permission is required for voice input.",
                        canRetryAiRequest = false
                    )
                }
            }

            AiCoachIntent.RetryClicked -> {
                val coachRequest = retryableCoachRequest ?: return
                if (_aiCoachUiState.value.isLoading) return
                _streamingReply.value = null
                _aiCoachUiState.update {
                    it.copy(
                        isLoading = true,
                        errorMessage = null,
                        canRetryAiRequest = false
                    )
                }
                requestCoachReply(coachRequest)
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
                        errorMessage = "Google sign-in failed. Please try again.",
                        canRetryAiRequest = false
                    )
                }
            }

        }
    }

    override fun onCleared() {
        coachTextToSpeechController.release()
        coachSpeechRecognizerController.release()
        super.onCleared()
    }
}