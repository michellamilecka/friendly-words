package com.example.friendly_words.child_app.game

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.friendly_words.child_app.data.GameItem
import com.example.friendly_words.child_app.theme.Blue
import kotlinx.coroutines.delay

@Composable
fun CorrectAnswerScreen(
    correctItem: GameItem,
    praiseText: String,
    speakPraise: () -> Unit,
    onTimeout: () -> Unit
) {
    // uruchamiamy TTS i timer
    LaunchedEffect(Unit) {
        speakPraise()
        delay(3000)
        onTimeout()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Blue)
    ) {
        Column(
            Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = praiseText,
                fontSize = 64.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(48.dp))

            com.example.friendly_words.child_app.components.ImageOptionBox(
                imagePath = correctItem.imagePath,
                label = correctItem.label,
                size = 500.dp,
                isDimmed = false,
                isScaled = false,
                animateCorrect = false,
                outlineCorrect = false,
                onClick = {}
            )
        }
    }
}


