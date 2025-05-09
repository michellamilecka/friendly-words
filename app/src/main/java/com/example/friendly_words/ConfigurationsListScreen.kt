package com.example.friendly_words

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FileCopy
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search

import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ConfigurationsListScreen(onBackClick: () -> Unit) {
    var searchQuery by remember { mutableStateOf("") } // Stan dla tekstu wyszukiwania

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
                            "Przyjazne Słowa Ustawienia",
                            fontSize = 30.sp,
                            modifier = Modifier.weight(1f),
                            color = Color.White
                        )
                        Text(
                            "UTWÓRZ",
                            fontSize=30.sp,
                            color=Color.White
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
            // Pasek wyszukiwania
            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                placeholder = {
                    Text(
                        text = "Wyszukaj",
                        style = TextStyle(fontSize = 35.sp) // Zwiększona wielkość napisu
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search Icon",
                        tint = Color.Gray
                    )
                },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = Color.Black
                ),
                shape = RoundedCornerShape(8.dp)
            )

            // Reszta zawartości ekranu
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                //contentAlignment = Alignment.Center
            ) {
                Column {
                    Spacer(modifier = Modifier.height(20.dp))
                    Row { Spacer(modifier = Modifier.width(20.dp))
                        Text(text="NAZWA KONFIGURACJI",
                        fontSize=25.sp,
                        color=Color.Gray)
                        Spacer(modifier = Modifier.width(895.dp))
                        Text(
                            text="AKCJE",
                            fontSize=25.sp,
                            color=Color.Gray)
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .background(Color.LightGray),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            var isChecked by remember { mutableStateOf(false) }

                            Checkbox(
                                checked = isChecked,
                                onCheckedChange = { isChecked = it },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = Color(0xFF004B88),
                                    uncheckedColor = Color.Gray,
                                    checkmarkColor = Color.White
                                )
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Spacer(modifier = Modifier.height(13.dp))
                                Text(
                                    text = "1 konfiguracja",
                                    fontSize = 30.sp
                                )
                                Spacer(modifier = Modifier.height(3.dp))
                                Text(
                                    text = "(konfiguracja aktywna w trybie: uczenie)",
                                    fontSize = 20.sp
                                )
                            }
                            Spacer(modifier = Modifier.width(150.dp))
                            IconButton(onClick = { /* Akcja kopiowania */ }) {
                                Icon(
                                    imageVector = Icons.Default.FileCopy,
                                    contentDescription = "Copy",
                                    tint = Color(0xFF004B88)
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            IconButton(onClick = { /* Akcja edycji */ }) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Edit",
                                    tint = Color(0xFF004B88)
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            IconButton(onClick = { /* Akcja usuwania */ }) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete",
                                    tint = Color(0xFF004B88)
                                )
                            }
                        }

                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .background(Color.LightGray),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically // Wyrównanie elementów w pionie
                        ) {
                            var isChecked by remember { mutableStateOf(false) } // Stan dla checkboxa

                            Checkbox(
                                checked = isChecked,
                                onCheckedChange = { isChecked = it }, // Aktualizacja stanu po zaznaczeniu
                                colors = CheckboxDefaults.colors(
                                    checkedColor = Color(0xFF004B88), // Kolor checkboxa po zaznaczeniu (niebieski z Twojego projektu)
                                    uncheckedColor = Color.Gray, // Kolor checkboxa, gdy niezaznaczony
                                    checkmarkColor = Color.White // Kolor znacznika (biały)
                                )
                            )
                            Spacer(modifier = Modifier.width(8.dp)) // Odstęp między checkboxem a tekstem
                            Text(
                                text = "1 konfiguracja",
                                fontSize = 30.sp
                            )
                        }
                    }
                }
            }
        }
    }
}