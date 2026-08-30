package com.example.roundtimer.domain.usecase

import com.example.roundtimer.domain.controller.CoachEngine
import com.example.roundtimer.domain.model.CoachRequest
import javax.inject.Inject

class GetAiCoachReplyUseCase @Inject constructor(
    private val coachEngine: CoachEngine
) {
    fun getReply(
        coachRequest: CoachRequest
    ) = coachEngine.getReply(coachRequest)

}