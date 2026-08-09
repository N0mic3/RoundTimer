package com.example.roundtimer.domain.repository

import com.example.roundtimer.domain.model.CoachReply

interface AiCoachRepository {
    suspend fun getReply(
        userMessage: String,
    ): CoachReply
}