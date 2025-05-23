package com.example.friendly_words

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.friendly_words.ui.components.YesNoDialog
import com.example.friendly_words.ui.theme.DarkBlue
import com.example.friendly_words.ui.theme.LightBlue

data class VocabularyItem(
    val word: String,
    val selectedImages: List<Boolean>,
    val inLearningStates: List<Boolean>,
    val inTestStates: List<Boolean>
) {
    companion object {
        fun create(word: String): VocabularyItem {
            val size = getImageResourcesForWord(word).size
            return VocabularyItem(
                word = word,
                selectedImages = List(size) { it == 0 }, // tylko pierwsze zdjęcie = true
                inLearningStates = List(size) { it == 0 }, // tylko pierwsze = true
                inTestStates = List(size) { it == 0 } // tylko pierwsze = true
            )
        }
    }
}

fun getImageResourcesForWord(word: String): List<Int> {
    return when (word.lowercase()) {
        "misiu" -> listOf(R.drawable.misiu_1, R.drawable.misiu_2, R.drawable.misiu_3)
        "tablet" -> listOf(R.drawable.tablet_1, R.drawable.tablet_2, R.drawable.tablet_3)
        "but" -> listOf(R.drawable.but_1, R.drawable.but_2, R.drawable.but_3)
        "kredka" -> listOf(R.drawable.kredka_1, R.drawable.kredka_2, R.drawable.kredka_3) // Przykładowo
        "parasol" -> listOf(R.drawable.parasol_1, R.drawable.parasol_2, R.drawable.parasol_3) // Przykładowo
        else -> listOf(R.drawable.placeholder)
    }
}

