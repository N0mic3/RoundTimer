package com.example.roundtimer.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign

@Composable
fun DurationPicker(
    modifier: Modifier = Modifier,
    title: String,
    startValue: Int,
    endValue: Int,
    currentValue: Int,
    step: Int,
    units: String,
    onClick: (Int) -> Unit,
) {
    var showPicker by rememberSaveable { mutableStateOf(false) }
    Row(
        modifier = modifier.testTag("duration_picker"),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier
                .weight(1f),
            text = title,
            textAlign = TextAlign.End
        )
        Box(
            modifier = Modifier
                .weight(1f)
        ) {
            OutlinedButton(
                onClick = {
                    showPicker = true
                },
            ) {
                Text("$currentValue $units")
            }
            DropdownMenu(
                expanded = showPicker,
                onDismissRequest = { showPicker = false }
            ) {
                for (selectedValue in startValue..endValue step step) {
                    DropdownMenuItem(
                        text = {
                            Text("$selectedValue $units")
                        },
                        onClick = {
                            showPicker = false
                            onClick.invoke(selectedValue)
                        }
                    )
                }
            }
        }
    }
}