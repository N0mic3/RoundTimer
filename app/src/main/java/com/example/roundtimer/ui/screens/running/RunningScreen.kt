package com.example.roundtimer.ui.screens.running

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
import com.example.roundtimer.domain.model.TimerPhase
import com.example.roundtimer.ui.navigation.RoundInfoModel
import com.example.roundtimer.utils.Utils.formatTime

@Composable
fun RunningScreen(
    roundInfoModel: RoundInfoModel,
    timerUiState: TimerUiState,
    onMainButtonClick: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.padding(
                bottom = 20.dp
            ),
            text = when (timerUiState.timeState.phase) {
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
            text = "Round ${timerUiState.timeState.currentRoundIndex + 1} / ${roundInfoModel.roundCount}"
        )
        Text(
            fontSize = 20.sp,
            text = formatTime(timerUiState.timeState.secondsLeft)
        )
        Button(
            onClick = onMainButtonClick
        ) {
            Text(
                text = when {
                    timerUiState.timeState.phase == TimerPhase.Complete -> "Reset"
                    timerUiState.timeState.isRunning -> "Pause"
                    else -> "Resume"
                }
            )
        }
    }
}