package com.example.roundtimer.ui.start

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.roundtimer.ui.navigation.RoundInfoModel
import com.example.roundtimer.ui.components.DurationPicker
import com.example.roundtimer.ui.start.StartViewModel

@Composable
fun StartScreen(
    modifier: Modifier = Modifier,
    onClick : (RoundInfoModel) -> Unit,
    startViewModel: StartViewModel,
) {
    val quoteUiState = startViewModel.stateUiState.collectAsStateWithLifecycle()
    var roundInfoModel = RoundInfoModel(
        workDuration = 5,
        restDuration = 5,
        roundCount = 1
    )
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            modifier = Modifier.padding(
                bottom = 6.dp
            ),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            fontSize = 16.sp,
            text = "Quote For the Day"
        )
        Text(
            modifier = modifier.padding(
                bottom = 12.dp
            ),
            textAlign = TextAlign.Center,
            text = quoteUiState.value.data
        )
        DurationPicker(
            title = "Work Duration: ",
            startValue = 5,
            endValue = 60,
            step = 5,
            units = "Seconds",
            onClick = {
                roundInfoModel = roundInfoModel.copy(
                    workDuration = it
                )
            }
        )
        DurationPicker(
            title = "Rest Duration: ",
            startValue = 5,
            endValue = 60,
            step = 5,
            units = "Seconds",
            onClick = {
                roundInfoModel = roundInfoModel.copy(
                    restDuration = it
                )
            }
        )
        DurationPicker(
            title = "Rounds: ",
            startValue = 1,
            endValue = 10,
            step = 1,
            units = "Rounds",
            onClick = {
                roundInfoModel = roundInfoModel.copy(
                    roundCount = it
                )
            }
        )
        Button(
            modifier = Modifier.padding(
                top = 12.dp
            ),
            onClick = {
                onClick.invoke(
                    roundInfoModel
                )
            }
        ) {
            Text("Start")
        }
    }
}