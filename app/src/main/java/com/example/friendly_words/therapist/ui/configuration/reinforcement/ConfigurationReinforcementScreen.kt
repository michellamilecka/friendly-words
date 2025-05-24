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

        val praiseWords = listOf("dobrze", "super", "świetnie", "ekstra", "rewelacja", "brawo")
        val praiseStates = remember { praiseWords.map { mutableStateOf(true) } }

        val chunks = praiseWords.chunked(3)
        val stateChunks = praiseStates.chunked(3)

        chunks.forEachIndexed { rowIndex, rowLabels ->
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                rowLabels.forEachIndexed { index, label ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = stateChunks[rowIndex][index].value,
                            onCheckedChange = { stateChunks[rowIndex][index].value = it },
                            colors = CheckboxDefaults.colors(
                                checkedColor = DarkBlue,
                                uncheckedColor = Color.Gray,
                                checkmarkColor = Color.White
                            )
                        )
                        Text(
                            text = label,
                            fontSize = 24.sp,
                            color = Color.Black
                        )
                    }
                }
            }
        }
        var praiseReadingEnabled by remember { mutableStateOf(true) }

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
                checked = praiseReadingEnabled,
                onCheckedChange = { praiseReadingEnabled = it },
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



