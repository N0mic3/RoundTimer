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

@Composable
fun DurationPicker(
    modifier: Modifier = Modifier,
    title: String,
    startValue: Int,
    endValue: Int,
    step: Int,
    units: String,
    onClick: (Int) -> Unit,
) {
    var value by rememberSaveable { mutableIntStateOf(startValue) }
    var showPicker by rememberSaveable { mutableStateOf(false) }
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title
        )
        Box {
            OutlinedButton(
                onClick = {
                    showPicker = true
                },
            ) {
                Text("$value $units")
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
                            value = selectedValue
                            showPicker = false
                            onClick.invoke(selectedValue)
                        }
                    )
                }
            }
        }
    }
}