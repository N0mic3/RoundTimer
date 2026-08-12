package com.example.roundtimer.ui.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface Screens : NavKey {
    val screenTitle: String
}

@Serializable
data object StartScreenNavKey : Screens {
    override val screenTitle: String = "Start Screen"
}

@Serializable
data class RunningScreenNavKey(
    val roundInfoModel: RoundInfoModel
) : Screens {
    override val screenTitle: String = "Running Screen"
}

@Serializable
data object SavedTimersScreenNavKey : Screens {
    override val screenTitle: String = "Saved Timer Screen"
}

@Serializable
data object AICoachScreenNavKey : Screens {
    override val screenTitle: String = "AI Coach Screen"
}