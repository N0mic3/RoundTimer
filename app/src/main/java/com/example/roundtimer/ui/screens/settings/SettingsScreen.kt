package com.example.roundtimer.ui.screens.settings

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SettingsScreen(
    settingsUiState : SettingsUiState,
    onToggleClicks: (SettingsItemType, Boolean) -> Unit
) {
    LazyColumn(
        modifier = Modifier.padding(
            horizontal = 12.dp
        )
    ) {
        items(
            items = settingsUiState.settingsUiItems,
            key = { it.settingType }
        ) { item ->
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.weight(
                        1f
                    ),
                    text = item.settingType.displayName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Switch(
                    modifier = Modifier.testTag(
                        "${item.settingType.name}_switch"
                    ),
                    checked = item.activeState,
                    onCheckedChange = { isEnabled ->
                        onToggleClicks(item.settingType, isEnabled)
                    },
                )
            }
        }
    }
}