package com.example.roundtimer.ui.screens.aiCoachScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.roundtimer.domain.usecase.GetAiCoachReplyUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AiCoachViewModel @Inject constructor(
    private val getAiCoachReplyUseCase: GetAiCoachReplyUseCase
) : ViewModel() {

    private val _aiCoachUiState = MutableStateFlow(AiCoachUiState())
    val aiCoachUiState = _aiCoachUiState.asStateFlow()

    fun onIntent(intent: AiCoachIntent) {
        when (intent) {
            is AiCoachIntent.InputChanged -> {
                _aiCoachUiState.value = _aiCoachUiState.value.copy(
                    input = intent.text
                )
            }
            AiCoachIntent.SendClicked -> {
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
                        } catch (_: Exception) {
                            _aiCoachUiState.update {
                                it.copy(
                                    isLoading = false,
                                    errorMessage = "Unable to reach AI Coach. Please try again.",
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}