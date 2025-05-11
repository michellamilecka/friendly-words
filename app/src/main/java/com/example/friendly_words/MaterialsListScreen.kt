package com.example.friendly_words

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.friendly_words.ui.theme.DarkBlue
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.ui.window.Dialog


@Composable
fun MaterialsListScreen(
    onBackClick: () -> Unit,
    onCreateClick: () -> Unit
) {
    val materials = remember { mutableStateListOf("Baja", "But", "Kredka", "vsvcsd", "sdcdscd", "cdsdc", "cdscd", "cdcdscsdc", "cdscdc", "cdsc","534","6789cv") }

    var showDeleteDialog by remember { mutableStateOf(false) }
    var materialToDelete by remember { mutableStateOf<String?>(null) }  // Przechowujemy nazwę materiału do usunięcia
    var showDeleteNotification by remember { mutableStateOf(false) }

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
                            "Materiały",
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
            Spacer(modifier = Modifier.height(20.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 50.dp)
            ) {
                Text(
                    "NAZWA ZASOBU",
                    fontSize = 25.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(start = 140.dp)
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    "AKCJE",
                    fontSize = 25.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(end = 140.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
            Spacer(modifier = Modifier.height(20.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(500.dp)
                    .padding(horizontal = 120.dp)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        //.width(2000.dp)
                        //.align(Alignment.Center)
                        .padding(start = 20.dp, end = 20.dp)
                ) {
                    itemsIndexed(materials) { index, material ->
                        Column {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    //.padding(start = 160.dp, end = 120.dp)
                                    .padding(vertical = 10.dp)
                                    .background(Color.White)
                                    .height(40.dp)
                            ) {
                                Text(
                                    text = material,
                                    fontSize = 25.sp,
                                    //modifier = Modifier.weight(1f)
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(start = 10.dp)
                                )

                                IconButton(onClick = {
                                    // edycja TODO
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = "Edytuj",
                                        tint = DarkBlue,
                                        modifier = Modifier.size(35.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.width(10.dp))

                                IconButton(onClick = {
                                    materialToDelete = material
                                    showDeleteDialog = true
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Usuń",
                                        tint = DarkBlue,
                                        modifier = Modifier.size(35.dp)
                                    )
                                }
                            }

                            if (index != materials.lastIndex) {
                                Spacer(modifier = Modifier.height(10.dp))
                                Divider(
                                    color = Color.LightGray,
                                    thickness = 1.dp,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                    //.padding(start = 140.dp, end = 110.dp)
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                            }
                        }
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                if (showDeleteNotification) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter)
                            //.padding(16.dp)
                            .background(Color(0xFF4CAF50))
                    ) {
                        Text(
                            text = "Usunięto zasób: ${materialToDelete ?: "nieznany"}",
                            color = Color.White,
                            fontSize = 18.sp,
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(8.dp)
                        )
                    }

                    LaunchedEffect(Unit) {
                        kotlinx.coroutines.delay(3000)
                        showDeleteNotification = false
                    }
                }
            }
        }
    }

    if (showDeleteDialog) {
        Dialog(onDismissRequest = { showDeleteDialog = false }) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White, shape = MaterialTheme.shapes.medium)
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Czy na pewno chcesz usunąć zasób: $materialToDelete?",
                        fontSize = 24.sp,
                        modifier = Modifier.padding(bottom = 32.dp)
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            onClick = {
                                materialToDelete?.let { material ->
                                    materials.remove(material)
                                    showDeleteNotification = true
                                    showDeleteDialog = false
                                }
                            },
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color.White)
                        ) {
                            Text("Tak", fontSize = 20.sp, color = DarkBlue)
                        }

                        Button(
                            onClick = { showDeleteDialog = false },
                            colors = ButtonDefaults.buttonColors(backgroundColor = DarkBlue)
                        ) {
                            Text("Nie", fontSize = 20.sp, color = Color.White)
                        }
                    }
                }
            }
        }
    }
}

