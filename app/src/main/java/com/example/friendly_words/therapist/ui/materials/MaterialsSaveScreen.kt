package com.example.friendly_words.therapist.ui.materials

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.friendly_words.R
import com.example.friendly_words.therapist.ui.theme.DarkBlue

@Composable
fun MaterialsSaveScreen() {
    var resourceName by remember { mutableStateOf("") }
    val images = remember {
        listOf(
            R.drawable.misiu_1,
            R.drawable.misiu_2,
            R.drawable.misiu_3,
            R.drawable.misiu_1,
            R.drawable.misiu_1,
            R.drawable.misiu_1,
            R.drawable.misiu_1,
            R.drawable.misiu_1
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
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
                    modifier = Modifier
                        .fillMaxWidth(0.8f),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.White,
                        focusedIndicatorColor = DarkBlue,
                        unfocusedIndicatorColor = Color.Gray
                    )
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Dodane zdjęcia",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkBlue
                )

                Spacer(modifier = Modifier.height(16.dp))

                val groupedImages = images.chunked(3)

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                        .weight(1f)
                ) {
                    items(groupedImages) { group ->
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            group.forEach { imageRes ->
                                Card(
                                    backgroundColor = DarkBlue,
                                    elevation = 4.dp,
                                    shape = MaterialTheme.shapes.medium,
                                    modifier = Modifier
                                        .weight(1f)
                                        .aspectRatio(1f)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(4.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Image(
                                            painter = painterResource(id = imageRes),
                                            contentDescription = "Zdjęcie",
                                            contentScale = ContentScale.Fit,
                                            modifier = Modifier.fillMaxSize(0.98f)
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

