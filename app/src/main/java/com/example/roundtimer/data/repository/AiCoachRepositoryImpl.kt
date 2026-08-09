package com.example.roundtimer.data.repository

import com.example.roundtimer.domain.model.CoachReply
import com.example.roundtimer.domain.repository.AiCoachRepository
import kotlinx.coroutines.delay
import javax.inject.Inject

class AiCoachRepositoryImpl @Inject constructor() : AiCoachRepository {
    override suspend fun getReply(userMessage: String): CoachReply {
        delay(1000)
        return CoachReply(
            message = "I understand. I will help you create a focused timer plan.",
        )
    }

}