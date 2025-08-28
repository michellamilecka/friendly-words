package com.example.child_app.ui.main

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
import com.example.child_app.ui.components.PlayButton
import com.example.child_app.ui.data.GameSettings
import com.example.child_app.ui.theme.Blue
import com.example.child_app.ui.theme.LightBlue

@Composable
fun MainScreen(onPlayClick: () -> Unit) {
    var isTestMode by remember { mutableStateOf(GameSettings.isTestMode) }

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
                text = "Aktywna konfiguracja: 1 konfiguracja NA STAŁE (tryb: ${if (isTestMode) "test" else "uczenie"})",
                fontSize = 20.sp,
                color = LightBlue
            )

            Spacer(modifier = Modifier.height(40.dp))

            PlayButton(onClick = onPlayClick)
        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (isTestMode) "Tryb: Test" else "Tryb: Uczenie",
                color = Color.White,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.width(12.dp))

            Switch(
                checked = isTestMode,
                onCheckedChange = {
                    isTestMode = it
                    GameSettings.isTestMode = it
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = LightBlue
                )
            )
        }
    }
}
