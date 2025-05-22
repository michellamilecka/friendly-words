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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
                selectedImages = List(size) { true },
                inLearningStates = List(size) { true },
                inTestStates = List(size) { true }
            )
        }
    }
}

fun getImageResourcesForWord(word: String): List<Int> {
    return when (word.lowercase()) {
        "misiu" -> listOf(R.drawable.misiu_1, R.drawable.misiu_2, R.drawable.misiu_3)
        "tablet" -> listOf(R.drawable.tablet_1, R.drawable.tablet_2, R.drawable.tablet_3)
        "but" -> listOf(R.drawable.but_1, R.drawable.but_2, R.drawable.but_3)
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
                    },
                    colors = CheckboxDefaults.colors(
                        checkedColor = LightBlue,
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
                        onCheckedChange = {
                            onLearningTestChanged(index, it, inTestStates[index])
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
                        onCheckedChange = {
                            onLearningTestChanged(index, inLearningStates[index], it)
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

    var selectedWordIndex by remember { mutableStateOf(0) }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(modifier = Modifier.fillMaxSize().weight(1f)) {
            // Lista słów
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(DarkBlue.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Column(modifier = Modifier.fillMaxHeight()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 30.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        listOf("SŁOWO", "W UCZENIU", "W TEŚCIE", "USUŃ").forEach {
                            Text(it, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                            Spacer(modifier = Modifier.width(73.dp))
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
                                    .background(if (isSelected) LightBlue.copy(alpha = 0.2f) else Color.Transparent)
                                    .padding(horizontal = 16.dp, vertical = 8.dp)
                                    .clickable { selectedWordIndex = index }
                            ) {
                                Text(item.word, fontSize = 30.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                                Spacer(modifier = Modifier.width(50.dp))
                                Icon(
                                    imageVector = if (hasLearning) Icons.Default.Check else Icons.Default.Close,
                                    contentDescription = null,
                                    tint = if (hasLearning) Color(0xFF4CAF50) else Color.Red,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(150.dp))
                                Icon(
                                    imageVector = if (hasTest) Icons.Default.Check else Icons.Default.Close,
                                    contentDescription = null,
                                    tint = if (hasTest) Color(0xFF4CAF50) else Color.Red,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(100.dp))
                                IconButton(onClick = {
                                    vocabItems = vocabItems.toMutableList().also { it.removeAt(index) }
                                    selectedWordIndex = when {
                                        selectedWordIndex == index -> -1
                                        selectedWordIndex > index -> selectedWordIndex - 1
                                        else -> selectedWordIndex
                                    }
                                }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Usuń", tint = DarkBlue, modifier = Modifier.size(40.dp))
                                }
                                Spacer(modifier = Modifier.width(50.dp))
                            }
                        }
                    }

                    Box(modifier = Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center) {
                        Button(
                            onClick = {
                                vocabItems = vocabItems + VocabularyItem.create("Nowe słowo")
                            },
                            colors = ButtonDefaults.buttonColors(backgroundColor = DarkBlue),
                            modifier = Modifier.width(200.dp).height(48.dp)
                        ) {
                            Text("DODAJ", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
            }

            // Obrazki i checkboxy
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
    }
}
