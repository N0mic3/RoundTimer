package com.example.roundtimer.domain.controller

import com.example.roundtimer.domain.model.CoachRequest
import com.example.roundtimer.domain.model.CoachResponseState
import kotlinx.coroutines.flow.Flow

interface CoachEngine {
    fun getReply(
        coachRequest: CoachRequest
    ) : Flow<CoachResponseState>
}