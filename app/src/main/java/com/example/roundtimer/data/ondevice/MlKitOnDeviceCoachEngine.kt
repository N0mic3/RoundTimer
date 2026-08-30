package com.example.roundtimer.data.ondevice

import com.example.roundtimer.domain.model.CoachReply
import com.example.roundtimer.domain.model.CoachRequest
import com.example.roundtimer.domain.model.CoachResponseState
import com.google.mlkit.genai.common.FeatureStatus
import com.google.mlkit.genai.prompt.Generation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

class MlKitOnDeviceCoachEngine @Inject constructor() {
    fun getReply(coachRequest: CoachRequest): Flow<CoachResponseState> {
        return flow {
            val model = Generation.getClient()
            try {
                when (model.checkStatus()) {
                    FeatureStatus.AVAILABLE -> {}

                    FeatureStatus.DOWNLOADABLE -> {
                        emit(
                            CoachResponseState.Error(
                                "On-device AI needs to be downloaded first.",
                            ),
                        )
                        return@flow
                    }

                    FeatureStatus.DOWNLOADING -> {
                        emit(
                            CoachResponseState.Error(
                                "On-device AI is downloading.",
                            ),
                        )
                        return@flow
                    }
                    else -> {
                        emit(
                            CoachResponseState.Error(
                                "On-device AI is not available on this device.",
                            ),
                        )
                        return@flow
                    }
                }

                emit(CoachResponseState.Generating)

                val fullReply = StringBuilder()
                model.generateContentStream(
                    buildPrompt(coachRequest)
                ).collect { chunk ->
                    val newText = chunk.candidates
                        .firstOrNull()?.text.orEmpty()

                    if (newText.isNotBlank()) {
                        fullReply.append(newText)
                        emit(
                            CoachResponseState.PartialResponse(
                                text = fullReply.toString(),
                            ),
                        )
                    }
                }

                val completedReply = fullReply.toString().trim()

                if (completedReply.isBlank()) {
                    emit(
                        CoachResponseState.Error(
                            "On-device AI did not return a response.",
                        ),
                    )
                } else {
                    emit(
                        CoachResponseState.Completed(
                            reply = CoachReply(
                                message = completedReply,
                            ),
                        ),
                    )
                }
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception : Exception) {
                emit(
                    CoachResponseState.Error(
                        exception.message
                            ?: "On-device AI is temporarily unavailable.",
                    ),
                )
            } finally {
                model.close()
            }
        }
    }

    private fun buildPrompt(
        coachRequest: CoachRequest
    ): String {
        return """
            You are RoundTimer's encouraging AI Coach.
            Help the user choose realistic focus and rest intervals.
            Reply in 2 to 4 concise, practical sentences.
            Use plain text only.
            User message: ${coachRequest.userMessage}
        """.trimIndent()
    }
}