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
import com.example.friendly_words.child_app.components.OptionData
import com.example.friendly_words.child_app.components.RoundOptionsLayout
import com.example.friendly_words.child_app.data.GameSettings
import com.example.friendly_words.child_app.theme.Blue
import kotlinx.coroutines.delay
import java.util.*

@Composable
fun GameScreen(
    viewModel: GameViewModel,
    onGameFinished: (correct: Int, wrong: Int) -> Unit
) {
    val currentRoundIndex by viewModel.currentRoundIndex
    val rounds by viewModel.rounds
    val currentRound = rounds[currentRoundIndex]
    val correctItem = currentRound.correctItem
    val instructionText = com.example.friendly_words.child_app.data.GameSettings.instructionType.getInstructionText(correctItem.label)

    val context = LocalContext.current
    var ttsReady by remember { mutableStateOf(false) }
    var currentPraise by remember { mutableStateOf("") }
    val tts = remember {
        TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) ttsReady = true
        }
    }
    LaunchedEffect(ttsReady) { if (ttsReady) tts.language = Locale("pl", "PL") }

    // ---------- START NOWEJ RUNDY ----------
    LaunchedEffect(currentRoundIndex, ttsReady) {
        if (ttsReady && !com.example.friendly_words.child_app.data.GameSettings.isTestMode) {
            tts.speak(instructionText, TextToSpeech.QUEUE_FLUSH, null, null)
        }

        viewModel.dimIncorrect.value = false
        viewModel.scaleCorrect.value = false
        viewModel.animateCorrect.value = false
        viewModel.outlineCorrect.value = false
        viewModel.answerJudged.value = false
        viewModel.correctClicked.value = false
        viewModel.hadMistakeThisRound.value = false
        viewModel.showCongratsScreen.value = false
        viewModel.goNextAfterCongrats.value = false

        if (!com.example.friendly_words.child_app.data.GameSettings.isTestMode) {
            delay(com.example.friendly_words.child_app.data.StatesFromConfiguration.effectsDelayMillis)
            viewModel.dimIncorrect.value = true
            viewModel.scaleCorrect.value = true
            viewModel.animateCorrect.value = true
            viewModel.outlineCorrect.value = true
        }
    }

    DisposableEffect(Unit) {
        onDispose { tts.stop(); tts.shutdown() }
    }

    // ---------- OBSŁUGA KLIKNIĘĆ ----------
    fun handleAnswer(item: com.example.friendly_words.child_app.data.GameItem) {
        if (viewModel.showCongratsScreen.value) return

        val isCorrect = item == correctItem

        if (com.example.friendly_words.child_app.data.GameSettings.isTestMode) {
            if (!viewModel.answerJudged.value) {
                if (isCorrect) viewModel.correctAnswersCount.value++ else viewModel.wrongAnswersCount.value++
                viewModel.answerJudged.value = true
                viewModel.goNextAfterCongrats.value = true
            }
            return
        }

        if (isCorrect && !viewModel.correctClicked.value) {
            viewModel.correctClicked.value = true
            if (!viewModel.hadMistakeThisRound.value) viewModel.correctAnswersCount.value++
            viewModel.showCongratsScreen.value = true
        } else if (!isCorrect && !viewModel.answerJudged.value) {
            viewModel.wrongAnswersCount.value++
            viewModel.hadMistakeThisRound.value = true
        }

        viewModel.answerJudged.value = true
    }

    fun speakPraise(text: String) {
        if (ttsReady) {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
        }
    }

    // ---------- EKRAN GRATULACJI ----------
    if (viewModel.showCongratsScreen.value) {
        LaunchedEffect(viewModel.showCongratsScreen.value) {
            if (viewModel.showCongratsScreen.value) {
                currentPraise = com.example.friendly_words.child_app.data.GameSettings.praises.random()
                speakPraise(currentPraise)
            }
        }

        CorrectAnswerScreen(
            correctItem = correctItem,
            praiseText = currentPraise,
            speakPraise = { speakPraise(currentPraise) },
            onTimeout = {
                viewModel.showCongratsScreen.value = false

                when (viewModel.repeatStage.value) {
                    0 -> {
                        if (viewModel.hadMistakeThisRound.value) {
                            val list = rounds.toMutableList()
                            list.add(currentRoundIndex + 1, currentRound)
                            viewModel.rounds.value = list
                            viewModel.repeatStage.value = 1
                        }
                    }
                    1 -> {
                        if (viewModel.hadMistakeThisRound.value) {
                            val list = rounds.toMutableList()
                            list.add(currentRoundIndex + 1, currentRound)
                            viewModel.rounds.value = list
                        } else {
                            val shuffledRound = currentRound.copy(options = currentRound.options.shuffled())
                            val list = rounds.toMutableList()
                            list.add(currentRoundIndex + 1, shuffledRound)
                            viewModel.rounds.value = list
                            viewModel.repeatStage.value = 2
                        }
                    }
                    2 -> {
                        if (viewModel.hadMistakeThisRound.value) {
                            val shuffledRound = currentRound.copy(options = currentRound.options.shuffled())
                            val list = rounds.toMutableList()
                            list.add(currentRoundIndex + 1, shuffledRound)
                            viewModel.rounds.value = list
                        } else {
                            viewModel.repeatStage.value = 0
                        }
                    }
                }

                viewModel.hadMistakeThisRound.value = false
                viewModel.goNextAfterCongrats.value = true
            }
        )
    }

    // ---------- PRZEJŚCIE DO KOLEJNEJ RUNDY ----------
    LaunchedEffect(viewModel.goNextAfterCongrats.value) {
        if (viewModel.goNextAfterCongrats.value) {
            delay(300)
            viewModel.goNextAfterCongrats.value = false
            viewModel.goToNext(onGameFinished)
        }
    }

    // ---------- UI RUNDY ----------
    if (!viewModel.showCongratsScreen.value) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Blue)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize().padding(24.dp)
            ) {
                Text(
                    text = instructionText,
                    fontSize = 60.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(80.dp))

                RoundOptionsLayout(
                    options = currentRound.options.map { OptionData(it.imageRes, it.label) },
                    numberOfItems = GameSettings.numberOfPicturesPerRound,
                    isDimmed = { item -> viewModel.dimIncorrect.value && currentRound.options.any { it.label == item.label && it != currentRound.correctItem } },
                    isScaled = { item -> viewModel.scaleCorrect.value && currentRound.correctItem.label == item.label },
                    animateCorrect = { item -> viewModel.animateCorrect.value && currentRound.correctItem.label == item.label },
                    outlineCorrect = { item -> viewModel.outlineCorrect.value && currentRound.correctItem.label == item.label },
                    onClick = { item ->
                        currentRound.options.find { it.label == item.label }?.let { handleAnswer(it) }
                    }
                )

            }
        }
    }
}
