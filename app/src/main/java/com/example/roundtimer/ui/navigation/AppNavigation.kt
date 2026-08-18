package com.example.roundtimer.ui.navigation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.SmartToy
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.example.roundtimer.ui.screens.settings.SettingsScreen
import com.example.roundtimer.ui.screens.settings.SettingsViewModel
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
                                imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                                contentDescription = "back button"
                            )
                        }
                    } else {
                        IconButton(
                            onClick = {
                                backStack.add(SettingsScreenNavKey)
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Settings,
                                contentDescription = "Setting button"
                            )
                        }
                    }
                },
                actions = {
                    (backStack.lastOrNull() as? StartScreenNavKey)?.let {
                        Icon(
                            imageVector = Icons.Outlined.SmartToy,
                            contentDescription = "AI Coach button",
                            modifier = Modifier.clickable {
                                backStack.add(AICoachScreenNavKey)
                            }
                        )
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.List,
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
                        val quoteUiState by startViewModel.stateUiState.collectAsStateWithLifecycle()
                        StartScreen(
                            navigateToRunningScreen = ::navigateToRunningScreen,
                            quoteUiState = quoteUiState,
                            insertSavedTimer = startViewModel::insertSavedTimer,
                        )
                    }
                    entry<RunningScreenNavKey> {
                        val runningViewModel: RunningViewModel = hiltViewModel<RunningViewModel, RunningViewModel.Factory>(
                            creationCallback = { factory ->
                                factory.create(it.roundInfoModel.toTimeSettings())
                            }
                        )
                        val timerUiState by runningViewModel.timerUiState.collectAsStateWithLifecycle()
                        RunningScreen(
                            roundInfoModel = it.roundInfoModel,
                            timerUiState = timerUiState,
                            onMainButtonClick = runningViewModel::onMainButtonClick,
                        )
                    }
                    entry<SavedTimersScreenNavKey> {
                        val savedTimersViewModel = hiltViewModel<SavedTimersViewModel>()
                        val saveTimeUiState by savedTimersViewModel.saveTimeUiState.collectAsStateWithLifecycle()
                        SavedTimerScreen(
                            navigateToRunningScreen = ::navigateToRunningScreen,
                            saveTimeUiState = saveTimeUiState,
                            updateSavedTimeList = savedTimersViewModel::updateSavedTimeList,
                            deleteSavedTimer = savedTimersViewModel::deleteSavedTimer,
                        )
                    }

                    entry<AICoachScreenNavKey> {
                        val aiCoachViewModel: AiCoachViewModel = hiltViewModel()
                        val aiCoachUiState by aiCoachViewModel.aiCoachUiState.collectAsStateWithLifecycle()
                        AiCoachScreen(
                            aiCoachUiState = aiCoachUiState,
                            onIntent = aiCoachViewModel::onIntent
                        )
                    }

                    entry<SettingsScreenNavKey> {
                        val settingsViewModel: SettingsViewModel = hiltViewModel()
                        val settingsUiState by settingsViewModel.settingsUiState.collectAsStateWithLifecycle()
                        SettingsScreen(
                            settingsUiState = settingsUiState,
                            onToggleClicks = settingsViewModel::toggleAction
                        )
                    }
                }
            )
        }
    }
}