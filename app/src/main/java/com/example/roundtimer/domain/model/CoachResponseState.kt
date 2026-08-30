package com.example.roundtimer.domain.model

sealed interface CoachResponseState {
    data object Generating : CoachResponseState

    data class PartialResponse(
        val text: String,
    ) : CoachResponseState

    data class Completed(
        val reply: CoachReply,
    ) : CoachResponseState

    data class Error(
        val message: String,
    ) : CoachResponseState
}