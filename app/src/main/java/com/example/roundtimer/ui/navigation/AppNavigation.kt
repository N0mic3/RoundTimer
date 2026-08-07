package com.example.roundtimer.ui.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.example.roundtimer.MainActivityViewModel
import com.example.roundtimer.ui.running.RunningScreen
import com.example.roundtimer.ui.running.RunningViewModel
import com.example.roundtimer.ui.savedTimers.SavedTimerScreen
import com.example.roundtimer.ui.savedTimers.SavedTimersViewModel
import com.example.roundtimer.ui.start.StartScreen
import com.example.roundtimer.ui.start.StartViewModel

@Composable
fun AppNavigation(
    mainActivityViewModel: MainActivityViewModel
) {
    val backStack = rememberNavBackStack(StartScreenNavKey)
    fun popBackStack() {
        if (backStack.size > 1) {
            backStack.removeAt(backStack.lastIndex)
        }
    }
    fun stopTimerAndPop() {
        mainActivityViewModel.stopTimerSession()
        popBackStack()
    }

    fun navigateToRunningScreen(
        roundInfoModel: RoundInfoModel,
    ) {
        mainActivityViewModel.startTimerSession()
        backStack.add(
            RunningScreenNavKey(
                roundInfoModel = roundInfoModel,
            )
        )
    }
    NavDisplay(
        backStack = backStack,
        onBack = {
            when(backStack.lastOrNull()) {
                is RunningScreenNavKey -> stopTimerAndPop()
                else -> popBackStack()
            }
        },
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider = entryProvider {
            entry<StartScreenNavKey> {
                val startViewModel: StartViewModel = hiltViewModel()
                StartScreen(
                    navigateToRunningScreen = ::navigateToRunningScreen,
                    navigateToSavedTimersScreen = {
                        backStack.add(SavedTimersScreenNavKey)
                    },
                    startViewModel = startViewModel
                )
            }
            entry<RunningScreenNavKey> {
                val runningViewModel: RunningViewModel = hiltViewModel<RunningViewModel, RunningViewModel.Factory>(
                    creationCallback = { factory ->
                        factory.create(it.roundInfoModel.toTimeSettings())
                    }
                )
                RunningScreen(
                    roundInfoModel = it.roundInfoModel,
                    onBackClick = ::stopTimerAndPop,
                    runningViewModel = runningViewModel
                )
            }
            entry<SavedTimersScreenNavKey> {
                val savedTimersViewModel = hiltViewModel<SavedTimersViewModel>()
                SavedTimerScreen(
                    onBackClick = ::popBackStack,
                    navigateToRunningScreen = ::navigateToRunningScreen,
                    savedTimersViewModel = savedTimersViewModel
                )
            }
        }
    )
}