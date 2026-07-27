package com.example.roundtimer.model

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface Screens : NavKey
@Serializable
data object StartScreenNavKey : Screens
@Serializable
data class RunningScreenNavKey(
    val roundInfoModel: RoundInfoModel
) : Screens