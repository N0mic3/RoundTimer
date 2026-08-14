package com.example.roundtimer.ui.navigation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.example.roundtimer.MainActivityViewModel
import com.example.roundtimer.R
import com.example.roundtimer.ui.screens.aiCoachScreen.AiCoachScreen
import com.example.roundtimer.ui.screens.aiCoachScreen.AiCoachViewModel
import com.example.roundtimer.ui.screens.running.RunningScreen
import com.example.roundtimer.ui.screens.running.RunningViewModel
import com.example.roundtimer.ui.screens.savedTimers.SavedTimerScreen
import com.example.roundtimer.ui.screens.savedTimers.SavedTimersViewModel
import com.example.roundtimer.ui.screens.start.StartScreen
import com.example.roundtimer.ui.screens.start.StartViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation(
    mainActivityViewModel: MainActivityViewModel
) {
    val backStack = rememberNavBackStack(StartScreenNavKey)
    fun handleBackNavigation() {
        if (backStack.size > 1) {
            when (backStack.lastOrNull()) {
                is RunningScreenNavKey -> {
                    mainActivityViewModel.stopTimerSession()
                }
                else -> {}
            }
            backStack.removeAt(backStack.lastIndex)
        }
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
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = backStack.lastOrNull()?.let {
                            (it as? Screens)?.screenTitle
                        } ?: "Unknown Screen"
                    )
                },
                navigationIcon = {
                    if (backStack.size > 1) {
                        IconButton(
                            onClick = ::handleBackNavigation
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_left_arrow),
                                contentDescription = "back button"
                            )
                        }
                    }
                },
                actions = {
                    (backStack.lastOrNull() as? StartScreenNavKey)?.let {
                        Icon(
                            painter = painterResource(R.drawable.ic_ai_robot),
                            contentDescription = "AI Coach button",
                            modifier = Modifier.clickable {
                                backStack.add(AICoachScreenNavKey)
                            }
                        )
                        Icon(
                            painter = painterResource(R.drawable.ic_list),
                            contentDescription = "Saved Timers button",
                            modifier = Modifier.clickable {
                                backStack.add(SavedTimersScreenNavKey)
                            }
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            NavDisplay(
                backStack = backStack,
                onBack = ::handleBackNavigation,
                entryDecorators = listOf(
                    rememberSaveableStateHolderNavEntryDecorator(),
                    rememberViewModelStoreNavEntryDecorator()
                ),
                entryProvider = entryProvider {
                    entry<StartScreenNavKey> {
                        val startViewModel: StartViewModel = hiltViewModel()
                        val quoteUiState = startViewModel.stateUiState.collectAsStateWithLifecycle()
                        StartScreen(
                            navigateToRunningScreen = ::navigateToRunningScreen,
                            quoteUiState = quoteUiState.value,
                            insertSavedTimer = { name, timeSetting ->
                                startViewModel.insertSavedTimer(
                                    name = name,
                                    timeSettings = timeSetting
                                )
                            },
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
                            runningViewModel = runningViewModel
                        )
                    }
                    entry<SavedTimersScreenNavKey> {
                        val savedTimersViewModel = hiltViewModel<SavedTimersViewModel>()
                        SavedTimerScreen(
                            navigateToRunningScreen = ::navigateToRunningScreen,
                            savedTimersViewModel = savedTimersViewModel
                        )
                    }

                    entry<AICoachScreenNavKey> {
                        val aiCoachViewModel: AiCoachViewModel = hiltViewModel()
                        AiCoachScreen(
                            aiCoachViewModel = aiCoachViewModel
                        )
                    }
                }
            )
        }
    }
}