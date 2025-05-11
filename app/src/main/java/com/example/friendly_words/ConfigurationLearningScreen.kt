package com.example.friendly_words


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
import com.example.friendly_words.ui.components.NumberSelector
import com.example.friendly_words.ui.theme.DarkBlue


@Composable
fun ConfigurationLearningScreen(
    onBackClick: () -> Unit
) {
    var imageCount by remember { mutableStateOf(3) }
    var repetitionCount by remember { mutableStateOf(2) }
    var timeCount by remember { mutableStateOf(3) }

    Column(
        modifier = Modifier
            .fillMaxSize()
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
                    maxValue = 4,
                    initialValue = 3,
                    onValueChange = { newValue -> imageCount = newValue }
                )

                Spacer(modifier = Modifier.height(24.dp))

                NumberSelector(
                    label = "Liczba powtórzeń dla każdego słowa:",
                    minValue = 1,
                    maxValue = 5,
                    initialValue = 2,
                    onValueChange = { newValue -> repetitionCount = newValue }
                )

                var expanded by remember { mutableStateOf(false) }
                var selectedOption by remember { mutableStateOf("{Słowo}") }
                val options = listOf("{Słowo}", "Pokaż {Słowo}", "Pokaż gdzie jest {Słowo}")
                var textFieldSize by remember { mutableStateOf(IntSize.Zero) }


                Column {
                    Text(
                        text = "Rodzaj polecenia:",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .onGloballyPositioned { coordinates ->
                                textFieldSize = coordinates.size
                            }
                            .clickable { expanded = !expanded }
                    ) {
                        OutlinedTextField(
                            value = selectedOption,
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
                                        selectedOption = selectionOption
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

                var captionsEnabled by remember { mutableStateOf(true) }

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
                        checked = captionsEnabled,
                        onCheckedChange = { captionsEnabled = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = DarkBlue,
                            checkedTrackColor = DarkBlue.copy(alpha = 0.5f),
                            uncheckedThumbColor = Color.LightGray,
                            uncheckedTrackColor = Color.Gray
                        )
                    )
                }
                var readingEnabled by remember { mutableStateOf(true) }

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
                        checked = readingEnabled,
                        onCheckedChange = { readingEnabled = it },
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
                    initialValue = 3,
                    onValueChange = { newValue -> timeCount = newValue }
                )

                Text(
                    text = "Wybierz rodzaj podpowiedzi:",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )
                var outlineCorrect by remember { mutableStateOf(false) }
                var animateCorrect by remember { mutableStateOf(false) }
                var scaleCorrect by remember { mutableStateOf(false) }
                var dimIncorrect by remember { mutableStateOf(false) }

                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = outlineCorrect,
                            onCheckedChange = { outlineCorrect = it },
                            colors = CheckboxDefaults.colors(checkedColor = DarkBlue)
                        )
                        Text("Obramuj poprawną", fontSize = 18.sp)
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = animateCorrect,
                            onCheckedChange = { animateCorrect = it },
                            colors = CheckboxDefaults.colors(checkedColor = DarkBlue)
                        )
                        Text("Porusz poprawną", fontSize = 18.sp)
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = scaleCorrect,
                            onCheckedChange = { scaleCorrect = it },
                            colors = CheckboxDefaults.colors(checkedColor = DarkBlue)
                        )
                        Text("Powiększ poprawną", fontSize = 18.sp)
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = dimIncorrect,
                            onCheckedChange = { dimIncorrect = it },
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



