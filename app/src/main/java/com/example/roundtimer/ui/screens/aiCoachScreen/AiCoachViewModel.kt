package com.example.roundtimer.ui.screens.aiCoachScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.roundtimer.domain.usecase.AuthUseCase
import com.example.roundtimer.domain.usecase.GetAiCoachReplyUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AiCoachViewModel @Inject constructor(
    private val getAiCoachReplyUseCase: GetAiCoachReplyUseCase,
    private val authUseCase: AuthUseCase
) : ViewModel() {

    private val _aiCoachUiState = MutableStateFlow(AiCoachUiState())
    val aiCoachUiState = _aiCoachUiState.asStateFlow()

    init {
        _aiCoachUiState.update {
            it.copy(
                isSignedIn = authUseCase.getCurrentUser() != null,
            )
        }
    }

    fun onIntent(intent: AiCoachIntent) {
        when (intent) {
            is AiCoachIntent.InputChanged -> {
                _aiCoachUiState.value = _aiCoachUiState.value.copy(
                    input = intent.text
                )
            }
            AiCoachIntent.SendClicked -> {
                if (_aiCoachUiState.value.isSignedIn) {
                    val messageText = _aiCoachUiState.value.input.trim()
                    if (!messageText.isBlank() && !_aiCoachUiState.value.isLoading) {
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
                            try {
                                val reply = getAiCoachReplyUseCase.getReply(
                                    userMessage = messageText
                                )
                                _aiCoachUiState.update {
                                    it.copy(
                                        input = "",
                                        messages = it.messages + CoachMessage(
                                            text = reply.message,
                                            isFromUser = false,
                                        ),
                                        isLoading = false
                                    )
                                }
                            } catch (e: Exception) {
                                _aiCoachUiState.update {
                                    it.copy(
                                        isLoading = false,
                                        errorMessage = e.message ?: "Unable to reach AI Coach. Please try again.",
                                    )
                                }
                            }
                        }
                    }
                } else {
                    _aiCoachUiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "Sign in to use AI Coach",
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
}