package com.example.roundtimer.domain.usecase

import com.example.roundtimer.domain.model.CoachReply
import com.example.roundtimer.domain.repository.AiCoachRepository
import javax.inject.Inject

class GetAiCoachReplyUseCase @Inject constructor(
    private val aiCoachRepository: AiCoachRepository,
) {
    suspend fun getReply(
        userMessage: String,
    ): CoachReply {
        return aiCoachRepository.getReply(userMessage)
    }
}