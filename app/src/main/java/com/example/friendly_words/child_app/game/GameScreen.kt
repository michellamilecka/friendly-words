package com.example.friendly_words.child_app.game

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
import com.example.friendly_words.child_app.theme.Blue
import kotlinx.coroutines.delay
import java.util.*

@Composable
fun GameScreen(onGameFinished: (correct: Int, wrong: Int) -> Unit) {

    /* ---------- DANE GRY ---------- */

    var rounds by remember { mutableStateOf(com.example.friendly_words.child_app.data.generateGameRounds()) }
    var currentRoundIndex by remember { mutableStateOf(0) }
    val currentRound = rounds[currentRoundIndex]
    val correctItem = currentRound.correctItem
    val instructionText = com.example.friendly_words.child_app.data.GameSettings.instructionType.getInstructionText(correctItem.label)

    var correctAnswersCount by remember { mutableStateOf(0) }
    var wrongAnswersCount by remember { mutableStateOf(0) }

    /**
     * repeatStage:
     * 0 – runda pierwotna
     * 1 – powtórka z identycznym układem (po błędzie)
     * 2 – powtórka z przetasowanym układem (po poprawnej odpowiedzi w etapie 1)
     */
    var repeatStage by remember { mutableStateOf(0) }

    /* ---------- STANY UI ---------- */

    var showCongratsScreen by remember { mutableStateOf(false) }
    var goNextAfterCongrats by remember { mutableStateOf(false) }

    var answerJudged by remember(currentRoundIndex) { mutableStateOf(false) }
    var correctClicked by remember(currentRoundIndex) { mutableStateOf(false) }
    var hadMistakeThisRound by remember(currentRoundIndex) { mutableStateOf(false) }

    var dimIncorrect by remember(currentRoundIndex) { mutableStateOf(false) }
    var scaleCorrect by remember(currentRoundIndex) { mutableStateOf(false) }
    var animateCorrect by remember(currentRoundIndex) { mutableStateOf(false) }
    var outlineCorrect by remember(currentRoundIndex) { mutableStateOf(false) }

    /* ---------- TTS ---------- */

    val context = LocalContext.current
    var ttsReady by remember { mutableStateOf(false) }
    var currentPraise by remember { mutableStateOf("") }
    val tts = remember {
        TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) ttsReady = true
        }
    }
    LaunchedEffect(ttsReady) { if (ttsReady) tts.language = Locale("pl", "PL") }

    /* ---------- START NOWEJ RUNDY ---------- */

    /* ---------- START NOWEJ RUNDY ---------- */

    LaunchedEffect(currentRoundIndex, ttsReady) {
        if (ttsReady && !com.example.friendly_words.child_app.data.GameSettings.isTestMode) {
            tts.speak(instructionText, TextToSpeech.QUEUE_FLUSH, null, null)
        }

        /* reset stanów */
        dimIncorrect = false; scaleCorrect = false
        animateCorrect = false; outlineCorrect = false
        answerJudged = false; correctClicked = false
        hadMistakeThisRound = false
        showCongratsScreen = false
        goNextAfterCongrats = false

        if (!com.example.friendly_words.child_app.data.GameSettings.isTestMode) {
            delay(com.example.friendly_words.child_app.data.StatesFromConfiguration.effectsDelayMillis)

            /* aktywujemy efekty – TYLKO poza testem */
            dimIncorrect = true
            scaleCorrect = true
            animateCorrect = true
            outlineCorrect = true
        }
    }


    DisposableEffect(Unit) {
        onDispose { tts.stop(); tts.shutdown() }
    }

    /* ---------- NAWIGACJA ---------- */

    fun goToNext() {
        if (currentRoundIndex < rounds.lastIndex) {
            currentRoundIndex++
        } else {
            onGameFinished(correctAnswersCount, wrongAnswersCount)
        }
    }

    /* ---------- OBSŁUGA KLIKNIĘĆ ---------- */

    fun handleAnswer(item: com.example.friendly_words.child_app.data.GameItem) {
        if (showCongratsScreen) return            // blokada w czasie ekranu grat.

        val isCorrect = item == correctItem

        if (com.example.friendly_words.child_app.data.GameSettings.isTestMode) {
            if (!answerJudged) {
                if (isCorrect) correctAnswersCount++ else wrongAnswersCount++
                answerJudged = true
                goNextAfterCongrats = true
            }
            return
        }

        if (isCorrect && !correctClicked) {
            correctClicked = true
            if (!hadMistakeThisRound) correctAnswersCount++ // punkt tylko jeśli bezbłędnie
            showCongratsScreen = true                       // ekran tylko po poprawnym kliknięciu
        } else if (!isCorrect && !answerJudged) {
            wrongAnswersCount++
            hadMistakeThisRound = true                      // odnotuj błąd w TEJ rundzie
        }

        answerJudged = true
    }

    fun speakPraise(text: String) {
        if (ttsReady) {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
        }
    }

    /* ---------- EKRAN GRATULACJI ---------- */

    if (showCongratsScreen) {
        LaunchedEffect(showCongratsScreen) {
            if (showCongratsScreen) {
                currentPraise = com.example.friendly_words.child_app.data.GameSettings.praises.random()
                speakPraise(currentPraise)
            }
        }

        CorrectAnswerScreen(
            correctItem = correctItem,
            praiseText = currentPraise,
            speakPraise = { speakPraise(currentPraise) },
            onTimeout = {
                showCongratsScreen = false

                when (repeatStage) {
                    0 -> {  // oryginalna runda
                        if (hadMistakeThisRound) {
                            // dodaj powtórkę z IDENTYCZNYM układem
                            val list = rounds.toMutableList()
                            list.add(currentRoundIndex + 1, currentRound)
                            rounds = list
                            repeatStage = 1
                        }
                    }

                    1 -> {  // powtórka z identycznym układem
                        if (hadMistakeThisRound) {
                            // znów błąd → kolejna powtórka identyczna
                            val list = rounds.toMutableList()
                            list.add(currentRoundIndex + 1, currentRound)
                            rounds = list
                            // repeatStage zostaje 1
                        } else {
                            // bez błędu → dokładamy przetasowany układ
                            val shuffledRound = currentRound.copy(
                                options = currentRound.options.shuffled()
                            )
                            val list = rounds.toMutableList()
                            list.add(currentRoundIndex + 1, shuffledRound)
                            rounds = list
                            repeatStage = 2
                        }
                    }

                    2 -> {  // powtórka z przetasowanym układem
                        if (hadMistakeThisRound) {
                            // błąd → kolejna przetasowana powtórka
                            val shuffledRound = currentRound.copy(
                                options = currentRound.options.shuffled()
                            )
                            val list = rounds.toMutableList()
                            list.add(currentRoundIndex + 1, shuffledRound)
                            rounds = list
                            // repeatStage pozostaje 2
                        } else {
                            // bezbłędnie → wracamy do trybu normalnego
                            repeatStage = 0
                        }
                    }
                }

                hadMistakeThisRound = false     // zresetuj przed kolejną rundą
                goNextAfterCongrats = true
            }
        )
    }


    /* ---------- PRZEJŚCIE DO KOLEJNEJ RUNDY ---------- */

    LaunchedEffect(goNextAfterCongrats) {
        if (goNextAfterCongrats) {
            delay(300)
            goNextAfterCongrats = false
            goToNext()
        }
    }

    /* ---------- UI RUNDY ---------- */

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

                Spacer(Modifier.height(80.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(32.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                ) {
                    Spacer(Modifier.weight(1f))

                    currentRound.options.forEach { item ->
                        com.example.friendly_words.child_app.components.ImageOptionBox(
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

                    Spacer(Modifier.weight(1f))
                }
            }
        }
    }
}
