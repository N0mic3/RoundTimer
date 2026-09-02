package com.example.roundtimer.domain.controller

import com.example.roundtimer.domain.model.TextToSpeechPlaybackEvent
import kotlinx.coroutines.flow.Flow

interface CoachTextToSpeechController {

    val playbackEvents: Flow<TextToSpeechPlaybackEvent>

    fun speak(text: String)

    fun stop()

    fun release()
}