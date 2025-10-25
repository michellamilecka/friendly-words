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

@Composable
fun MainScreen(
    configurationDao: com.example.shared.data.daos.ConfigurationDao,
    onPlayClick: () -> Unit,
    canPlay: Boolean
) {
    val activeConfig by configurationDao.getActiveConfiguration().collectAsState(initial = null)
    val isTestMode = activeConfig?.activeMode == "test"
    val canPlayState = canPlay

    Box(
        modifier = Modifier.fillMaxSize().background(Blue)
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center).fillMaxWidth(0.9f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Przyjazne Słowa",
                fontSize = 50.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Aktywna konfiguracja: ${activeConfig?.name ?: "brak"} (tryb: ${if (isTestMode) "test" else "uczenie"})",
                fontSize = 20.sp,
                color = LightBlue
            )

            com.example.friendly_words.child_app.components.PlayButton(
                onClick = onPlayClick,
                enabled = canPlay
            )

            if (!canPlay) {
                Text(
                    text = "Brak materiałow w kroku uczenia. Dodaj materiały lub zmień konfigurację w aplikacji terapeuty.",
                    fontSize = 24.sp,
                    color = Color.White
                )
            }

        }
    }
}