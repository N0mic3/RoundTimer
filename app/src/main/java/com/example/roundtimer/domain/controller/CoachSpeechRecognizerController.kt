package com.example.roundtimer.domain.controller

import com.example.roundtimer.domain.model.SpeechRecognitionEvent
import kotlinx.coroutines.flow.Flow

interface CoachSpeechRecognizerController {
    val recognitionEvents: Flow<SpeechRecognitionEvent>

    fun startListening()

    fun stopListening()

    fun release()
}