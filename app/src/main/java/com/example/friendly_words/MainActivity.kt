package com.example.friendly_words

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                LanguageLearningApp()
            }
        }
    }
}

data class VocabularyItem(
    val word: String,
    var inLearning: Boolean = true,
    var inTest: Boolean = true
)

@Composable
fun LanguageLearningApp() {
    var vocabItems by remember {
        mutableStateOf(
            mutableListOf(
                VocabularyItem("Misiu"),
                VocabularyItem("Tablet"),
                VocabularyItem("But")
            )
        )
    }

    var newWordText by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        IconButton(onClick = { /* Go back */ }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                        Text(
                            "Nazwa konfiguracji: Przykład",
                            fontSize = 18.sp,
                            modifier = Modifier.weight(1f)
                        )
                        TextButton(onClick = { /* Save action */ }) {
                            Text("ZAPISZ", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                },
                backgroundColor = Color(0xFF3F51B5)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Tabs with equal width
            TabRow(
                selectedTabIndex = 0,
                backgroundColor = Color(0xFF3F51B5),
                contentColor = Color.White
            ) {
                Tab(
                    selected = true,
                    onClick = {},
                    text = { Text("Materiał", modifier = Modifier.padding(vertical = 12.dp)) }
                )
                Tab(
                    selected = false,
                    onClick = {},
                    text = { Text("Uczenie", modifier = Modifier.padding(vertical = 12.dp)) }
                )
                Tab(
                    selected = false,
                    onClick = {},
                    text = { Text("Wzmocnienia", modifier = Modifier.padding(vertical = 12.dp)) }
                )
                Tab(
                    selected = false,
                    onClick = {},
                    text = { Text("Test", modifier = Modifier.padding(vertical = 12.dp)) }
                )
            }

            // Main content area taking full remaining height
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            ) {
                // Left side - vocabulary list
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                ) {
                    // Headers
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "SŁOWO",
                            modifier = Modifier.weight(1f),
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "W UCZENIU",
                            modifier = Modifier.weight(1f),
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "W TEŚCIE",
                            modifier = Modifier.weight(1f),
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "USUŃ",
                            modifier = Modifier.weight(1f),
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // List of vocabulary items - takes all available space
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
                            ) {
                                Text(
                                    item.word,
                                    modifier = Modifier.weight(1f)
                                )
                                Box(
                                    modifier = Modifier.weight(1f),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Checkbox(
                                        checked = item.inLearning,
                                        onCheckedChange = {
                                            val index = vocabItems.indexOf(item)
                                            vocabItems = vocabItems.toMutableList().apply {
                                                this[index] = item.copy(inLearning = it)
                                            }
                                        },
                                        colors = CheckboxDefaults.colors(
                                            checkedColor = Color(0xFF039BE5),
                                            uncheckedColor = Color.Gray,
                                            checkmarkColor = Color.White
                                        )
                                    )
                                }
                                Box(
                                    modifier = Modifier.weight(1f),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Checkbox(
                                        checked = item.inTest,
                                        onCheckedChange = {
                                            val index = vocabItems.indexOf(item)
                                            vocabItems = vocabItems.toMutableList().apply {
                                                this[index] = item.copy(inTest = it)
                                            }
                                        },
                                        colors = CheckboxDefaults.colors(
                                            checkedColor = Color(0xFF039BE5),
                                            uncheckedColor = Color.Gray,
                                            checkmarkColor = Color.White
                                        )
                                    )
                                }
                                Box(
                                    modifier = Modifier.weight(1f),
                                    contentAlignment = Alignment.Center
                                ) {
                                    IconButton(
                                        onClick = {
                                            vocabItems = vocabItems.toMutableList().apply {
                                                remove(item)
                                            }
                                        }
                                    ) {
                                        Icon(
                                            Icons.Default.Delete,
                                            contentDescription = "Usuń",
                                            tint = Color(0xFF3F51B5)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Add button at bottom
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
                                backgroundColor = Color(0xFF3F51B5)
                            ),
                            modifier = Modifier
                                .width(200.dp)
                                .height(48.dp)
                        ) {
                            Text(
                                "DODAJ",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                // Right side panel
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Wybierz materiał na liście obok")
                }
            }
        }
    }
}