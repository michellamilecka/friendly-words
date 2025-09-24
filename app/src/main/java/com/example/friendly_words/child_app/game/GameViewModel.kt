package com.example.friendly_words.child_app.game

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.friendly_words.child_app.data.generateGameRounds

class GameViewModel : ViewModel() {

    // ---------- DANE GRY ----------
    var rounds = mutableStateOf(generateGameRounds())
    var currentRoundIndex = mutableStateOf(0)
    var correctAnswersCount = mutableStateOf(0)
    var wrongAnswersCount = mutableStateOf(0)
    var repeatStage = mutableStateOf(0)

    // ---------- STANY UI ----------
    var showCongratsScreen = mutableStateOf(false)
    var goNextAfterCongrats = mutableStateOf(false)
    var answerJudged = mutableStateOf(false)
    var correctClicked = mutableStateOf(false)
    var hadMistakeThisRound = mutableStateOf(false)

    var dimIncorrect = mutableStateOf(false)
    var scaleCorrect = mutableStateOf(false)
    var animateCorrect = mutableStateOf(false)
    var outlineCorrect = mutableStateOf(false)

    // Funkcja do przejścia do następnej rundy
    fun goToNext(onGameFinished: (correct: Int, wrong: Int) -> Unit) {
        if (currentRoundIndex.value < rounds.value.lastIndex) {
            currentRoundIndex.value++
        } else {
            // wywołaj callback z aktualnymi wynikami
            onGameFinished(correctAnswersCount.value, wrongAnswersCount.value)

            // reset wyników i rundy
            correctAnswersCount.value = 0
            wrongAnswersCount.value = 0
            currentRoundIndex.value = 0
            repeatStage.value = 0
            rounds.value = generateGameRounds()
        }
    }

}
