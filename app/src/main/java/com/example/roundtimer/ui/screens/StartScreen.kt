package com.example.roundtimer.ui.screens

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
import com.example.roundtimer.model.RoundInfoModel
import com.example.roundtimer.ui.components.DurationPicker

@Composable
fun StartScreen(
    modifier: Modifier = Modifier,
    onClick : (RoundInfoModel) -> Unit,
) {
    val roundInfoModel = RoundInfoModel(
        workDuration = 5,
        restDuration = 5,
        roundCount = 1
    )
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        DurationPicker(
            title = "Work Duration: ",
            startValue = 5,
            endValue = 60,
            step = 5,
            units = "Seconds",
            onClick = {
                roundInfoModel.workDuration = it
            }
        )
        DurationPicker(
            title = "Rest Duration: ",
            startValue = 5,
            endValue = 60,
            step = 5,
            units = "Seconds",
            onClick = {
                roundInfoModel.restDuration = it
            }
        )
        DurationPicker(
            title = "Rounds: ",
            startValue = 1,
            endValue = 10,
            step = 1,
            units = "Rounds",
            onClick = {
                roundInfoModel.roundCount = it
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