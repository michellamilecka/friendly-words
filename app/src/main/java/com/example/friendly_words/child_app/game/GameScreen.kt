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
import com.example.friendly_words.R
import com.example.friendly_words.child_app.data.*
import com.example.friendly_words.child_app.main.ChildMainViewModel
import com.example.friendly_words.child_app.theme.Blue
import kotlinx.coroutines.delay
import java.util.*

@Composable
fun GameScreen(
    viewModel: ChildMainViewModel,
    onGameFinished: (correct: Int, wrong: Int) -> Unit
) {
    val resources by viewModel.resources.collectAsState()
    val isTestMode = GameSettings.isTestMode

    /* ---------- DANE GRY ---------- */
    var rounds by remember(resources) { mutableStateOf(generateGameRounds(resources)) }
    var currentRoundIndex by remember { mutableStateOf(0) }
    val currentRound = rounds.getOrNull(currentRoundIndex)
    val correctItem = currentRound?.correctItem

    var correctAnswersCount by remember { mutableStateOf(0) }
    var wrongAnswersCount by remember { mutableStateOf(0) }
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
    LaunchedEffect(currentRoundIndex, ttsReady) {
        correctItem?.let { item ->
            val instructionText = GameSettings.getInstructionText(item.label)
            if (ttsReady && !isTestMode) {
                tts.speak(instructionText, TextToSpeech.QUEUE_FLUSH, null, null)
            }
        }

        /* reset stanów */
        dimIncorrect = false; scaleCorrect = false
        animateCorrect = false; outlineCorrect = false
        answerJudged = false; correctClicked = false
        hadMistakeThisRound = false
        showCongratsScreen = false
        goNextAfterCongrats = false

        if (!isTestMode) {
            delay(StatesFromConfiguration.effectsDelayMillis)
            dimIncorrect = StatesFromConfiguration.enableDimIncorrect
            scaleCorrect = StatesFromConfiguration.enableScaleCorrect
            animateCorrect = StatesFromConfiguration.enableAnimateCorrect
            outlineCorrect = StatesFromConfiguration.enableOutlineCorrect
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
    fun handleAnswer(item: GameItem) {
        if (showCongratsScreen || correctItem == null) return

        val isCorrect = item == correctItem

        if (isTestMode) {
            if (!answerJudged) {
                if (isCorrect) correctAnswersCount++ else wrongAnswersCount++
                answerJudged = true
                goNextAfterCongrats = true
            }
            return
        }

        if (isCorrect && !correctClicked) {
            correctClicked = true
            if (!hadMistakeThisRound) correctAnswersCount++
            showCongratsScreen = true
        } else if (!isCorrect && !answerJudged) {
            wrongAnswersCount++
            hadMistakeThisRound = true
        }

        answerJudged = true
    }

    fun speakPraise(text: String) {
        if (ttsReady) tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    /* ---------- EKRAN GRATULACJI ---------- */
    if (showCongratsScreen && correctItem != null) {
        LaunchedEffect(showCongratsScreen) {
            if (showCongratsScreen) {
                currentPraise = GameSettings.praises.random()
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
                    0 -> if (hadMistakeThisRound) {
                        val list = rounds.toMutableList()
                        list.add(currentRoundIndex + 1, currentRound)
                        rounds = list
                        repeatStage = 1
                    }
                    1 -> {
                        val list = rounds.toMutableList()
                        if (hadMistakeThisRound) {
                            list.add(currentRoundIndex + 1, currentRound)
                        } else {
                            val shuffledRound = currentRound.copy(
                                options = currentRound.options.shuffled()
                            )
                            list.add(currentRoundIndex + 1, shuffledRound)
                            repeatStage = 2
                        }
                        rounds = list
                    }
                    2 -> {
                        val list = rounds.toMutableList()
                        if (hadMistakeThisRound) {
                            val shuffledRound = currentRound.copy(
                                options = currentRound.options.shuffled()
                            )
                            list.add(currentRoundIndex + 1, shuffledRound)
                        } else {
                            repeatStage = 0
                        }
                        rounds = list
                    }
                }
                hadMistakeThisRound = false
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
    if (!showCongratsScreen && currentRound != null) {
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
                    text = GameSettings.getInstructionText(currentRound.correctItem.label),
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
                            isDimmed = dimIncorrect && item != currentRound.correctItem,
                            isScaled = scaleCorrect && item == currentRound.correctItem,
                            animateCorrect = animateCorrect && item == currentRound.correctItem,
                            outlineCorrect = outlineCorrect && item == currentRound.correctItem,
                            onClick = { handleAnswer(item) }
                        )
                    }

                    Spacer(Modifier.weight(1f))
                }
            }
        }
    }
}
