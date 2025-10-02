package com.example.friendly_words.therapist.ui.configuration.material

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import com.composables.core.VerticalScrollbar
import com.composables.core.rememberScrollAreaState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.composables.core.ScrollArea
import com.composables.core.Thumb
import com.example.friendly_words.therapist.ui.components.YesNoDialogWithName
import com.example.friendly_words.therapist.ui.theme.DarkBlue
import com.example.friendly_words.therapist.ui.theme.LightBlue
import com.example.friendly_words.therapist.ui.theme.White
import com.example.shared.data.another.ConfigurationMaterialState


@Composable
fun ImageSelectionWithCheckbox(
    images: List<String>,
    selectedImages: List<Boolean>,
    onImageSelectionChanged: (List<Boolean>) -> Unit,
    onLearningTestChanged: (index: Int, inLearning: Boolean, inTest: Boolean) -> Unit,
    inLearningStates: List<Boolean>,
    inTestStates: List<Boolean>
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth()
            .background(color = White)
    ) {
        images.chunked(3).forEachIndexed { chunkIndex, chunk ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
                    .background(
                        color = White
                    )
            ) {
                chunk.forEachIndexed { innerIndex, resId ->
                    val index = chunkIndex * 3 + innerIndex
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f)
                    ) {
                        Checkbox(
                            checked = selectedImages[index],
                            onCheckedChange = {
                                val newSelectedImages =
                                    selectedImages.toMutableList()
                                        .also { it[index] = it[index].not() }
                                onImageSelectionChanged(newSelectedImages)

                                // Automatyczne ustawienie inLearning i inTest
                                val checked = newSelectedImages[index]
                                onLearningTestChanged(index, checked, checked)
                            },
                            colors = CheckboxDefaults.colors(
                                checkedColor = DarkBlue,
                                uncheckedColor = Color.Gray,
                                checkmarkColor = Color.White
                            )
                        )
                        Box(modifier = Modifier.height(200.dp).aspectRatio(1f)) {
                            Image(
                                painter = rememberAsyncImagePainter(model = resId), // teraz resId to String = ścieżka do pliku
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(top = 8.dp)
                        ) {
                            Checkbox(
                                checked = inLearningStates[index],
                                //enabled = selectedImages[index],
                                onCheckedChange = { newLearning ->
                                    val currentTest = inTestStates[index]
                                    val shouldSelectImage = newLearning || currentTest

                                    // Update stan obrazka (czy ma być wybrany)
                                    val newSelectedImages = selectedImages.toMutableList()
                                        .also { it[index] = shouldSelectImage }
                                    onImageSelectionChanged(newSelectedImages)

                                    // Update stanu learning i test
                                    onLearningTestChanged(index, newLearning, currentTest)
                                },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = LightBlue,
                                    uncheckedColor = Color.Gray,
                                    checkmarkColor = Color.White
                                )
                            )
                            Spacer(modifier = Modifier.width(35.dp))
                            Checkbox(
                                checked = inTestStates[index],
                                //enabled = selectedImages[index],
                                onCheckedChange = { newTest ->
                                    val currentLearning = inLearningStates[index]
                                    val shouldSelectImage = newTest || currentLearning

                                    // Update stan obrazka
                                    val newSelectedImages = selectedImages.toMutableList()
                                        .also { it[index] = shouldSelectImage }
                                    onImageSelectionChanged(newSelectedImages)

                                    // Update stanu learning i test
                                    onLearningTestChanged(index, currentLearning, newTest)
                                },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = LightBlue,
                                    uncheckedColor = Color.Gray,
                                    checkmarkColor = Color.White
                                )
                            )
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Spacer(modifier = Modifier.width(5.dp))
                            Text("Uczenie")
                            Spacer(modifier = Modifier.width(35.dp))
                            Text("Test")
                            Spacer(modifier = Modifier.width(17.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ConfigurationMaterialScreen(
    state: ConfigurationMaterialState,
    onEvent: (ConfigurationMaterialEvent) -> Unit,
    onBackClick: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    val lazyListState = rememberLazyListState()
    val scrollAreaState = rememberScrollAreaState(lazyListState)
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current


    Column(modifier = Modifier.fillMaxSize()) {
        Row(modifier = Modifier.fillMaxSize().weight(1f)) {
            // Lista słów po lewej
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(DarkBlue.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Column(modifier = Modifier.fillMaxHeight()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        listOf(
                            "SŁOWO",
                            "W UCZENIU",
                            "W TEŚCIE",
                            "USUŃ"
                        ).forEachIndexed { index, label ->
                            Text(
                                label,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Gray,
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(end = if (index == 3) 13.dp else 0.dp),
                                textAlign = when (index) {
                                    0 -> TextAlign.Start
                                    3 -> TextAlign.End
                                    else -> TextAlign.Center
                                }
                            )
                        }
                    }

                    LazyColumn(modifier = Modifier.weight(1f).fillMaxWidth()) {
                        items(state.vocabItems) { item ->
                            val index = state.vocabItems.indexOf(item)
                            val isSelected = state.selectedWordIndex == index

                            val hasLearning = item.selectedImages.zip(item.inLearningStates)
                                .any { it.first && it.second }
                            val hasTest = item.selectedImages.zip(item.inTestStates)
                                .any { it.first && it.second }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(if (isSelected) LightBlue.copy(alpha = 0.3f) else Color.Transparent)
                                    .padding(horizontal = 4.dp, vertical = 8.dp)
                                    .clickable {
                                        onEvent(ConfigurationMaterialEvent.WordSelected(index))
                                    }
                            ) {
                                Box(modifier = Modifier.weight(1f)) {
                                    Text(
                                        item.learnedWord,
                                        fontSize = 30.sp,
                                        fontWeight = FontWeight.Bold,
                                    )
                                }

                                Row(
                                    modifier = Modifier.weight(2f)
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .weight(1f)
                                            .fillMaxHeight(),
                                        horizontalArrangement = Arrangement.Center,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = if (hasLearning) Icons.Default.Check else Icons.Default.Close,
                                            contentDescription = null,
                                            tint = if (hasLearning) Color(0xFF4CAF50) else Color.Red,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }

                                    Row(
                                        modifier = Modifier
                                            .weight(1f)
                                            .fillMaxHeight(),
                                        horizontalArrangement = Arrangement.Center,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = if (hasTest) Icons.Default.Check else Icons.Default.Close,
                                            contentDescription = null,
                                            tint = if (hasTest) Color(0xFF4CAF50) else Color.Red,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }
                                }

                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxHeight()
                                        .fillMaxWidth(),
                                    contentAlignment = Alignment.CenterEnd
                                ) {
                                    IconButton(onClick = {
                                        onEvent(ConfigurationMaterialEvent.WordDeleted(index))
                                    }) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Usuń",
                                            tint = DarkBlue,
                                            modifier = Modifier.size(40.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Dialog przeniesiony poza pętlę LazyColumn
                    if (state.showDeleteDialog && state.wordIndexToDelete != null && state.wordIndexToDelete!! in state.vocabItems.indices) {
                        YesNoDialogWithName(
                            show = true,
                            message = "Czy chcesz usunąć z kroku uczenia słowo:",
                            name = "${state.vocabItems[state.wordIndexToDelete!!].learnedWord}?",
                            onConfirm = {
                                onEvent(ConfigurationMaterialEvent.ConfirmDelete(state.wordIndexToDelete!!))
                            },
                            onDismiss = {
                                onEvent(ConfigurationMaterialEvent.CancelDelete)
                            }
                        )
                    }

                    Box(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Button(
                            onClick = { onEvent(ConfigurationMaterialEvent.ShowAddDialog) },
                            colors = ButtonDefaults.buttonColors(backgroundColor = DarkBlue),
                            modifier = Modifier.width(200.dp).height(48.dp)
                        ) {
                            Text(
                                "DODAJ",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }

            // Obrazki i checkboxy po prawej
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(
                        color = White
                    )
                    .padding(16.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    val selectedIndex = state.selectedWordIndex
                    if (selectedIndex in state.vocabItems.indices) {
                        val item = state.vocabItems[selectedIndex]
                        val images = item.imagePaths

                        if (images.isEmpty()) {
                            Text(
                                "Ten materiał nie zawiera żadnych zdjęć. Aby je dodać, przejdź do sekcji 'MATERIAŁY EDUKACYJNE'.",
                                fontSize = 20.sp
                            )
                        } else {
                            ImageSelectionWithCheckbox(
                                images = images,
                                selectedImages = item.selectedImages,
                                inLearningStates = item.inLearningStates,
                                inTestStates = item.inTestStates,
                                onImageSelectionChanged = {
                                    onEvent(ConfigurationMaterialEvent.ImageSelectionChanged(it))
                                },
                                onLearningTestChanged = { i, learning, test ->
                                    onEvent(
                                        ConfigurationMaterialEvent.LearningTestChanged(
                                            i,
                                            learning,
                                            test
                                        )
                                    )
                                }
                            )
                        }

                    } else {
                        Text(
                            "Do kroku uczenia nie dodano jeszcze żadnych materiałów. Aby to zrobić, kliknij przycisk 'DODAJ'. ",
                            fontSize = 20.sp
                        )
                    }
                }
            }
        }


        // Dialog dodawania słowa
        if (state.showAddDialog) {
            val scrollState = rememberScrollState()
            val lazyListState = rememberLazyListState()
            val scrollAreaState = rememberScrollAreaState(lazyListState)

            // Funkcja do zamykania klawiatury i dialogu
            val closeDialog = {
                searchQuery = ""
                keyboardController?.hide()
                focusManager.clearFocus()
                onEvent(ConfigurationMaterialEvent.HideAddDialog)
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f))
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        closeDialog()
                    },
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .fillMaxHeight(0.8f)
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            keyboardController?.hide()
                            focusManager.clearFocus()
                        },
                    elevation = 8.dp,
                    color = Color.White,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        // Tytuł
                        Text(
                            text = "Wybierz materiał, który chcesz dodać do kroku uczenia:",
                            fontSize = 26.sp,
                            fontStyle = FontStyle.Italic,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        // Pole wyszukiwania z focusable
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            label = { Text("Szukaj...") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp),
                            keyboardOptions = KeyboardOptions.Default.copy(
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    keyboardController?.hide()
                                    focusManager.clearFocus()
                                }
                            ),
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                textColor = Color.Black,
                                cursorColor = DarkBlue,
                                focusedBorderColor = DarkBlue,
                                unfocusedBorderColor = Color.Gray,
                                focusedLabelColor = DarkBlue,
                                unfocusedLabelColor = Color.Gray
                            )
                        )

                        // Lista słów
                        val filteredWords = state.availableWordsToAdd.filter {
                            it.name.contains(searchQuery, ignoreCase = true) ||
                                    it.category.contains(searchQuery, ignoreCase = true)
                        }.sortedWith { a, b -> a.name.compareTo(b.name, ignoreCase = true) }

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                        ) {
                            ScrollArea(state = scrollAreaState) {
                                LazyColumn(
                                    state = lazyListState,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(end = 12.dp)
                                ) {
                                    if (filteredWords.isEmpty()) {
                                        item {
                                            Text(
                                                "BRAK WYNIKÓW\nDODAJ MATERIAŁY W SEKCJI MATERIAŁY EDUKACYJNE",
                                                fontSize = 16.sp,
                                                color = Color.Gray,
                                                modifier = Modifier.padding(vertical = 8.dp)
                                            )
                                        }
                                    } else {
                                        items(filteredWords) { resource ->
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .clickable {
                                                        closeDialog()
                                                        onEvent(ConfigurationMaterialEvent.AddWord(resource.id))
                                                    }
                                                    .padding(vertical = 8.dp)
                                            ) {
                                                Text(
                                                    text = resource.name,
                                                    fontSize = 18.sp,
                                                    fontWeight = FontWeight.Normal,
                                                    color = Color.Black
                                                )
                                                if (resource.category.isNotBlank()) {
                                                    Spacer(modifier = Modifier.width(8.dp))
                                                    Text(
                                                        text = "(${resource.category})",
                                                        fontSize = 16.sp,
                                                        color = Color.Gray
                                                    )
                                                }
                                            }

                                        }
                                    }
                                }

                                VerticalScrollbar(
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .fillMaxHeight()
                                        .width(8.dp)
                                ) {
                                    Thumb(Modifier.background(Color.Gray))
                                }
                            }
                        }

                        // Przycisk anuluj
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Button(
                                onClick = closeDialog,
                                colors = ButtonDefaults.buttonColors(
                                    backgroundColor = DarkBlue,
                                    contentColor = Color.White
                                )
                            ) {
                                Text("ANULUJ")
                            }
                        }
                    }
                }
            }

            // Efekt do ukrywania klawiatury przy zamknięciu
            LaunchedEffect(state.showAddDialog) {
                if (!state.showAddDialog) {
                    keyboardController?.hide()
                    focusManager.clearFocus()
                }
            }
        }

    }
}