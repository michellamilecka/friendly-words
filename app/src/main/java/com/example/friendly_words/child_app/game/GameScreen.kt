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
    LaunchedEffect(ttsReady) { if (ttsReady) tts.language = Locale("pl", "PL") }

    // ---------- START NOWEJ RUNDY ----------
    LaunchedEffect(currentRoundIndex, ttsReady) {
        if (
            ttsReady &&
            !com.example.friendly_words.child_app.data.GameSettings.isTestMode &&
            (viewModel.activeLearningSettings.value?.readCommand == true)
        ) {
            tts.speak(commandText, TextToSpeech.QUEUE_FLUSH, null, null)
        }

        // reset stanu
        viewModel.answerJudged.value = false
        viewModel.correctClicked.value = false
        viewModel.hadMistakeThisRound.value = false
        viewModel.showCongratsScreen.value = false
        viewModel.goNextAfterCongrats.value = false
        viewModel.showHint.value = false

        // Timer podpowiedzi: pokaż tylko jeśli do tego czasu nie było poprawnego kliknięcia
        // i podpowiedź nie została już wymuszona błędnym kliknięciem. // NEW
        if (!com.example.friendly_words.child_app.data.GameSettings.isTestMode) {
            delay(hintDelayMillis)
            if (!viewModel.correctClicked.value &&
                !viewModel.answerJudged.value &&
                !viewModel.showHint.value
            ) {
                viewModel.showHint.value = true
            }
        }
    }

    DisposableEffect(Unit) { onDispose { tts.stop(); tts.shutdown() } }

    // ---------- LICZENIE BŁĘDÓW PO POKAZANIU PODPOWIEDZI ----------
    LaunchedEffect(showHint) {
        if (showHint && !viewModel.hadMistakeThisRound.value && !viewModel.correctClicked.value) {
            // Podpowiedź pojawiła się bez wcześniejszego złego kliku -> policz błąd (raz).
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
            // poprawna odpowiedź
            viewModel.correctClicked.value = true
            if (!viewModel.hadMistakeThisRound.value) viewModel.correctAnswersCount.value++
            viewModel.showCongratsScreen.value = true
            viewModel.answerJudged.value = true
        } else if (!isCorrect) {
            // błędne kliknięcie -> licz błąd i natychmiast pokaż podpowiedź, jeśli jeszcze nie widać // NEW
            viewModel.wrongAnswersCount.value++
            viewModel.hadMistakeThisRound.value = true
            if (!viewModel.showHint.value) {
                viewModel.showHint.value = true
            }
            // nie ustawiamy answerJudged, aby pozwolić na dalsze próby w tej rundzie,
            // ale i tak runda wejdzie w ścieżkę powtórzeń przez hadMistakeThisRound
        }
    }

    fun speakPraise(text: String) {
        if (ttsReady) tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    // ---------- EKRAN GRATULACJI ----------
    if (viewModel.showCongratsScreen.value) {
        LaunchedEffect(viewModel.showCongratsScreen.value) {
            if (viewModel.showCongratsScreen.value) {
                val praises = viewModel.activeLearningSettings.value?.typesOfPraises ?: emptyList()
                if (praises.isNotEmpty()) {
                    currentPraise = praises.random()
                    speakPraise(currentPraise)
                } else {
                    currentPraise = "" // brak pochwał w bazie -> nie mówimy nic
                }
            }
        }

        CorrectAnswerScreen(
            correctItem = correctItem,
            praiseText = currentPraise,
            speakPraise = { if (currentPraise.isNotBlank()) speakPraise(currentPraise) },
            onTimeout = {
                viewModel.goNextAfterCongrats.value = true
                val roundsList = rounds.toMutableList()
                when (viewModel.repeatStage.value) {
                    0 -> {
                        if (viewModel.hadMistakeThisRound.value) {
                            roundsList.add(currentRoundIndex + 1, currentRound)
                            viewModel.rounds.value = roundsList
                            viewModel.repeatStage.value = 1
                        }
                    }
                    1 -> {
                        if (viewModel.hadMistakeThisRound.value) {
                            roundsList.add(currentRoundIndex + 1, currentRound)
                            viewModel.rounds.value = roundsList
                        } else {
                            val shuffledRound = currentRound.copy(options = currentRound.options.shuffled())
                            roundsList.add(currentRoundIndex + 1, shuffledRound)
                            viewModel.rounds.value = roundsList
                            viewModel.repeatStage.value = 2
                        }
                    }
                    2 -> {
                        if (viewModel.hadMistakeThisRound.value) {
                            val shuffledRound = currentRound.copy(options = currentRound.options.shuffled())
                            roundsList.add(currentRoundIndex + 1, shuffledRound)
                            viewModel.rounds.value = roundsList
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
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
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
