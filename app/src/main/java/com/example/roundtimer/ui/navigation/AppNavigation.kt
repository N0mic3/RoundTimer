package com.example.roundtimer.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.example.roundtimer.domain.usecase.QuoteUseCase
import com.example.roundtimer.domain.usecase.TimeUseCase
import com.example.roundtimer.ui.running.RunningScreen
import com.example.roundtimer.ui.running.RunningViewModel
import com.example.roundtimer.ui.start.StartScreen
import com.example.roundtimer.ui.start.StartViewModel

@Composable
fun AppNavigation() {
    val backStack = rememberNavBackStack(StartScreenNavKey)
    NavDisplay(
        backStack = backStack,
        onBack = {
            if (backStack.size > 1) {
                backStack.removeAt(backStack.lastIndex)
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
                    onClick = {
                        backStack.add(RunningScreenNavKey(
                            roundInfoModel = it
                        ))
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
                    onBackClick = {
                        if (backStack.size > 1) {
                            backStack.removeAt(backStack.lastIndex)
                        }
                    },
                    runningViewModel = runningViewModel
                )
            }
        }
    )
}