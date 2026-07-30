package com.example.roundtimer.ui.start

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.roundtimer.ui.components.DurationPicker
import com.example.roundtimer.ui.navigation.RoundInfoModel

@Composable
fun StartScreen(
    modifier: Modifier = Modifier,
    navigateToRunningScreen : (RoundInfoModel) -> Unit,
    navigateToSavedTimersScreen : () -> Unit,
    startViewModel: StartViewModel,
) {
    var showSaveDialog by rememberSaveable {
        mutableStateOf(false)
    }
    var timerName by rememberSaveable {
        mutableStateOf("")
    }
    val quoteUiState = startViewModel.stateUiState.collectAsStateWithLifecycle()
    var workDuration by rememberSaveable {
        mutableIntStateOf(5)
    }
    var restDuration by rememberSaveable {
        mutableIntStateOf(5)
    }
    var roundCount by rememberSaveable {
        mutableIntStateOf(1)
    }
    val roundInfoModel = RoundInfoModel(
        workDuration = workDuration,
        restDuration = restDuration,
        roundCount = roundCount
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
            startValue = workDuration,
            endValue = 60,
            step = 5,
            units = "Seconds",
            onClick = {
                workDuration = it
            }
        )
        DurationPicker(
            title = "Rest Duration: ",
            startValue = restDuration,
            endValue = 60,
            step = 5,
            units = "Seconds",
            onClick = {
                restDuration = it
            }
        )
        DurationPicker(
            title = "Rounds: ",
            startValue = roundCount,
            endValue = 20,
            step = 1,
            units = "Rounds",
            onClick = {
                roundCount = it
            }
        )
        Button(
            modifier = Modifier.padding(
                top = 12.dp
            ),
            onClick = {
                navigateToRunningScreen.invoke(
                    roundInfoModel
                )
            }
        ) {
            Text("Start")
        }
        Button(
            modifier = Modifier.padding(
                top = 12.dp
            ),
            onClick = {
                showSaveDialog = true
            }
        ) {
            Text("Save")
        }
        Button(
            modifier = Modifier.padding(
                top = 12.dp
            ),
            onClick = navigateToSavedTimersScreen
        ) {
            Text("Saved List")
        }
    }
    if (showSaveDialog) {
        AlertDialog(
            onDismissRequest = {
                showSaveDialog = false
            },
            title = {
                Text(
                    text = "Save Timer"
                )
            },
            text = {
                OutlinedTextField(
                    value = timerName,
                    onValueChange = {
                        timerName = it
                    },
                    label = {
                        Text(
                            text = "Choose a name for this timer preset."
                        )
                    }
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        startViewModel.insertSavedTimer(
                            name = timerName.trim(),
                            timeSettings = roundInfoModel.toTimeSettings()
                        )
                        timerName = ""
                        showSaveDialog = false
                    },
                    enabled = timerName.trim().isNotBlank()
                ) {
                    Text(
                        text = "Save"
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showSaveDialog = false
                    }
                ) {
                    Text(
                        text = "Cancel"
                    )
                }
            }
        )
    }
}