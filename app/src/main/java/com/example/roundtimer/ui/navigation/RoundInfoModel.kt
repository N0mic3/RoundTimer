package com.example.roundtimer.ui.navigation

import com.example.roundtimer.domain.model.TimeSettings
import kotlinx.serialization.Serializable

@Serializable
data class RoundInfoModel(
    val workDuration: Int,
    val restDuration: Int,
    val roundCount: Int
){
    fun toTimeSettings() = TimeSettings(
        workDuration = this.workDuration,
        restDuration = this.restDuration,
        roundCount = this.roundCount
    )
}
