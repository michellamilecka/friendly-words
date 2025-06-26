package com.example.child_app.ui.game

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
import com.example.child_app.ui.components.ImageOptionBox
import com.example.child_app.ui.data.GameItem
import com.example.child_app.ui.theme.Blue
import kotlinx.coroutines.delay
import com.example.child_app.ui.data.GameSettings

@Composable
fun CorrectAnswerScreen(
    correctItem: GameItem,
    praiseText: String,          // ← przekazujemy wylosowaną pochwałę
    speakPraise: () -> Unit,     // ← lambda, która odtwarza TTS
    onTimeout: () -> Unit
) {
    // uruchamiamy TTS i timer tylko raz
    LaunchedEffect(Unit) {
        speakPraise()            // ← mówimy pochwałę
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

            ImageOptionBox(
                imageRes = correctItem.imageRes,
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


