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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.friendly_words.therapist.ui.components.NumberSelector
import com.example.friendly_words.therapist.ui.components.NumberSelectorForPictures
import com.example.friendly_words.therapist.ui.theme.DarkBlue
import com.example.friendly_words.therapist.ui.theme.White
import com.example.shared.data.another.ConfigurationTestState

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ConfigurationTestScreen(
    state: ConfigurationTestState,
    availableImagesForTest: Int,      // realna dostępność testu
    availableImagesForLearning: Int,  // realna dostępność uczenia
    learningImageCount: Int,          // ustawiona liczba w uczeniu
    onEvent: (ConfigurationTestEvent) -> Unit,
    onBackClick: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val options = listOf("{Słowo}", "Gdzie jest {Słowo}", "Pokaż gdzie jest {Słowo}")
    val labelColor = if (state.testEditEnabled) Color.Black else Color.Gray

    val learningAvailable = availableImagesForLearning.coerceAtLeast(0)
    val testAvailableRaw = availableImagesForTest.coerceAtLeast(0)

    // ⬇️ KLUCZ: jeśli edycja testu jest WYŁĄCZONA i w uczeniu 0 -> blokujemy na 0.
    //           jeśli edycja testu jest WŁĄCZONA -> używamy realnej dostępności testu.
    val effectiveAvailable =
        if (!state.testEditEnabled && learningAvailable == 0) 0 else testAvailableRaw

    val minAllowed = if (effectiveAvailable == 0) 0 else 1
    val maxAllowed = effectiveAvailable
    val defaultImageCount = when {
        effectiveAvailable == 0 -> 0
        effectiveAvailable < 3 -> effectiveAvailable
        else -> 3
    }

    // UI zawsze pokazuje wartość w bieżącym zakresie testu
    val clampedValueForUI =
        if (effectiveAvailable == 0) 0 else state.imageCount.coerceIn(1, maxAllowed)

    var dialogMessage by remember { mutableStateOf<String?>(null) }

    // Synchronizacja przy zmianie: dostępności, dziedziczenia z uczenia i przełączania edycji
    LaunchedEffect(effectiveAvailable, learningImageCount, state.testEditEnabled) {
        if (!state.testEditEnabled) {
            // Dziedziczenie z UCZENIA
            if (learningAvailable == 0) {
                // UCZENIE=0 -> TEST=0
                if (state.imageCount != 0) onEvent(ConfigurationTestEvent.SetImageCount(0))
            } else {
                // UCZENIE>0: przejmij wartość z uczenia, ale ogranicz do dostępności testu
                val inherited = when {
                    maxAllowed == 0 -> 0
                    else -> learningImageCount.coerceIn(1, maxAllowed)
                }
                if (state.imageCount != inherited) {
                    // Jeśli uczenie > max testu -> przytnij + pokaż info
                    if (learningImageCount > maxAllowed && maxAllowed > 0) {
                        dialogMessage = "W uczeniu wybrano $learningImageCount, ale w teście dostępnych jest tylko $maxAllowed. Ustawiono maksymalną liczbę dla testu."
                    }
                    onEvent(ConfigurationTestEvent.SetImageCount(inherited))
                }
            }
        } else {
            // Edycja testu WŁĄCZONA – pracujemy w realnym zakresie testu
            if (maxAllowed == 0) {
                // brak dostępnych w teście: trzymaj 0
                if (state.imageCount != 0) onEvent(ConfigurationTestEvent.SetImageCount(0))
            } else {
                // upewnij się, że jest 1..maxAllowed
                if (state.imageCount < 1) onEvent(ConfigurationTestEvent.SetImageCount(1))
                if (state.imageCount > maxAllowed) {
                    onEvent(ConfigurationTestEvent.SetImageCount(maxAllowed))
                    dialogMessage = "Wybrano więcej obrazków niż dostępne. Ustawiono maksymalną liczbę: $maxAllowed."
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
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
                    NumberSelectorForPictures(
                        label = "Liczba obrazków wyświetlanych na ekranie:",
                        minValue = minAllowed,
                        maxValue = maxAllowed,
                        value = clampedValueForUI,
                        enabled = state.testEditEnabled && effectiveAvailable > 0, // blokada przycisków
                        labelEnabled = state.testEditEnabled,                      // 🔸 styl zawsze wg checkboxa
                        labelColor = Color.Black,
                        onValueChange = { newValue ->
                            val clamped = if (effectiveAvailable == 0) 0 else newValue.coerceIn(1, maxAllowed)
                            if (clamped != state.imageCount) {
                                onEvent(ConfigurationTestEvent.SetImageCount(clamped))
                            }
                        },
                        onDisabledDecrementClick = {
                            if (effectiveAvailable > 0 && state.testEditEnabled) {
                                dialogMessage = "Nie możesz ustawić mniej niż $minAllowed."
                            }
                        },
                        onDisabledIncrementClick = {
                            if (effectiveAvailable > 0 && state.testEditEnabled) {
                                dialogMessage = "Nie możesz ustawić więcej niż $maxAllowed – tyle masz dostępnych obrazków w teście."
                            }
                        }
                    )

                    // Komunikat przy 0 (gdy faktycznie brak w bieżącym trybie)
                    if (effectiveAvailable == 0 || clampedValueForUI == 0) {
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = "Dodaj materiały edukacyjne w zakładce „Materiał”, aby zwiększyć liczbę obrazków.",
                            fontSize = 12.sp,
                            color = Color.Red,
                            fontStyle = FontStyle.Italic,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    NumberSelector(
                        label = "Liczba powtórzeń dla każdego słowa:",
                        minValue = 1,
                        maxValue = 5,
                        value = state.repetitionCount,
                        onValueChange = {
                            if (state.testEditEnabled) {
                                onEvent(ConfigurationTestEvent.SetRepetitionCount(it))
                            }
                        },
                        enabled = state.testEditEnabled,
                        labelColor = labelColor
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Rodzaj polecenia:",
                            fontSize = 20.sp,
                            fontWeight = if (state.testEditEnabled) FontWeight.Medium else FontWeight.Normal,
                            color = labelColor
                        )
                        Spacer(modifier = Modifier.height(25.dp))
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
                            text = "Jeśli dziecko nie wybierze odpowiedzi, ekran zmieni się automatycznie po czasie ustawionym w sekcji UCZENIE – Pokaż podpowiedź po (sekundach).",
                            fontSize = 16.sp,
                            color = Color.Black,
                            lineHeight = 22.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.width(32.dp))

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
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
                            fontWeight = if (state.testEditEnabled) FontWeight.Medium else FontWeight.Normal,
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
                            fontWeight = if (state.testEditEnabled) FontWeight.Medium else FontWeight.Normal,
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
                            text =  "W trybie testu nie używa się podpowiedzi i wzmocnień, a terapeuta nie pomaga i nie rozmawia z dzieckiem, aż do zakończenia testu.",
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

    // Popup tylko, gdy mamy dodatnią dostępność (w przeciwnym razie i tak jest 0)
    if (effectiveAvailable > 0) {
        dialogMessage?.let { msg ->
            AlertDialog(
                onDismissRequest = { dialogMessage = null },
                title = { Text("Informacja") },
                text = { Text(msg) },
                confirmButton = {
                    TextButton(onClick = { dialogMessage = null }) {
                        Text("OK", color = DarkBlue, fontWeight = FontWeight.Bold)
                    }
                }
            )
        }
    }
}
