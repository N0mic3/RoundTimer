package com.example.roundtimer.data.controller

import android.content.Context
import android.speech.tts.TextToSpeech
import com.example.roundtimer.domain.controller.CoachTextToSpeechController
import com.example.roundtimer.domain.model.TextToSpeechPlaybackEvent
import dagger.hilt.android.qualifiers.ApplicationContext
import android.speech.tts.UtteranceProgressListener
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import java.util.Locale
import javax.inject.Inject

class AndroidCoachTextToSpeechController @Inject constructor(
    @ApplicationContext context: Context
) : CoachTextToSpeechController {

    private val _playbackEvents = MutableSharedFlow<TextToSpeechPlaybackEvent>(
        extraBufferCapacity = 1,
    )

    override val playbackEvents = _playbackEvents.asSharedFlow()
    private var isInitialized = false
    private var textToSpeech: TextToSpeech

    init {
        textToSpeech = TextToSpeech(context) { status ->
            isInitialized = status == TextToSpeech.SUCCESS
            if (isInitialized) {
                textToSpeech.language = Locale.getDefault()
            }
        }
        textToSpeech.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onDone(p0: String?) {
                _playbackEvents.tryEmit(
                    TextToSpeechPlaybackEvent.FINISHED
                )
            }

            override fun onError(utteranceId: String?, errorCode: Int) {
                _playbackEvents.tryEmit(
                    TextToSpeechPlaybackEvent.ERROR
                )
            }

            @Deprecated("Deprecated in Java")
            override fun onError(p0: String?) {
                _playbackEvents.tryEmit(
                    TextToSpeechPlaybackEvent.ERROR
                )
            }

            override fun onStart(p0: String?) {
                _playbackEvents.tryEmit(
                    TextToSpeechPlaybackEvent.STARTED
                )
            }

        })
    }
    override fun speak(text: String) {
        if (isInitialized && text.isNotBlank()) {
            textToSpeech.speak(
                text,
                TextToSpeech.QUEUE_FLUSH,
                null,
                "ai_coach_reply"
            )
        }
    }

    override fun stop() {
        textToSpeech.stop()
    }

    override fun release() {
        textToSpeech.stop()
        textToSpeech.shutdown()
    }
}