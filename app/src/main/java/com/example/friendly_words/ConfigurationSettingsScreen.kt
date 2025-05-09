package com.example.friendly_words

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class VocabularyItem(
    val word: String,
    var inLearning: Boolean = true,
    var inTest: Boolean = true
)

@Composable
fun ConfigurationSettingsScreen(
    onBackClick: () -> Unit // Dodajemy funkcję do obsługi kliknięcia w strzałkę
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

    var newWordText by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        IconButton(onClick = onBackClick) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White
                            )
                        }
                        Text(
                            "Nowa konfiguracja",
                            fontSize = 30.sp,
                            modifier = Modifier.weight(1f),
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.width(15.dp))
                    }
                },
                backgroundColor = Color(0xFF004B88)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            TabRow(
                selectedTabIndex = 0,
                backgroundColor = Color.White,
                contentColor = Color.Black
            ) {
                Tab(
                    selected = true,
                    onClick = {},
                    modifier = Modifier.height(50.dp),
                    text = { Text(text="MATERIAŁ", fontWeight = FontWeight.Bold, modifier = Modifier.padding(vertical = 10.dp)) }
                )
                Tab(
                    selected = false,
                    onClick = {},
                    modifier = Modifier.width(50.dp),
                    text = { Text(text="UCZENIE", fontWeight = FontWeight.Bold, modifier = Modifier.padding(vertical = 10.dp)) }
                )
                Tab(
                    selected = false,
                    onClick = {},
                    modifier = Modifier.width(50.dp),
                    text = { Text(text="WZMOCNIENIA", fontWeight = FontWeight.Bold, modifier = Modifier.padding(vertical = 10.dp)) }
                )
                Tab(
                    selected = false,
                    onClick = {},
                    modifier = Modifier.width(50.dp),
                    text = { Text(text="TEST", fontWeight = FontWeight.Bold, modifier = Modifier.padding(vertical = 10.dp)) }
                )
                Tab(
                    selected = false,
                    onClick = {},
                    modifier = Modifier.width(50.dp),
                    text = { Text(text="ZAPISZ", fontWeight = FontWeight.Bold, modifier = Modifier.padding(vertical = 10.dp)) }
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            ) {Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(Color(0xFF004B88).copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ){
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "SŁOWO",
                            fontSize=20.sp,
                            modifier = Modifier.weight(1f),
                            fontWeight = FontWeight.Bold,
                            color=Color.Gray
                        )
                        Spacer(modifier = Modifier.width(50.dp))
                        Text(
                            "W UCZENIU",
                            fontSize=20.sp,
                            modifier = Modifier.weight(1f),
                            fontWeight = FontWeight.Bold,
                            color=Color.Gray
                        )
                        Spacer(modifier = Modifier.width(50.dp))
                        Text(
                            "W TEŚCIE",
                            fontSize=20.sp,
                            modifier = Modifier.weight(1f),
                            fontWeight = FontWeight.Bold,
                            color=Color.Gray
                        )
                        Spacer(modifier = Modifier.width(65.dp))
                        Text(
                            "USUŃ",
                            fontSize=20.sp,
                            modifier = Modifier.weight(1f),
                            fontWeight = FontWeight.Bold,
                            color=Color.Gray
                        )
                        //Spacer(modifier = Modifier.width(10.dp))
                    }

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
                                    fontSize=30.sp,
                                    fontWeight = FontWeight.Bold,
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
                                        modifier = Modifier.scale(1.5f),
                                        colors = CheckboxDefaults.colors(
                                            checkedColor = Color(0xFF2EB2D6),
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
                                        modifier = Modifier.scale(1.5f),
                                        colors = CheckboxDefaults.colors(
                                            checkedColor = Color(0xFF2EB2D6),
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
                                            tint = Color(0xFF004B88),
                                            modifier = Modifier.size(40.dp)
                                        )
                                    }
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
                                backgroundColor = Color(0xFF004B88)
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
                        .fillMaxHeight(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text="Wybierz materiał na liście obok",
                        fontSize=25.sp)
                }
            }
        }
    }
}