@Composable
fun ImageSelectionWithCheckbox(
    images: List<Int>,
    selectedImages: List<Boolean>,
    onImageSelectionChanged: (List<Boolean>) -> Unit,
    onLearningTestChanged: (index: Int, inLearning: Boolean, inTest: Boolean) -> Unit,
    inLearningStates: List<Boolean>,
    inTestStates: List<Boolean>
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        images.forEachIndexed { index, resId ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                Checkbox(
                    checked = selectedImages[index],
                    onCheckedChange = {
                        val newSelectedImages = selectedImages.toMutableList().also { it[index] = it[index].not() }
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
                        painter = painterResource(id = resId),
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
                        enabled = selectedImages[index],
                        onCheckedChange = { newLearning ->
                            val currentTest = inTestStates[index]
                            val shouldSelectImage = newLearning || currentTest

                            // Update stan obrazka (czy ma być wybrany)
                            val newSelectedImages = selectedImages.toMutableList().also { it[index] = shouldSelectImage }
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
                        enabled = selectedImages[index],
                        onCheckedChange = { newTest ->
                            val currentLearning = inLearningStates[index]
                            val shouldSelectImage = newTest || currentLearning

                            // Update stan obrazka
                            val newSelectedImages = selectedImages.toMutableList().also { it[index] = shouldSelectImage }
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

@Composable
fun ConfigurationMaterialScreen(onBackClick: () -> Unit) {
    var vocabItems by remember {
        mutableStateOf(
            listOf(
                VocabularyItem.create("Misiu"),
                VocabularyItem.create("Tablet"),
                VocabularyItem.create("But")
            )
        )
    }
    val configuration = LocalConfiguration.current

    var showDeleteDialog by remember { mutableStateOf(false) }
    var selectedWordIndex by remember { mutableStateOf(0) }
    var wordIndexToDelete by remember { mutableStateOf<Int?>(null) } // Nowy stan dla indeksu do usunięcia

    // Nowe stany do dodawania słowa
    var showAddDialog by remember { mutableStateOf(false) }
    var availableWordsToAdd by remember { mutableStateOf(listOf("Kredka", "Parasol")) }

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
                        listOf("SŁOWO", "W UCZENIU", "W TEŚCIE", "USUŃ").forEachIndexed { index, label ->
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
                        items(vocabItems) { item ->
                            val index = vocabItems.indexOf(item)
                            val isSelected = selectedWordIndex == index

                            val hasLearning = item.selectedImages.zip(item.inLearningStates).any { it.first && it.second }
                            val hasTest = item.selectedImages.zip(item.inTestStates).any { it.first && it.second }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(if (isSelected) LightBlue.copy(alpha = 0.3f) else Color.Transparent)
                                    .padding(horizontal = 4.dp, vertical = 8.dp)
                                    .clickable { selectedWordIndex = index }
                            ) {
                                Box(modifier = Modifier.weight(1f)) {
                                    Text(
                                        item.word,
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
                                        wordIndexToDelete = index // Ustaw indeks elementu do usunięcia
                                        showDeleteDialog = true
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
                    if (showDeleteDialog && wordIndexToDelete != null && wordIndexToDelete!! in vocabItems.indices) {
                        YesNoDialog(
                            show = showDeleteDialog,
                            message = "Czy chcesz usunąć z konfiguracji materiał:\n${vocabItems[wordIndexToDelete!!].word}?",
                            onConfirm = {
                                vocabItems = vocabItems.toMutableList().also { it.removeAt(wordIndexToDelete!!) }
                                selectedWordIndex = when {
                                    selectedWordIndex == wordIndexToDelete -> -1
                                    selectedWordIndex > wordIndexToDelete!! -> selectedWordIndex - 1
                                    else -> selectedWordIndex
                                }
                                showDeleteDialog = false
                                wordIndexToDelete = null // Resetuj indeks po usunięciu
                            },
                            onDismiss = {
                                showDeleteDialog = false
                                wordIndexToDelete = null // Resetuj indeks po zamknięciu dialogu
                            }
                        )
                    }

                    Box(modifier = Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center) {
                        Button(
                            onClick = {
                                showAddDialog = true
                            },
                            colors = ButtonDefaults.buttonColors(backgroundColor = DarkBlue),
                            modifier = Modifier.width(200.dp).height(48.dp)
                        ) {
                            Text("DODAJ", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
            }

            // Obrazki i checkboxy po prawej
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(16.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                Column {
                    if (selectedWordIndex in vocabItems.indices) {
                        val selectedWord = vocabItems[selectedWordIndex]
                        val images = getImageResourcesForWord(selectedWord.word)

                        ImageSelectionWithCheckbox(
                            images = images,
                            selectedImages = selectedWord.selectedImages,
                            onImageSelectionChanged = { newSelectedImages ->
                                vocabItems = vocabItems.mapIndexed { i, item ->
                                    if (i == selectedWordIndex)
                                        item.copy(selectedImages = newSelectedImages)
                                    else item
                                }
                            },
                            onLearningTestChanged = { idx, newInLearning, newInTest ->
                                vocabItems = vocabItems.mapIndexed { i, item ->
                                    if (i == selectedWordIndex)
                                        item.copy(
                                            inLearningStates = item.inLearningStates.toMutableList().also { it[idx] = newInLearning },
                                            inTestStates = item.inTestStates.toMutableList().also { it[idx] = newInTest }
                                        )
                                    else item
                                }
                            },
                            inLearningStates = selectedWord.inLearningStates,
                            inTestStates = selectedWord.inTestStates
                        )
                    } else {
                        Text("Wybierz słowo z listy po lewej.", fontSize = 20.sp)
                    }
                }
            }
        }

        // Dialog dodawania słowa
        if (showAddDialog) {
            AlertDialog(
                onDismissRequest = { showAddDialog = false },
                title = {
                    Text("Wybierz słowo, które chcesz dodać do konfiguracji:")
                },
                text = {
                    if (availableWordsToAdd.isEmpty()) {
                        Text("BRAK")
                    } else {
                        Column {
                            availableWordsToAdd.forEach { word ->
                                Text(
                                    text = word,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            // Dodaj słowo
                                            vocabItems = vocabItems + VocabularyItem.create(word)
                                            // Usuń słowo z dostępnych
                                            availableWordsToAdd = availableWordsToAdd.filterNot { it == word }
                                            // Zamknij dialog
                                            showAddDialog = false
                                        }
                                        .padding(vertical = 8.dp),
                                    fontSize = 18.sp
                                )
                            }
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = { showAddDialog = false },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = DarkBlue,
                            contentColor = Color.White
                        )
                    ) {
                        Text("ANULUJ")
                    }
                }
            )
        }
    }
}