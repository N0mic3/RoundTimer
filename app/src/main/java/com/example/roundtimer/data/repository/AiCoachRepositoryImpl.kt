package com.example.roundtimer.data.repository

import com.example.roundtimer.domain.model.CoachReply
import com.example.roundtimer.domain.repository.AiCoachRepository
import com.google.firebase.functions.FirebaseFunctions
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AiCoachRepositoryImpl @Inject constructor(
    private val firebaseFunctions: FirebaseFunctions,
) : AiCoachRepository {
    override suspend fun getReply(userMessage: String): CoachReply {
        val result = firebaseFunctions
            .getHttpsCallable("askAiCoach")
            .call(
                mapOf("message" to userMessage)
            )
            .await()

        val data = result.data as? Map<*, *> ?: error("AI Coach returned an invalid response.")
        val reply = data["reply"] as? String ?: error("AI Coach response did not contain a reply.")
        return CoachReply(
            message = reply,
        )
    }

}