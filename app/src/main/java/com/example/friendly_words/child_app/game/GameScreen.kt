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
import com.example.friendly_words.child_app.components.RoundOptionsLayout
import com.example.friendly_words.child_app.data.GameItem
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
    val showHint by viewModel.showHint

    if (rounds.isEmpty() || currentRoundIndex >= rounds.size) return

    val currentRound = rounds[currentRoundIndex]
    val correctItem = currentRound.correctItem
    val commandType = viewModel.commandType.value
    val commandText = commandType.replace("{Słowo}", correctItem.label)
    val displayedImages = viewModel.activeLearningSettings.value?.displayedImagesCount ?: 4
    val hintDelayMillis = ((viewModel.activeLearningSettings.value?.hintAfterSeconds ?: 1) * 1000L)
    val showLabels = viewModel.activeLearningSettings.value?.showLabelsUnderImages ?: true


    val context = LocalContext.current
    var ttsReady by remember { mutableStateOf(false) }
    var currentPraise by remember { mutableStateOf("") }
    val tts = remember {
        TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) ttsReady = true
        }
    }
    LaunchedEffect(ttsReady) {
        if (ttsReady) tts.language = Locale("pl", "PL")
    }

    // ---------- START NOWEJ RUNDY ----------
    LaunchedEffect(currentRoundIndex, ttsReady) {
        if (
            ttsReady &&
            !com.example.friendly_words.child_app.data.GameSettings.isTestMode &&
            (viewModel.activeLearningSettings.value?.readCommand == true)
        ) {
            tts.speak(commandText, TextToSpeech.QUEUE_FLUSH, null, null)
        }

        // reset logiki odpowiedzi
        viewModel.answerJudged.value = false
        viewModel.correctClicked.value = false
        viewModel.hadMistakeThisRound.value = false
        viewModel.showCongratsScreen.value = false
        viewModel.goNextAfterCongrats.value = false
        viewModel.showHint.value = false // reset podpowiedzi

        if (!com.example.friendly_words.child_app.data.GameSettings.isTestMode) {
            delay(hintDelayMillis)
            viewModel.showHint.value = true // pokazanie podpowiedzi po czasie
        }
    }

    DisposableEffect(Unit) { onDispose { tts.stop(); tts.shutdown() } }

    // ---------- LICZENIE BŁĘDÓW PO POKAZANIU PODPOWIEDZI ----------
    LaunchedEffect(showHint) {
        if (showHint && !viewModel.hadMistakeThisRound.value && !viewModel.correctClicked.value) {
            // Podpowiedź się pojawiła, a gracz nie kliknął poprawnie
            viewModel.wrongAnswersCount.value++
            viewModel.hadMistakeThisRound.value = true
            viewModel.answerJudged.value = true
        }
    }

    // ---------- OBSŁUGA KLIKNIĘĆ ----------
    fun handleAnswer(item: GameItem) {
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
            // Poprawna odpowiedź
            viewModel.correctClicked.value = true
            if (!viewModel.hadMistakeThisRound.value) viewModel.correctAnswersCount.value++
            viewModel.showCongratsScreen.value = true
            viewModel.answerJudged.value = true
        } else if (!isCorrect) {
            // Każde kliknięcie błędnego elementu zwiększa liczbę błędów
            viewModel.wrongAnswersCount.value++
            viewModel.hadMistakeThisRound.value = true
            // NIE ustawiamy answerJudged, żeby można było dalej klikać i powtarzać rundę
        }
    }

    fun speakPraise(text: String) {
        if (ttsReady) tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
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
                viewModel.goNextAfterCongrats.value = true

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
            }
        )
    }

    // ---------- PRZEJŚCIE DO KOLEJNEJ RUNDY ----------
    LaunchedEffect(viewModel.goNextAfterCongrats.value) {
        if (viewModel.goNextAfterCongrats.value) {
            viewModel.goNextAfterCongrats.value = false
            viewModel.showCongratsScreen.value = false
            viewModel.goToNext(onGameFinished)
        }
    }

    // ---------- UI RUNDY ----------
    if (!viewModel.showCongratsScreen.value && !viewModel.goNextAfterCongrats.value) {
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
                    text = commandText,
                    fontSize = 60.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(48.dp))

                RoundOptionsLayout(
                    options = currentRound.options.map { GameItem(it.label, it.imagePath) },
                    numberOfItems = displayedImages,
                    isDimmed = { item ->
                        viewModel.dimIncorrect.value &&
                                showHint && currentRound.options.any { it.label == item.label && it != currentRound.correctItem }
                    },
                    isScaled = { item ->
                        viewModel.scaleCorrect.value && showHint && currentRound.correctItem.label == item.label
                    },
                    animateCorrect = { item ->
                        viewModel.animateCorrect.value && showHint && currentRound.correctItem.label == item.label
                    },
                    outlineCorrect = { item ->
                        viewModel.outlineCorrect.value && showHint && currentRound.correctItem.label == item.label
                    },
                    showLabels = showLabels,
                    onClick = { item ->
                        currentRound.options.find { it.label == item.label }?.let { handleAnswer(it) }
                    }
                )
            }
        }
    }
}
