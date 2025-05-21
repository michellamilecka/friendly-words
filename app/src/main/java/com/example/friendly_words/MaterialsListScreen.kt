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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.window.Dialog
import com.example.friendly_words.ui.theme.LightBlue
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import com.example.friendly_words.ui.components.YesNoDialog


@Composable
fun MaterialsListScreen(
    onBackClick: () -> Unit,
    onCreateClick: () -> Unit
) {
    var materials by remember {
        mutableStateOf(
            mutableListOf("Misiu", "Tablet", "But")
        )
    }
    var selectedWordIndex by remember {
        mutableStateOf(if (materials.isNotEmpty()) 0 else null)
    }

    var showDeleteDialog by remember { mutableStateOf(false) }
    var materialToDelete by remember { mutableStateOf<Pair<Int, String>?>(null) }
    var showDeleteNotification by remember { mutableStateOf(false) }
    var deletedMaterialName by remember { mutableStateOf<String?>(null) }

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
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
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
                                    .padding(vertical = 8.dp, horizontal = 12.dp)
                                    .height(55.dp)
                            ) {
                                Text(
                                    word,
                                    fontSize = 28.sp,
                                    fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal,
                                    modifier = Modifier.weight(1f)
                                )
                                IconButton(onClick = { onCreateClick() }) {
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = "Edytuj",
                                        tint = DarkBlue,
                                        modifier = Modifier.size(35.dp)
                                    )
                                }
                                IconButton(onClick = {
                                    materialToDelete = index to word
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
                        }
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

        if (showDeleteDialog && materialToDelete != null) {
            val (_, name) = materialToDelete!!
            YesNoDialog(
                show = showDeleteDialog,
                message = "Czy na pewno chcesz usunąć zasób: $name?",
                onConfirm = {
                    // akcja usunięcia
                    val (index, _) = materialToDelete!!
                    materials = materials.toMutableList().apply { removeAt(index) }
                    selectedWordIndex = when {
                        materials.isEmpty() -> null
                        selectedWordIndex == index -> 0
                        selectedWordIndex != null && selectedWordIndex!! > index -> selectedWordIndex!! - 1
                        else -> selectedWordIndex
                    }
                    deletedMaterialName = name
                    showDeleteNotification = true
                    showDeleteDialog = false
                },
                onDismiss = {
                    showDeleteDialog = false
                }
            )
        }


    }

    // Dialog potwierdzający usunięcie
    if (showDeleteDialog && materialToDelete != null) {
        val (index, name) = materialToDelete!!
        Dialog(onDismissRequest = {  }) {
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
                        text = "Czy na pewno chcesz usunąć zasób: $name?",
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
                                materials = materials.toMutableList().apply { removeAt(index) }
                                selectedWordIndex = when {
                                    materials.isEmpty() -> null
                                    selectedWordIndex == index -> 0       // ustaw pierwszy element po usunięciu wybranego
                                    selectedWordIndex != null && selectedWordIndex!! > index -> selectedWordIndex!! - 1
                                    else -> selectedWordIndex
                                }
                                deletedMaterialName = name
                                showDeleteNotification = true
                                showDeleteDialog = false
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
