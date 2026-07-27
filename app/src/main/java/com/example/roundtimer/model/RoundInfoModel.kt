package com.example.roundtimer.model

import kotlinx.serialization.Serializable

@Serializable
data class RoundInfoModel(
    var workDuration: Int,
    var restDuration: Int,
    var roundCount: Int
)
