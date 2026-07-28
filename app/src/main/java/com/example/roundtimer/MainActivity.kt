package com.example.roundtimer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.example.roundtimer.model.RunningScreenNavKey
import com.example.roundtimer.model.StartScreenNavKey
import com.example.roundtimer.ui.screens.RunningScreen
import com.example.roundtimer.ui.screens.StartScreen
import com.example.roundtimer.ui.theme.RoundTimerTheme
import com.example.roundtimer.ui.viewmodel.RunningViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RoundTimerTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        AppNavigation()
                    }
                }
            }
        }
    }
}

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
                StartScreen(
                    onClick = {
                        backStack.add(RunningScreenNavKey(
                            roundInfoModel = it
                        ))
                    }
                )
            }
            entry<RunningScreenNavKey> {
                val runningViewModelFactory = remember {
                    RunningViewModel.factory(it.roundInfoModel)
                }
                val runningViewModel: RunningViewModel = viewModel(
                    factory = runningViewModelFactory
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
