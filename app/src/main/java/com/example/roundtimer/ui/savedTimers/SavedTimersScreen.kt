package com.example.roundtimer.ui.savedTimers

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
    var timerName by rememberSaveable {
        mutableStateOf("")
    }
    var showDeleteDialog by rememberSaveable {
        mutableStateOf(false)
    }
    var showUpdateDialog by rememberSaveable {
        mutableStateOf(false)
    }
    var currentPosition by rememberSaveable {
        mutableStateOf<Int?>(null)
    }
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
            itemsIndexed(
                items = saveTimeUiState.value.savedTimeList,
                key = { _, timer -> timer.id }
            ) { index, timer ->
                TimerRow(
                    timer = timer,
                    startOnclick = {
                        navigateToRunningScreen.invoke(
                            RoundInfoModel(
                                workDuration = timer.timeSettings.workDuration,
                                restDuration = timer.timeSettings.restDuration,
                                roundCount = timer.timeSettings.roundCount
                            )
                        )
                    },
                    updateOnclick = {
                        currentPosition = index
                        timerName = timer.name
                        showUpdateDialog = true
                    },
                    deleteOnclick = {
                        currentPosition = index
                        showDeleteDialog = true
                    },
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
    currentPosition?.let { index ->
        saveTimeUiState.value.savedTimeList.getOrNull(index)
    }?.let { timer ->
        if (showUpdateDialog) {
            AlertDialog(
                title = {
                    Text(
                        "Please enter a new name:"
                    )
                },
                text = {
                    OutlinedTextField(
                        value = timerName,
                        onValueChange = {
                            timerName = it
                        }
                    )
                },
                onDismissRequest = {
                    currentPosition = null
                    showUpdateDialog = false
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            currentPosition = null
                            showUpdateDialog = false
                        },
                    ) {
                        Text("Cancel")
                    }
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            savedTimersViewModel.updateSavedTimeList(
                                savedTimer = timer.copy(
                                    name = timerName.trim()
                                )
                            )
                            currentPosition = null
                            showUpdateDialog = false
                        },
                        enabled = timerName.trim().isNotBlank()
                    ) {
                        Text("Save")
                    }
                }
            )
        }
        if (showDeleteDialog) {
            AlertDialog(
                title = {
                    Text(
                        "Confirming Deletion?"
                    )
                },
                onDismissRequest = {
                    currentPosition = null
                    showDeleteDialog = false
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            currentPosition = null
                            showDeleteDialog = false
                        },
                    ) {
                        Text("Cancel")
                    }
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            savedTimersViewModel.deleteSavedTimeList(
                                savedTimer = timer
                            )
                            currentPosition = null
                            showDeleteDialog = false
                        },
                    ) {
                        Text("Confirm")
                    }
                }
            )
        }
    }
}

@Composable
private fun TimerRow(
    timer: SavedTimer,
    startOnclick: () -> Unit,
    updateOnclick: () -> Unit,
    deleteOnclick: () -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .weight(1f)
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
            Column(
                modifier = Modifier.wrapContentSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = startOnclick
                ) {
                    Text("Start")
                }
                Button(
                    onClick = updateOnclick
                ) {
                    Text("Update")
                }
                Button(
                    onClick = deleteOnclick
                ) {
                    Text("Delete")
                }
            }
        }
    }
}