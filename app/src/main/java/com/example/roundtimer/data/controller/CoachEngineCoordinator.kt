package com.example.roundtimer.data.controller

import com.example.roundtimer.data.cloud.CloudCoachEngine
import com.example.roundtimer.data.ondevice.MlKitOnDeviceCoachEngine
import com.example.roundtimer.domain.controller.CoachEngine
import com.example.roundtimer.domain.model.CoachMode
import com.example.roundtimer.domain.model.CoachRequest
import com.example.roundtimer.domain.model.CoachResponseState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CoachEngineCoordinator @Inject constructor(
    private val cloudCoachEngine: CloudCoachEngine,
    private val mlKitOnDeviceCoachEngine: MlKitOnDeviceCoachEngine,
) : CoachEngine {

    override fun getReply(
        coachRequest: CoachRequest,
    ): Flow<CoachResponseState> {
        return when (coachRequest.coachMode) {
            CoachMode.CLOUD ->
                cloudCoachEngine.getReply(coachRequest)

            CoachMode.ON_DEVICE ->
                mlKitOnDeviceCoachEngine.getReply(coachRequest)
        }
    }
}