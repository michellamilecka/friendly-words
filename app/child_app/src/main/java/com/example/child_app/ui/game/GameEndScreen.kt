package com.example.child_app.ui.game

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.child_app.ui.components.PlayButton
import com.example.child_app.ui.theme.Blue

@Composable
fun GameEndScreen(
    correctAnswers: Int,
    wrongAnswers: Int,
    onPlayAgain: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Blue),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Koniec gry!",
                fontSize = 64.sp,
                color = Color.White,
                modifier = Modifier.padding(bottom = 20.dp)
            )

            Text(
                text = "😄",
                fontSize = 250.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Poprawne odpowiedzi: $correctAnswers",
                fontSize = 24.sp,
                color = Color.White,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Text(
                text = "Niepoprawne odpowiedzi: $wrongAnswers",
                fontSize = 24.sp,
                color = Color.White,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            PlayButton(
                size = 150.dp,
                onClick = onPlayAgain
            )
        }
    }
}

