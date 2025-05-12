package com.example.friendly_words

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import com.example.friendly_words.ui.theme.DarkBlue

@Composable
fun ConfigurationsListScreen(
    onBackClick: () -> Unit,
    onCreateClick: () -> Unit,
    onEditClick: (String) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }

    var configurations by remember {
        mutableStateOf(
            mutableListOf("1 konfiguracja", "2 konfiguracja")
        )
    }

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
                            fontSize = 30.sp,
                            color = Color.White,
                            modifier = Modifier
                                .clickable { onCreateClick() }
                        )
                        Spacer(modifier = Modifier.width(15.dp))
                    }
                },
                backgroundColor = DarkBlue
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                placeholder = {
                    Text(
                        text = "Wyszukaj",
                        style = TextStyle(fontSize = 35.sp)
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

            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Column {
                    Spacer(modifier = Modifier.height(20.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Spacer(modifier = Modifier.width(20.dp))

                        var showTooltip by remember { mutableStateOf(false) }

                        Text(
                            text = "NAZWA KONFIGURACJI",
                            fontSize = 25.sp,
                            color = Color.Gray
                        )

                        Spacer(modifier = Modifier.width(6.dp))

                        Box {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = "Informacja",
                                tint = Color.Gray,
                                modifier = Modifier
                                    .size(40.dp)
                                    .clickable { showTooltip = !showTooltip }
                            )

                            if (showTooltip) {
                                Popup(
                                    alignment = Alignment.TopStart,
                                    onDismissRequest = { showTooltip = false }
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(Color.White)
                                            .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                                            .padding(8.dp)
                                    ) {
                                        Text(
                                            text = "Zestaw do nauki z dodatkowymi ustawieniami procesu uczenia.",
                                            fontSize = 30.sp,
                                            color = Color.Black,
                                            textAlign = TextAlign.Start
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.width(840.dp))

                        Text(
                            text = "AKCJE",
                            fontSize = 25.sp,
                            color = Color.Gray
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))


                    configurations.forEach { config ->
                        ConfigurationItem(
                            title = config,
                            onDelete = {
                                configurations = configurations.toMutableList().apply {
                                    remove(config)
                                }
                            },
                            onEdit = { onEditClick(config) }
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun ConfigurationItem(
    title: String,
    onDelete: () -> Unit,
    onEdit: () -> Unit // Callback for edit action
) {
    var isChecked by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .background(DarkBlue.copy(alpha = 0.2f)),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = isChecked,
                onCheckedChange = { isChecked = it },
                colors = CheckboxDefaults.colors(
                    checkedColor = DarkBlue,
                    uncheckedColor = Color.Gray,
                    checkmarkColor = Color.White
                )
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Spacer(modifier = Modifier.height(13.dp))
                Text(
                    text = title,
                    fontSize = 30.sp
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = "(konfiguracja aktywna w trybie: uczenie)",
                    fontSize = 20.sp
                )
            }
            Spacer(modifier = Modifier.width(590.dp))
            IconButton(onClick = { }) {
                Icon(
                    imageVector = Icons.Default.FileCopy,
                    contentDescription = "Copy",
                    tint = DarkBlue,
                    modifier = Modifier.size(65.dp)
                )
            }
            Spacer(modifier = Modifier.width(20.dp))
            IconButton(onClick = onEdit) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit",
                    tint = DarkBlue,
                    modifier = Modifier.size(65.dp)
                )
            }
            Spacer(modifier = Modifier.width(20.dp))
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = DarkBlue,
                    modifier = Modifier.size(65.dp)
                )
            }
        }
    }
}