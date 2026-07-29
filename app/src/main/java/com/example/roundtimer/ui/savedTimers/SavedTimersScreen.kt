package com.example.roundtimer.ui.savedTimers

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.roundtimer.domain.model.SavedTimer
import com.example.roundtimer.ui.navigation.RoundInfoModel

@Composable
fun SavedTimerScreen(
    onBackClick: () -> Unit,
    navigateToRunningScreen : (RoundInfoModel) -> Unit,
    savedTimersViewModel: SavedTimersViewModel
) {
    val saveTimeUiState = savedTimersViewModel.saveTimeUiState.collectAsStateWithLifecycle()
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(
                items = saveTimeUiState.value.savedTimeList,
                key = { timer -> timer.id }
            ) { timer ->
                TimerRow(
                    timer = timer,
                    onClick = navigateToRunningScreen
                )
            }
        }
        Button(
            onClick = onBackClick
        ) {
            Text(
                text = "back"
            )
        }
    }
}

@Composable
private fun TimerRow(
    timer: SavedTimer,
    onClick: (RoundInfoModel) -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = {
            onClick.invoke(
                RoundInfoModel(
                    workDuration = timer.timeSettings.workDuration,
                    restDuration = timer.timeSettings.restDuration,
                    roundCount = timer.timeSettings.roundCount
                )
            )
        }
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = timer.name,
            )
            Text(
                text = "Work duration: ${timer.timeSettings.workDuration}",
            )
            Text(
                text = "rest duration: ${timer.timeSettings.restDuration}",
            )
            Text(
                text = "roundCount: ${timer.timeSettings.roundCount}",
            )
        }
    }
}