package com.example.friendly_words

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.friendly_words.ui.theme.DarkBlue
import com.example.friendly_words.ui.theme.LightBlue

@Composable
fun MaterialsImagesScreen() {
    val availableImages = listOf(
        R.drawable.misiu_1,
        R.drawable.misiu_2,
        R.drawable.misiu_3
    )

    var images by remember { mutableStateOf(listOf<Int>()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Button(
                onClick = {
                    val randomImage = availableImages.random()
                    images = images + randomImage
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = DarkBlue)
            ) {
                Text("Dodaj", color = Color.White, fontSize = 18.sp)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 100.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(8.dp)
        ) {
            items(images) { imageRes ->
                Card(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .fillMaxSize(),
                    elevation = 4.dp,
                    backgroundColor = DarkBlue,
                    shape = MaterialTheme.shapes.medium
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = imageRes),
                            contentDescription = "Zdjęcie",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.fillMaxSize(0.95f)
                        )

                        IconButton(
                            onClick = {
                                images = images.filter { it != imageRes }
                            },
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .offset(x = (-4).dp, y = 4.dp)
                                .size(26.dp)
                                .background(
                                    color = LightBlue,
                                    shape = MaterialTheme.shapes.small
                                )
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Clear,
                                contentDescription = "Usuń zdjęcie",
                                tint = Color.White,
                                modifier = Modifier.fillMaxSize(0.8f)
                            )
                        }
                    }
                }
            }
        }
    }
}
