package com.example.friendly_words

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.friendly_words.ui.theme.DarkBlue
import com.example.friendly_words.ui.theme.LightBlue


data class VocabularyItem(
    val word: String,
    var inLearning: Boolean = true,
    var inTest: Boolean = true,
    var selectedImages: MutableList<Boolean> = mutableListOf<Boolean>().apply {
        addAll(List(getImageResourcesForWord(word).size) { true })
    }
)



fun getImageResourcesForWord(word: String): List<Int> {
    return when (word.lowercase()) {
        "misiu" -> listOf(
            R.drawable.misiu_1,
            R.drawable.misiu_2,
            R.drawable.misiu_3
        )
        "tablet" -> listOf(
            R.drawable.tablet_1,
            R.drawable.tablet_2,
            R.drawable.tablet_3
        )
        "but" -> listOf(
            R.drawable.but_1,
            R.drawable.but_2,
            R.drawable.but_3
        )
        else -> listOf(R.drawable.placeholder)
    }
}


@Composable
fun ImageSelectionWithCheckbox(
    images: List<Int>,
    selectedImages: List<Boolean>,
    onImageSelectionChanged: (List<Boolean>) -> Unit
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
                Box(
                    modifier = Modifier
                        .height(200.dp)
                        .aspectRatio(1f)
                ) {
                    Image(
                        painter = painterResource(id = resId),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                Checkbox(
                    checked = selectedImages[index],
                    onCheckedChange = {
                        val newSelectedImages = selectedImages.toMutableList()
                        newSelectedImages[index] = it
                        onImageSelectionChanged(newSelectedImages)
                    },
                    colors = CheckboxDefaults.colors(
                        checkedColor = LightBlue,
                        uncheckedColor = Color.Gray,
                        checkmarkColor = Color.White
                    )
                )
            }
        }
    }
}


@Composable
fun ConfigurationMaterialScreen(
    onBackClick: () -> Unit
) {
    var vocabItems by remember {
        mutableStateOf(
            mutableListOf(
                VocabularyItem("Misiu"),
                VocabularyItem("Tablet"),
                VocabularyItem("But")
            )
        )
    }

    var selectedWordIndex by remember { mutableStateOf<Int?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            // Left Panel - Vocabulary List
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(DarkBlue.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier.fillMaxHeight()
                ) {
                    // Headers
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        listOf("SŁOWO", "W UCZENIU", "W TEŚCIE", "USUŃ").forEach {
                            Text(
                                text = it,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Gray,
                                modifier = Modifier.weight(1f)
                            )
                            Spacer(modifier = Modifier.width(30.dp))
                        }
                    }

                    // Vocabulary List
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    ) {
                        items(vocabItems) { item ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 8.dp)
                                    .clickable {
                                        selectedWordIndex = vocabItems.indexOf(item)
                                    }
                            ) {
                                // Word text
                                Text(
                                    item.word,
                                    fontSize = 30.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.weight(1f)
                                )

                                // Learning Checkbox
                                Checkbox(
                                    checked = item.inLearning,
                                    onCheckedChange = {
                                        val index = vocabItems.indexOf(item)
                                        vocabItems = vocabItems.toMutableList().apply {
                                            this[index] = item.copy(inLearning = it)
                                        }
                                    },
                                    modifier = Modifier
                                        .scale(1.5f)
                                        .weight(1f),
                                    colors = CheckboxDefaults.colors(
                                        checkedColor = LightBlue,
                                        uncheckedColor = Color.Gray,
                                        checkmarkColor = Color.White
                                    )
                                )

                                // Test Checkbox
                                Checkbox(
                                    checked = item.inTest,
                                    onCheckedChange = {
                                        val index = vocabItems.indexOf(item)
                                        vocabItems = vocabItems.toMutableList().apply {
                                            this[index] = item.copy(inTest = it)
                                        }
                                    },
                                    modifier = Modifier
                                        .scale(1.5f)
                                        .weight(1f),
                                    colors = CheckboxDefaults.colors(
                                        checkedColor = LightBlue,
                                        uncheckedColor = Color.Gray,
                                        checkmarkColor = Color.White
                                    )
                                )

                                // Delete Icon
                                IconButton(
                                    onClick = {
                                        val itemIndex = vocabItems.indexOf(item)
                                        vocabItems = vocabItems.toMutableList().apply {
                                            remove(item)
                                        }

                                        if (selectedWordIndex == itemIndex) {
                                            selectedWordIndex = null
                                        } else if (selectedWordIndex != null && itemIndex < selectedWordIndex!!) {

                                            selectedWordIndex = selectedWordIndex!! - 1
                                        }
                                    },
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(
                                        Icons.Default.Delete,
                                        contentDescription = "Usuń",
                                        tint = DarkBlue,
                                        modifier = Modifier.size(40.dp)
                                    )
                                }
                            }
                        }
                    }


                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Button(
                            onClick = {
                                vocabItems = vocabItems.toMutableList().apply {
                                    add(VocabularyItem("Nowe słowo"))
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = DarkBlue
                            ),
                            modifier = Modifier
                                .width(200.dp)
                                .height(48.dp)
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


            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(16.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                Column {
                    Text("Wybierz obrazki", fontSize = 16.sp)
                    if (selectedWordIndex != null && selectedWordIndex in vocabItems.indices) {
                        val selectedWord = vocabItems[selectedWordIndex!!]
                        val images = getImageResourcesForWord(selectedWord.word)
                        ImageSelectionWithCheckbox(
                            images = images,
                            selectedImages = selectedWord.selectedImages,
                            onImageSelectionChanged = { selectedImageStates ->
                                vocabItems = vocabItems.toMutableList().apply {
                                    this[selectedWordIndex!!] = selectedWord.copy(
                                        selectedImages = selectedImageStates.toMutableList()
                                    )
                                }
                            }
                        )
                    } else {
                        Text("Wybierz materiał na liście obok", fontSize = 25.sp)
                    }
                }
            }
        }
    }
}