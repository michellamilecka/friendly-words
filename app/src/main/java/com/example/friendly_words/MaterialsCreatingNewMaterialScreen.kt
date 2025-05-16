package com.example.friendly_words

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.friendly_words.ui.components.NewConfigurationTopBar
import com.example.friendly_words.ui.components.NewMaterialTopTabs
import com.example.friendly_words.ui.components.YesNoDialog
import com.example.friendly_words.ui.theme.DarkBlue
import com.example.friendly_words.ui.theme.LightBlue

@Composable
fun MaterialsCreatingNewMaterialScreen(onBackClick: () -> Unit, onSaveClick: () -> Unit) {
    var resourceName by remember { mutableStateOf("") }

    val images = remember {
        mutableStateListOf(
            R.drawable.misiu_1, R.drawable.misiu_2, R.drawable.misiu_3,
            R.drawable.misiu_1, R.drawable.misiu_1, R.drawable.misiu_1,
            R.drawable.misiu_1, R.drawable.misiu_1
        )
    }
    var showExitConfirmation by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            NewConfigurationTopBar(title = "Nazwa zasobu:", onBackClick = { showExitConfirmation = true })
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onSaveClick()
                    // tutaj logika zapisywania
                },
                backgroundColor = DarkBlue,
                contentColor = Color.White,
                modifier = Modifier.size(72.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Save, //lub Outlined.Save
                    contentDescription = null,
                    modifier = Modifier.size(32.dp)

                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color.White)
                .padding(24.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            ) {
                // Lewa kolumna: nazwa zasobu
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(200.dp))

                    Text(
                        text = "Nazwa zasobu",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkBlue,
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    TextField(
                        value = resourceName,
                        onValueChange = { resourceName = it },
                        placeholder = { Text("Wpisz nazwę...") },
                        modifier = Modifier.fillMaxWidth(0.8f),
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = Color.White,
                            focusedIndicatorColor = DarkBlue,
                            unfocusedIndicatorColor = Color.Gray
                        )
                    )

                    Spacer(modifier = Modifier.height(80.dp))

                    Button(
                        onClick = {
                            images.add(0, R.drawable.misiu_1)
                        },
                        colors = ButtonDefaults.buttonColors(backgroundColor = DarkBlue),
                        modifier = Modifier
                            .fillMaxWidth(0.3f)
                            .height(48.dp)
                    ) {
                        Text("Dodaj obrazek", color = Color.White, fontSize = 16.sp)
                    }
                }


                // Prawa kolumna: zdjęcia z opcją usuwania
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "Dodane obrazki",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkBlue
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    val groupedImages = images.chunked(3)

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(500.dp) // ustalasz sztywną wysokość, np. 300dp nad FAB
                            .padding(horizontal = 8.dp)
                    ) {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp)
                            //.weight(1f)
                            //.heightIn(200.dp)
                        ) {
                            items(groupedImages) { group ->
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    group.forEach { imageRes ->
                                        Box(
                                            modifier = Modifier
                                                .weight(1f)
                                                .aspectRatio(1f)
                                                .padding(top = 8.dp)
                                                .background(Color.White),
                                            contentAlignment = Alignment.TopEnd
                                        ) {
                                            Image(
                                                painter = painterResource(id = imageRes),
                                                contentDescription = null,
                                                contentScale = ContentScale.Fit,
                                                modifier = Modifier
                                                    .fillMaxSize()
                                            )

                                            Box(
                                                modifier = Modifier
                                                    .align(Alignment.TopEnd)
                                                    .padding(6.dp)
                                                    .size(38.dp)
                                                    .background(
                                                        color = LightBlue,
                                                        shape = MaterialTheme.shapes.small
                                                    ),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Filled.Clear,
                                                    contentDescription = "Usuń zdjęcie",
                                                    tint = Color.White,
                                                    modifier = Modifier
                                                        .size(14.dp)
                                                        .clickable { images.remove(imageRes) }
                                                )
                                            }
                                        }
                                    }

                                    repeat(3 - group.size) {
                                        Spacer(modifier = Modifier.weight(1f))
                                    }
                                }
                            }

                        }
                    }
                }
            }
        }

        if (showExitConfirmation) {
            YesNoDialog(
                show = showExitConfirmation,
                message = "Wyjść bez zapisywania?",
                onConfirm = {
                    showExitConfirmation = false
                    onBackClick() // TAK
                },
                onDismiss = {
                    showExitConfirmation = false
                }
            )
        }


    }
}


