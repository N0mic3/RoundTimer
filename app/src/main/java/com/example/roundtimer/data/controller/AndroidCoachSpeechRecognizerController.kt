package com.example.roundtimer.data.controller

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import com.example.roundtimer.domain.controller.CoachSpeechRecognizerController
import com.example.roundtimer.domain.model.SpeechRecognitionEvent
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import java.util.Locale
import javax.inject.Inject

class AndroidCoachSpeechRecognizerController @Inject constructor(
    @param:ApplicationContext private val context: Context
) : CoachSpeechRecognizerController {
    private val _recognitionEvents = MutableSharedFlow<SpeechRecognitionEvent>(
        extraBufferCapacity = 1,
    )
    private var stoppedByUser = false

    override val recognitionEvents: Flow<SpeechRecognitionEvent>
        get() = _recognitionEvents.asSharedFlow()
    private var speechRecognizer: SpeechRecognizer? = null

    override fun startListening() {
        stoppedByUser = false
        if (!SpeechRecognizer.isRecognitionAvailable(context)) {
            _recognitionEvents.tryEmit(
                SpeechRecognitionEvent.Error(
                    message = "Speech recognition is not available on this device.",
                )
            )
            return
        }
        val recognizer = speechRecognizer ?: createSpeechRecognizer()
        recognizer.startListening(createRecognizerIntent())
    }

    override fun stopListening() {
        stoppedByUser = true
        speechRecognizer?.stopListening()
    }

    override fun release() {
        speechRecognizer?.destroy()
        speechRecognizer = null
    }

    private fun createSpeechRecognizer() = SpeechRecognizer.createSpeechRecognizer(context).also { recognizer ->
        speechRecognizer = recognizer
        recognizer.setRecognitionListener(object : RecognitionListener {
            override fun onBeginningOfSpeech() {}

            override fun onBufferReceived(p0: ByteArray?) {}

            override fun onEndOfSpeech() {}

            override fun onError(p0: Int) {
                if (stoppedByUser) {
                    stoppedByUser = false
                    return
                }
                _recognitionEvents.tryEmit(
                    SpeechRecognitionEvent.Error(
                         message = "Speech recognition failed. Please try again."
                    )
                )
            }

            override fun onEvent(p0: Int, p1: Bundle?) {}

            override fun onPartialResults(p0: Bundle?) {
                transcriptFrom(p0)?.let { text ->
                    _recognitionEvents.tryEmit(
                        SpeechRecognitionEvent.PartialResult(
                            text = text
                        )
                    )
                }
            }

            override fun onReadyForSpeech(p0: Bundle?) {}

            override fun onResults(p0: Bundle?) {
                stoppedByUser = false
                transcriptFrom(p0)?.let { text ->
                    _recognitionEvents.tryEmit(
                        SpeechRecognitionEvent.FinalResult(
                            text = text
                        )
                    )
                }
            }

            override fun onRmsChanged(p0: Float) {}

        })
    }

    private fun transcriptFrom(bundle : Bundle?) = bundle?.getStringArrayList(
        SpeechRecognizer.RESULTS_RECOGNITION
    )?.firstOrNull()?.trim()?.takeIf { it.isNotBlank() }

    private fun createRecognizerIntent() = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
        putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM,
        )
        putExtra(
            RecognizerIntent.EXTRA_LANGUAGE,
            Locale.getDefault().toLanguageTag(),
        )
        putExtra(
            RecognizerIntent.EXTRA_PARTIAL_RESULTS,
            true,
        )
    }
}