package com.example.friendly_words

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MainScreen() {
    var currentScreen by remember { mutableStateOf("main") }

    when (currentScreen) {
        "main" -> MainContent(
            onConfigClick = { currentScreen = "config" }
        )
        "config" -> ConfigurationsListScreen(
            onBackClick = { currentScreen = "main" }
        )
    }
}
@Composable
fun MainContent(onConfigClick: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.height(95.dp),
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            "Przyjazne Słowa Ustawienia",
                            fontSize = 30.sp,
                            modifier = Modifier.weight(1f),
                            color = Color.White
                        )
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
                .padding(top = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(200.dp))
            Button(
                onClick =  onConfigClick,
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xFFA1CDE1),
                    contentColor = Color.Black
                ),
                modifier = Modifier
                    .width(360.dp)
                    .height(70.dp)
            ) {
                Text(
                    text = "KONFIGURACJE",
                    fontSize = 15.sp,

                )
            }

            Button(
                onClick = { /* Akcja dla "DODAJ MATERIAŁY" */ },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xFFA1CDE1),
                    contentColor = Color.Black
                ),
                modifier = Modifier
                    .width(360.dp)
                    .height(70.dp)
            ) {
                Text(
                    text = "DODAJ MATERIAŁY",
                    fontSize = 15.sp,

                )
            }
        }
    }
}
