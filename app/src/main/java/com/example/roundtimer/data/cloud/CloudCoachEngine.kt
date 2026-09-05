package com.example.roundtimer.data.cloud

import com.example.roundtimer.domain.model.CoachReply
import com.example.roundtimer.domain.model.CoachRequest
import com.example.roundtimer.domain.model.CoachResponseState
import com.google.firebase.functions.FirebaseFunctions
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

class CloudCoachEngine @Inject constructor(
    private val firebaseFunctions: FirebaseFunctions,
) {
    fun getReply(coachRequest: CoachRequest): Flow<CoachResponseState> {
        return flow {
            emit(CoachResponseState.Generating)

            try {
                val result = firebaseFunctions
                    .getHttpsCallable("askAiCoach")
                    .call(mapOf("message" to coachRequest.userMessage))
                    .await()

                val data = result.data as? Map<*, *>
                    ?: error("AI Coach returned an invalid response.")

                val reply = (data["reply"] as? String)?.trim()
                    ?: error("AI Coach response did not contain a reply.")

                emit(
                    CoachResponseState.Completed(
                        CoachReply(message = reply),
                    ),
                )
            } catch (exception: CancellationException) {
                throw exception
            } catch (_: Exception) {
                emit(
                    CoachResponseState.Error("Unable to reach AI Coach. Please try again.",),
                )
            }
        }
    }

}