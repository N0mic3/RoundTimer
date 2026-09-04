package com.example.roundtimer.domain.model

sealed interface SpeechRecognitionEvent {
    data class PartialResult(
        val text: String,
    ) : SpeechRecognitionEvent

    data class FinalResult(
        val text: String,
    ) : SpeechRecognitionEvent

    data class Error(
        val message: String,
    ) : SpeechRecognitionEvent
}