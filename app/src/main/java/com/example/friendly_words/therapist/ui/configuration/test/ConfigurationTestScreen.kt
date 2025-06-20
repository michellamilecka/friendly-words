package com.example.friendly_words.therapist.ui.configuration.test

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.friendly_words.therapist.ui.components.NumberSelector
import com.example.friendly_words.therapist.ui.configuration.learning.ConfigurationLearningState
import com.example.friendly_words.therapist.ui.theme.DarkBlue
import com.example.friendly_words.therapist.ui.theme.White

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ConfigurationTestScreen(
    state: ConfigurationTestState,
    onEvent: (ConfigurationTestEvent) -> Unit,
    onBackClick: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val options = listOf("{Słowo}", "Gdzie jest {Słowo}", "Pokaż gdzie jest {Słowo}")
    val labelColor = if (state.testEditEnabled) Color.Black else Color.Gray

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(color = White)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Checkbox(
                checked = state.testEditEnabled,
                onCheckedChange = { onEvent(ConfigurationTestEvent.ToggleTestEdit) },
                colors = CheckboxDefaults.colors(checkedColor = DarkBlue)
            )
            Text("Zmień dla testu", fontSize = 18.sp)
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                NumberSelector(
                    label = "Liczba obrazków wyświetlanych na ekranie:",
                    minValue = 1,
                    maxValue = 6,
                    value = state.imageCount,
                    onValueChange = {
                        if (state.testEditEnabled) {
                            onEvent(ConfigurationTestEvent.SetImageCount(it))
                        }
                    },
                    enabled = state.testEditEnabled,
                    labelColor = labelColor
                )

                Spacer(modifier = Modifier.height(24.dp))

                NumberSelector(
                    label = "Łączna liczba prób w teście:",
                    minValue = 1,
                    maxValue = 5,
                    value = state.attemptsCount,
                    onValueChange = {
                        if (state.testEditEnabled) {
                            onEvent(ConfigurationTestEvent.SetAttemptsCount(it))
                        }
                    },
                    enabled = state.testEditEnabled,
                    labelColor = labelColor
                )

                Spacer(modifier = Modifier.height(24.dp))

                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Rodzaj polecenia:",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium,
                        color = labelColor
                    )

                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = {
                            if (state.testEditEnabled) {
                                expanded = !expanded
                            }
                        }
                    ) {
                        OutlinedTextField(
                            readOnly = true,
                            value = state.selectedPrompt,
                            onValueChange = {},
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                            },
                            modifier = Modifier
                                .fillMaxWidth(),
                            enabled = state.testEditEnabled,
                            textStyle = LocalTextStyle.current.copy(fontSize = 18.sp),
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                textColor = labelColor,
                                focusedBorderColor = DarkBlue,
                                unfocusedBorderColor = DarkBlue,
                                disabledTextColor = Color.Gray,
                                disabledBorderColor = Color.Gray,
                                disabledLabelColor = Color.Gray,
                                disabledTrailingIconColor = Color.Gray
                            )
                        )

                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            options.forEach { selectionOption ->
                                DropdownMenuItem(
                                    onClick = {
                                        onEvent(ConfigurationTestEvent.SetPrompt(selectionOption))
                                        expanded = false
                                    },
                                    enabled = state.testEditEnabled
                                ) {
                                    Text(
                                        text = selectionOption,
                                        fontSize = 18.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.width(32.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                //verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
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
                        color = if (state.testEditEnabled) Color.Black else Color.Gray
                    )

                    Switch(
                        checked = state.captionsEnabled,
                        onCheckedChange = {
                            if (state.testEditEnabled) {
                                onEvent(ConfigurationTestEvent.ToggleCaptions(it))
                            }
                        },
                        enabled = state.testEditEnabled,
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = DarkBlue,
                            checkedTrackColor = DarkBlue.copy(alpha = 0.5f),
                            uncheckedThumbColor = Color.LightGray,
                            uncheckedTrackColor = Color.Gray
                        )
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                ) {
                    Text(
                        text = "Czytanie polecenia",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (state.testEditEnabled) Color.Black else Color.Gray
                    )

                    Switch(
                        checked = state.readingEnabled,
                        onCheckedChange = {
                            if (state.testEditEnabled) {
                                onEvent(ConfigurationTestEvent.ToggleReading(it))
                            }
                        },
                        enabled = state.testEditEnabled,
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = DarkBlue,
                            checkedTrackColor = DarkBlue.copy(alpha = 0.5f),
                            uncheckedThumbColor = Color.LightGray,
                            uncheckedTrackColor = Color.Gray
                        )
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
                Column(
                    modifier = Modifier
                        .widthIn(max = 450.dp)
                        .background(Color(0xFFF0F0F0), shape = RoundedCornerShape(8.dp))
                        .padding(12.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Informacja",
                        tint = DarkBlue,
                        modifier = Modifier
                            .size(36.dp)
                            .padding(bottom = 8.dp)
                    )

                    Text(
                        text = "W trybie testu dziecko pracuje na materiałach w zakładce MATERIAŁ. " +
                                "W trybie testu nie używa się podpowiedzi i wzmocnień, " +
                                "a terapeuta powinien powstrzymać się od interwencji w interakcje dziecka aż do zakończenia testu.",
                        fontSize = 16.sp,
                        color = Color.Black,
                        lineHeight = 22.sp,
                        textAlign = TextAlign.Start
                    )
                }
            }
        }
    }
}
