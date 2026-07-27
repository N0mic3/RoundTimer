package com.example.roundtimer.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.roundtimer.model.RoundInfoModel
import com.example.roundtimer.utils.formatTime
import kotlinx.coroutines.delay

enum class Phase(val message: String) {
    Ready("Get Ready"),
    Work("Work"),
    Rest("Rest"),
    Complete("Done")
}

@Composable
fun RunningScreen(
    roundInfoModel: RoundInfoModel,
    onBackClick: () -> Unit,
) {
    var currentPhase by rememberSaveable {
        mutableStateOf(Phase.Ready)
    }
    var currentSectionLeft by rememberSaveable {
        mutableIntStateOf(5)
    }
    var isRunning by rememberSaveable {
        mutableStateOf(true)
    }
    var currentRoundIndex by rememberSaveable{
        mutableIntStateOf(0)
    }
    LaunchedEffect(isRunning, currentSectionLeft) {
        if (isRunning && currentSectionLeft > 0) {
            delay(1000)
            currentSectionLeft -= 1
            when(currentPhase) {
                Phase.Ready -> {
                    if (currentSectionLeft == 0) {
                        currentSectionLeft = roundInfoModel.workDuration
                        currentPhase = Phase.Work
                    }
                }
                Phase.Work -> {
                    if (currentSectionLeft == 0) {
                        if (currentRoundIndex + 1 == roundInfoModel.roundCount) {
                            currentPhase = Phase.Complete
                            isRunning = false
                        } else {
                            currentRoundIndex += 1
                            currentSectionLeft = roundInfoModel.restDuration
                            currentPhase = Phase.Rest
                        }
                    }
                }
                Phase.Rest ->  {
                    if (currentSectionLeft == 0) {
                        currentSectionLeft = roundInfoModel.workDuration
                        currentPhase = Phase.Work
                    }
                }
                else -> {}
            }
        }
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.padding(
                bottom = 20.dp
            ),
            text = currentPhase.message,
            fontSize = 40.sp
        )
        Text(
            modifier = Modifier.padding(
                bottom = 12.dp
            ),
            fontSize = 20.sp,
            text = "Round ${currentRoundIndex + 1} / ${roundInfoModel.roundCount}"
        )
        Text(
            fontSize = 20.sp,
            text = formatTime(currentSectionLeft)
        )
        Button(
            onClick = {
                when {
                    currentPhase == Phase.Complete -> {
                        currentSectionLeft = 5
                        currentPhase = Phase.Ready
                        isRunning = true
                        currentRoundIndex = 0
                    }
                    else -> {
                        isRunning = isRunning.not()
                    }
                }
            }
        ) {
            Text(
                text = when {
                    currentPhase == Phase.Complete -> "Reset"
                    isRunning -> "Pause"
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