package com.example.friendly_words.child_app.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.friendly_words.child_app.theme.Blue
import com.example.friendly_words.child_app.theme.LightBlue
import com.example.shared.data.entities.Configuration

@Composable
fun MainScreen(
    onPlayClick: () -> Unit,
    activeConfig: Configuration?,
    isTestMode: Boolean
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Blue)
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth(0.9f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(
                text = "Przyjazne Słowa",
                fontSize = 50.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Aktywna konfiguracja: ${activeConfig?.name ?: "brak"} " +
                        "(tryb: ${if (isTestMode) "test" else "uczenie"})",
                fontSize = 20.sp,
                color = LightBlue
            )

            Spacer(modifier = Modifier.height(40.dp))

            com.example.friendly_words.child_app.components.PlayButton(onClick = onPlayClick)
        }
    }
}

