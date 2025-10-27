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
import com.example.friendly_words.child_app.components.RoundOptionsLayout
import com.example.friendly_words.child_app.data.GameItem
import com.example.friendly_words.child_app.theme.Blue
import kotlinx.coroutines.delay
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties

@Composable
fun CorrectAnswerScreen(
    correctItem: GameItem,
    displayWord: String,
    speakWordAndPraise: () -> Unit,
    onTimeout: () -> Unit,
    showLabels: Boolean,
    overlaySprites: List<Int> = emptyList(),
    overlayDirection: TravelDirection = TravelDirection.UP,
    overlayCount: Int = 20,
) {
    // --- Mówienie słowa i pochwały, potem przejście dalej ---
    LaunchedEffect(Unit) {
        speakWordAndPraise()
        delay(4000)
        onTimeout()
    }

    // --- Główne tło ekranu ---
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Blue)
    ) {
        // --- Centralny układ z tekstem i poprawną opcją ---
        Column(
            Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = displayWord,
                fontSize = 64.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(48.dp))

            RoundOptionsLayout(
                options = listOf(correctItem),
                numberOfItems = 1,
                isDimmed = { false },
                isScaled = { false },
                animateCorrect = { false },
                outlineCorrect = { false },
                showLabels = showLabels,
                onClick = {}
            )
        }

        // --- Warstwa animacji (np. balony, kwiatki, samochody) ---
        if (overlaySprites.isNotEmpty()) {
            Popup(
                alignment = Alignment.TopStart,
                properties = PopupProperties(
                    focusable = false,
                    usePlatformDefaultWidth = false
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Transparent)
                ) {
                    FloatingSpritesLayer(
                        sprites = overlaySprites,
                        count = overlayCount,
                        direction = overlayDirection,
                        baseTravelMs = when (overlayDirection) {
                            TravelDirection.RIGHT, TravelDirection.LEFT -> 4500 // samochody wolniejsze
                            else -> 2500
                        },
                        rotate = overlayDirection == TravelDirection.UP || overlayDirection == TravelDirection.DOWN
                    )
                }
            }
        }
    }
}
