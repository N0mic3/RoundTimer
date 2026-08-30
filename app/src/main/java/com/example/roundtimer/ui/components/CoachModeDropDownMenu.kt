package com.example.roundtimer.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.roundtimer.domain.model.CoachMode

@Composable
fun CoachModeDropDownMenu(
    selectedMode: CoachMode,
    onclick: (CoachMode) -> Unit
) {
    var isExpand by remember {
        mutableStateOf(false)
    }
    Box {
        CompositionLocalProvider(
            LocalRippleConfiguration provides null,
        ) {
            TextButton(
                onClick = {
                    isExpand = true
                }
            ) {
                Text(
                    text = when (selectedMode) {
                        CoachMode.CLOUD -> "Cloud"
                        CoachMode.ON_DEVICE -> "On-device"
                    },
                )
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Choose AI response mode",
                )
            }
        }
        DropdownMenu(
            expanded = isExpand,
            onDismissRequest = {
                isExpand = false
            }
        ) {
            DropdownMenuItem(
                text = {
                    Text(
                        text = "Cloud"
                    )
                },
                onClick = {
                    if (selectedMode != CoachMode.CLOUD) {
                        onclick.invoke(CoachMode.CLOUD)
                    }
                    isExpand = false
                }
            )
            DropdownMenuItem(
                text = {
                    Text(
                        text = "On-device"
                    )
                },
                onClick = {
                    if (selectedMode != CoachMode.ON_DEVICE) {
                        onclick.invoke(CoachMode.ON_DEVICE)
                    }
                    isExpand = false
                }
            )
        }
    }
}