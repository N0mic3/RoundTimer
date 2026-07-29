package com.example.roundtimer.ui.navigation

import androidx.navigation3.runtime.NavKey
import com.example.roundtimer.ui.navigation.RoundInfoModel
import kotlinx.serialization.Serializable

@Serializable
sealed interface Screens : NavKey
@Serializable
data object StartScreenNavKey : Screens
@Serializable
data class RunningScreenNavKey(
    val roundInfoModel: RoundInfoModel
) : Screens

@Serializable
data object SavedTimersScreenNavKey : Screens