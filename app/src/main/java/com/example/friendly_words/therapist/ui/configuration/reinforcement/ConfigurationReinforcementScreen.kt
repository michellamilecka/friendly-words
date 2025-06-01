package com.example.friendly_words.therapist.ui.configuration.reinforcement

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.friendly_words.therapist.ui.theme.DarkBlue


@Composable
fun ConfigurationReinforcementScreen(
    state: ConfigurationReinforcementState,
    onEvent: (ConfigurationReinforcementEvent) -> Unit,
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Pochwały słowne:",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = DarkBlue,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        val praiseWords = state.praiseStates.keys.toList().chunked(3)

        praiseWords.forEach { row ->
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                row.forEach{ word ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = state.praiseStates[word] == true,
                            onCheckedChange = { onEvent(ConfigurationReinforcementEvent.TogglePraise(word, it)) },
                            colors = CheckboxDefaults.colors(
                                checkedColor = DarkBlue,
                                uncheckedColor = Color.Gray,
                                checkmarkColor = Color.White
                            )
                        )
                        Text(
                            text = word,
                            fontSize = 24.sp,
                            color = Color.Black
                        )
                    }
                }
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(top = 16.dp)
        ) {
            Text(
                text = "Czytanie głosowe pochwał",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )

            Switch(
                checked = state.praiseReadingEnabled,
                onCheckedChange = { onEvent(ConfigurationReinforcementEvent.ToggleReading(it)) },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = DarkBlue,
                    checkedTrackColor = DarkBlue.copy(alpha = 0.5f),
                    uncheckedThumbColor = Color.LightGray,
                    uncheckedTrackColor = Color.Gray
                )
            )
        }
    }
}



