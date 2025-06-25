package com.example.child_app.ui.game

import android.speech.tts.TextToSpeech
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.child_app.ui.components.ImageOptionBox
import com.example.child_app.ui.data.GameItem
import com.example.child_app.ui.data.GameSettings
import com.example.child_app.ui.data.StatesFromConfiguration
import com.example.child_app.ui.data.generateGameRounds
import com.example.child_app.ui.theme.Blue
import kotlinx.coroutines.delay
import java.util.*

@Composable
fun GameScreen(onGameFinished: (correct: Int, wrong: Int) -> Unit) {
    val context = LocalContext.current
    val rounds = remember { generateGameRounds() }
    var currentRoundIndex by remember { mutableStateOf(0) }

    val currentRound = rounds[currentRoundIndex]
    val correctItem = currentRound.correctItem
    val instructionText = GameSettings.instructionType.getInstructionText(correctItem.label)

    var correctAnswersCount by remember { mutableStateOf(0) }
    var wrongAnswersCount by remember { mutableStateOf(0) }

    var showCongratsScreen by remember { mutableStateOf(false) }
    var goNextAfterCongrats by remember { mutableStateOf(false) }

    var answerJudged by remember(currentRoundIndex) { mutableStateOf(false) }
    var correctClicked by remember(currentRoundIndex) { mutableStateOf(false) }
    var wasIncorrectClick by remember(currentRoundIndex) { mutableStateOf(false) }
    var goToNextRound by remember(currentRoundIndex) { mutableStateOf(false) }

    var dimIncorrect by remember(currentRoundIndex) { mutableStateOf(false) }
    var scaleCorrect by remember(currentRoundIndex) { mutableStateOf(false) }
    var animateCorrect by remember(currentRoundIndex) { mutableStateOf(false) }
    var outlineCorrect by remember(currentRoundIndex) { mutableStateOf(false) }

    var ttsReady by remember { mutableStateOf(false) }

    val tts = remember {
        TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                ttsReady = true
            }
        }
    }

    LaunchedEffect(ttsReady) {
        if (ttsReady) {
            tts.language = Locale("pl", "PL")
        }
    }

    // Start rundy: mówimy instrukcję i uruchamiamy efekty, ale **nie blokujemy kliknięć**
    LaunchedEffect(currentRoundIndex, ttsReady) {
        if (ttsReady && !GameSettings.isTestMode) {
            tts.speak(instructionText, TextToSpeech.QUEUE_FLUSH, null, null)
        }

        // Reset stanów
        dimIncorrect = false
        scaleCorrect = false
        animateCorrect = false
        outlineCorrect = false
        answerJudged = false
        correctClicked = false
        wasIncorrectClick = false
        goToNextRound = false
        showCongratsScreen = false
        goNextAfterCongrats = false

        if (!GameSettings.isTestMode) {
            delay(StatesFromConfiguration.effectsDelayMillis)
        }

        // Aktywuj efekty (ale kliknięcia są możliwe cały czas)
        dimIncorrect = true
        scaleCorrect = true
        animateCorrect = true
        outlineCorrect = true
    }

    DisposableEffect(Unit) {
        onDispose {
            tts.stop()
            tts.shutdown()
        }
    }

    fun goToNext() {
        if (currentRoundIndex < rounds.lastIndex) {
            currentRoundIndex++
        } else {
            onGameFinished(correctAnswersCount, wrongAnswersCount)
        }
    }

    fun handleAnswer(item: GameItem) {
        if (showCongratsScreen) return // blokuj kliknięcia tylko podczas ekranu gratulacji

        val isCorrect = item == correctItem

        if (GameSettings.isTestMode) {
            if (!answerJudged) {
                if (isCorrect) correctAnswersCount++ else wrongAnswersCount++
                answerJudged = true
                goToNextRound = true
            }
            return
        }

        if (isCorrect && !correctClicked) {
            correctClicked = true
            if (!wasIncorrectClick) {
                correctAnswersCount++
            }
            showCongratsScreen = true
        } else if (!isCorrect && !answerJudged) {
            wrongAnswersCount++
            wasIncorrectClick = true
        }

        answerJudged = true
    }

    if (showCongratsScreen) {
        CorrectAnswerScreen(correctItem = correctItem) {
            showCongratsScreen = false
            goNextAfterCongrats = true
        }
    }

    LaunchedEffect(goNextAfterCongrats) {
        if (goNextAfterCongrats) {
            delay(300)
            goNextAfterCongrats = false
            goToNext()
        }
    }

    LaunchedEffect(goToNextRound) {
        if (goToNextRound) {
            delay(500)
            goToNextRound = false
            goToNext()
        }
    }

    // Wyświetlaj ekran gry jeśli nie na ekranie gratulacji
    if (!showCongratsScreen) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Blue)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
            ) {
                Text(
                    text = instructionText,
                    fontSize = 60.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(80.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(32.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                ) {
                    Spacer(modifier = Modifier.weight(1f))

                    currentRound.options.forEach { item ->
                        ImageOptionBox(
                            imageRes = item.imageRes,
                            label = item.label,
                            size = 350.dp,
                            isDimmed = dimIncorrect && item != correctItem,
                            isScaled = scaleCorrect && item == correctItem,
                            animateCorrect = animateCorrect && item == correctItem,
                            outlineCorrect = outlineCorrect && item == correctItem,
                            onClick = { handleAnswer(item) }
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}
