package com.example.friendly_words

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.filled.Create
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.window.Dialog
import com.example.friendly_words.ui.theme.LightBlue
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items


@Composable
fun MaterialsListScreen(
    onBackClick: () -> Unit,
    onCreateClick: () -> Unit
) {
    var selectedWordIndex by remember { mutableStateOf<Int?>(null) }

    var materials by remember {
        mutableStateOf(
            mutableListOf("Misiu", "Tablet", "But")
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                backgroundColor = DarkBlue,
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
                            color = Color.White,
                            modifier = Modifier.weight(1f)
                        )

                        Text(
                            "UTWÓRZ",
                            fontSize = 30.sp,
                            color = Color.White,
                            modifier = Modifier.clickable { onCreateClick() }
                        )
                        Spacer(modifier = Modifier.width(15.dp))
                    }
                }
            )
        }
    ) { padding ->
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Lewy panel – 1/3 ekranu
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .background(DarkBlue.copy(alpha = 0.1f))
                    //.padding(12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                        //.padding(start = 12.dp, end = 12.dp, bottom = 8.dp)
                ) {
                    Text(
                        "NAZWA ZASOBU",
                        modifier = Modifier.weight(1f),
                        fontSize = 20.sp,
                        color = Color.Gray,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "AKCJE",
                        fontSize = 20.sp,
                        color = Color.Gray,
                        fontWeight = FontWeight.Bold
                    )
                }

                LazyColumn {
                    itemsIndexed(materials) { index, word ->
                        val isSelected = selectedWordIndex == index
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(if (isSelected) LightBlue.copy(alpha = 0.3f) else Color.Transparent)
                                .clickable { selectedWordIndex = index }
                        ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                //.background(if (isSelected) LightBlue.copy(alpha = 0.3f) else Color.Transparent)
                                //.clickable { selectedWordIndex = index }
                                .padding(vertical = 8.dp, horizontal = 12.dp)
                                .height(55.dp)
                        ) {
                            Text(
                                word,
                                fontSize = 28.sp,
                                fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal,
                                //fontWeight = FontWeight.Medium,
                                modifier = Modifier.weight(1f)
                            )
                            IconButton(onClick = { /* TODO: edit logic */ }) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Edytuj",
                                    tint = DarkBlue,
                                    modifier = Modifier.size(35.dp)
                                )
                            }
                            IconButton(onClick = {
                                materials = materials.toMutableList().apply { removeAt(index) }
                                if (selectedWordIndex == index) {
                                    selectedWordIndex = null
                                } else if (selectedWordIndex != null && selectedWordIndex!! > index) {
                                    selectedWordIndex = selectedWordIndex!! - 1
                                }
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Usuń",
                                    tint = DarkBlue,
                                    modifier = Modifier.size(35.dp)
                                )
                            }
                        }}
                    }
                }
            }

            // Prawy panel – 2/3 ekranu
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(2f)
                    .padding(16.dp)
            ) {
                if (selectedWordIndex != null && selectedWordIndex in materials.indices) {
                    val word = materials[selectedWordIndex!!]
                    val images = getImageResourcesForWord(word)

                    Column(modifier = Modifier.fillMaxSize()) {
                        Text(
                            "Obrazki dla: $word",
                            fontSize = 24.sp,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )

                        LazyVerticalGrid(
                            columns = GridCells.Fixed(3),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                        ) {
                            items(images) { resId ->
                                Image(
                                    painter = painterResource(id = resId),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .aspectRatio(1f)
                                )
                            }
                        }

                    }

                } else {
                    Text("Wybierz materiał z listy", fontSize = 25.sp)
                }
            }
        }
    }
}
