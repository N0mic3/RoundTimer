package com.example.roundtimer.ui.screens.aiCoachScreen

import com.example.roundtimer.domain.model.CoachMode

sealed interface AiCoachIntent {
    data class InputChanged(val text: String) : AiCoachIntent
    data class SendClicked(
        val coachMode: CoachMode
    ) : AiCoachIntent
    data class GoogleCredentialReceived(
        val idToken: String,
    ) : AiCoachIntent

    data class GoogleCredentialFailed(
        val message: String,
    ) : AiCoachIntent

    data class CoachModeChanged(
        val mode: CoachMode,
    ) : AiCoachIntent

    data object DownloadOnDeviceModelClicked : AiCoachIntent

    data class PlayReplyClicked(
        val text: String,
    ) : AiCoachIntent

    data object StopSpeechClicked : AiCoachIntent

    data object StartSpeechRecognize : AiCoachIntent

    data object StopSpeechRecognize : AiCoachIntent

    data object SpeechRecognitionPermissionDenied : AiCoachIntent
}