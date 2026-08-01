package com.example.roundtimer.ui.running

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.roundtimer.domain.model.TimerPhase
import com.example.roundtimer.ui.navigation.RoundInfoModel
import com.example.roundtimer.utils.Utils.formatTime

@Composable
fun RunningScreen(
    roundInfoModel: RoundInfoModel,
    onBackClick: () -> Unit,
    runningViewModel: RunningViewModel
) {
    val timerUiState = runningViewModel.timerUiState.collectAsStateWithLifecycle()
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.padding(
                bottom = 20.dp
            ),
            text = when (timerUiState.value.timeState.phase) {
                TimerPhase.Ready -> "Get Ready"
                TimerPhase.Work -> "Work"
                TimerPhase.Rest -> "Rest"
                TimerPhase.Complete -> "Done"
            },
            fontSize = 40.sp
        )
        Text(
            modifier = Modifier.padding(
                bottom = 12.dp
            ),
            fontSize = 20.sp,
            text = "Round ${timerUiState.value.timeState.currentRoundIndex + 1} / ${roundInfoModel.roundCount}"
        )
        Text(
            fontSize = 20.sp,
            text = formatTime(timerUiState.value.timeState.secondsLeft)
        )
        Button(
            onClick = {
                runningViewModel.onMainButtonClick()
            }
        ) {
            Text(
                text = when {
                    timerUiState.value.timeState.phase == TimerPhase.Complete -> "Reset"
                    timerUiState.value.timeState.isRunning -> "Pause"
                    else -> "Resume"
                }
            )
        }
        Button(
            onClick = onBackClick
        ) {
            Text(
                text = "Back"
            )
        }
    }
}