package com.example.friendly_words.therapist.ui.configuration.learning


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.friendly_words.therapist.ui.components.NumberSelector
import com.example.friendly_words.therapist.ui.theme.DarkBlue
import com.example.friendly_words.therapist.ui.theme.White

@Composable
fun ConfigurationLearningScreen(
    state:ConfigurationLearningState,
    onEvent:(ConfigurationLearningEvent) -> Unit,
    onBackClick: () -> Unit
){
    //val state by viewModel.state.collectAsState()

    var expanded by remember { mutableStateOf(false) }
    var textFieldSize by remember { mutableStateOf(IntSize.Zero) }
    val options = listOf("{Słowo}", "Gdzie jest {Słowo}", "Pokaż gdzie jest {Słowo}")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color= White
            )
    ){
        Row(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(16.dp),
                contentAlignment = Alignment.TopCenter
            ) { Column(
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Text(
                    text = "Ustawienia próby",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkBlue,
                    textAlign = TextAlign.Center
                )

                NumberSelector(
                    label = "Liczba obrazków wyświetlanych na ekranie:",
                    minValue = 1,
                    maxValue = 6,
                    value = state.imageCount,
                    onValueChange = { onEvent(ConfigurationLearningEvent.SetImageCount(it)) }
                )

                Spacer(modifier = Modifier.height(24.dp))

                NumberSelector(
                    label = "Liczba powtórzeń dla każdego słowa:",
                    minValue = 1,
                    maxValue = 3,
                    value = state.repetitionCount,
                    onValueChange = { onEvent(ConfigurationLearningEvent.SetRepetitionCount(it)) }
                )

                Column {
                    Text(
                        text = "Rodzaj polecenia:",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black
                    )

                    Box(
                        modifier = Modifier
                            .onGloballyPositioned { coordinates ->
                                textFieldSize = coordinates.size
                            }
                            .clickable { expanded = !expanded }
                    ) {
                        OutlinedTextField(
                            value = state.selectedPrompt,
                            onValueChange = {},
                            readOnly = true,
                            enabled = false,
                            textStyle = LocalTextStyle.current.copy(fontSize = 18.sp),
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Filled.ArrowDropDown,
                                    contentDescription = "Rozwiń",
                                    tint = DarkBlue
                                )
                            },
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                disabledTextColor = Color.Black,
                                disabledBorderColor = DarkBlue,
                                disabledLabelColor = DarkBlue,
                                disabledTrailingIconColor = DarkBlue
                            ),
                            modifier = Modifier
                        )

                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            modifier = Modifier

                        ) {
                            options.forEach { selectionOption ->
                                DropdownMenuItem(
                                    onClick = {
                                        onEvent(ConfigurationLearningEvent.SetPrompt(selectionOption))
                                        expanded = false
                                    }
                                ) {
                                    Text(
                                        text = selectionOption,
                                        fontSize = 20.sp
                                    )
                                }
                            }
                        }
                    }
                }

            }

            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(16.dp),
                contentAlignment = Alignment.TopCenter
            ) { Column(
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Text(
                    text = "Ustawienia kroku",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkBlue,
                    textAlign = TextAlign.Center
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .padding(top = 16.dp)
                ) {
                    Text(
                        text = "Podpisy pod obrazkami",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black
                    )

                    Switch(
                        checked = state.captionsEnabled,
                        onCheckedChange = { onEvent(ConfigurationLearningEvent.ToggleCaptions(it)) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = DarkBlue,
                            checkedTrackColor = DarkBlue.copy(alpha = 0.5f),
                            uncheckedThumbColor = Color.LightGray,
                            uncheckedTrackColor = Color.Gray
                        )
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                ) {
                    Text(
                        text = "Czytanie polecenia",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black
                    )

                    Switch(
                        checked = state.readingEnabled,
                        onCheckedChange = {
                            onEvent(ConfigurationLearningEvent.ToggleReading(it))
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = DarkBlue,
                            checkedTrackColor = DarkBlue.copy(alpha = 0.5f),
                            uncheckedThumbColor = Color.LightGray,
                            uncheckedTrackColor = Color.Gray
                        )
                    )
                }

                NumberSelector(
                    label = "Pokaż podpowiedź po (sekundach):",
                    minValue = 3,
                    maxValue = 9,
                    value = state.timeCount,
                    onValueChange = { onEvent(ConfigurationLearningEvent.SetTimeCount(it)) }
                )

                Text(
                    text = "Wybierz rodzaj podpowiedzi:",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = state.outlineCorrect,
                            onCheckedChange = { newValue ->
                                onEvent(ConfigurationLearningEvent.ToggleOutlineCorrect(newValue))
                            },
                            colors = CheckboxDefaults.colors(checkedColor = DarkBlue)
                        )
                        Text("Obramuj poprawną", fontSize = 18.sp)
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = state.animateCorrect,
                            onCheckedChange = { newValue ->
                                onEvent(ConfigurationLearningEvent.ToggleAnimateCorrect(newValue))
                            },
                            colors = CheckboxDefaults.colors(checkedColor = DarkBlue)
                        )
                        Text("Porusz poprawną", fontSize = 18.sp)
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = state.scaleCorrect,
                            onCheckedChange = { newValue ->
                                onEvent(ConfigurationLearningEvent.ToggleScaleCorrect(newValue))
                            },
                            colors = CheckboxDefaults.colors(checkedColor = DarkBlue)
                        )
                        Text("Powiększ poprawną", fontSize = 18.sp)
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = state.dimIncorrect,
                            onCheckedChange = { newValue ->
                                onEvent(ConfigurationLearningEvent.ToggleDimIncorrect(newValue))
                            },
                            colors = CheckboxDefaults.colors(checkedColor = DarkBlue)
                        )
                        Text("Wyszarz niepoprawne", fontSize = 18.sp)
                    }
                }
            }
            }
        }
    }
}