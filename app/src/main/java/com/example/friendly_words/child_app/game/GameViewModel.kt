package com.example.friendly_words.child_app.game

import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.friendly_words.child_app.data.GameRound
import com.example.friendly_words.child_app.data.generateGameRounds
import com.example.shared.data.entities.LearningSettings
import com.example.shared.data.entities.toLearningSettings
import com.example.shared.data.repositories.ConfigurationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import jakarta.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val configurationRepository: ConfigurationRepository,
) : ViewModel() {

    // ---------- DANE GRY ----------
    var rounds = mutableStateOf<List<GameRound>>(emptyList())
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
    var showHint = mutableStateOf(false)

    // ---------- AKTYWNE USTAWIENIA ----------
    var activeLearningSettings = mutableStateOf<LearningSettings?>(null)

    // ---------- PODPOWIEDZI z LearningSettings ----------
    val dimIncorrect: State<Boolean> = derivedStateOf {
        activeLearningSettings.value?.typesOfHints?.contains("Wyszarz niepoprawne") == true
    }
    val scaleCorrect: State<Boolean> = derivedStateOf {
        activeLearningSettings.value?.typesOfHints?.contains("Powiększ poprawną") == true
    }
    val animateCorrect: State<Boolean> = derivedStateOf {
        activeLearningSettings.value?.typesOfHints?.contains("Animuj poprawną") == true
    }
    val outlineCorrect: State<Boolean> = derivedStateOf {
        activeLearningSettings.value?.typesOfHints?.contains("Obramuj poprawną") == true
    }

    // ---------- OBSERWOWANY COMMAND TYPE ----------
    val commandType: State<String> = derivedStateOf {
        when (activeLearningSettings.value?.commandType) {
            "SHORT" -> "{Słowo}"
            "WHERE_IS" -> "Gdzie jest {Słowo}"
            "SHOW_ME" -> "Pokaż gdzie jest {Słowo}"
            else -> "{Słowo}"
        }
    }

    // ---------- HISTORIA POZYCJI POPRAWNEJ OPCJI (do pseudolosowania) ----------
    private val correctPosHistory = mutableMapOf<String, MutableSet<Int>>()

    private fun keyFor(round: GameRound): String = round.correctItem.label

    // Zapisz aktualny indeks poprawnej opcji dla tej rundy (pozycji).
    fun noteCorrectPos(round: GameRound) {
        val idx = round.options.indexOf(round.correctItem)
        if (idx >= 0) {
            val key = keyFor(round)
            correctPosHistory.getOrPut(key) { mutableSetOf() }.add(idx)
        }
    }

    /**
     * Zwraca kopię rundy z potasowanymi opcjami tak, żeby poprawna opcja
     * NIE trafiła na żadną z wcześniej użytych pozycji (o ile to możliwe).
     * Wyjątek: gdy na ekranie jest 1 obrazek, nie wymuszamy zmiany pozycji.
     */
    fun shuffledRoundAvoidingPrevious(round: GameRound, displayedImages: Int): GameRound {
        if (displayedImages <= 1) return round // nic nie wymuszamy dla 1 kafelka

        val key = keyFor(round)
        val avoid = correctPosHistory[key] ?: emptySet()

        val options = round.options
        val correct = round.correctItem

        // jeśli zapełniliśmy wszystkie możliwe pozycje – i tak potasujemy "jakkolwiek"
        if (avoid.size >= options.size) {
            return round.copy(options = options.shuffled())
        }

        var best = options.shuffled()
        // spróbuj kilka razy znaleźć układ, w którym indeks poprawnej nie jest w "avoid"
        repeat(50) {
            val candidate = options.shuffled()
            val idx = candidate.indexOf(correct)
            if (idx !in avoid) {
                best = candidate
                return@repeat
            }
        }
        return round.copy(options = best)
    }

    init {
        viewModelScope.launch {
            configurationRepository.getActiveConfiguration().collect { config ->
                println("CONFIG FROM REPO: $config")

                if (config != null) {
                    val materialState = configurationRepository.getMaterialState(config.id)
                    val reinforcementState = configurationRepository.getReinforcementState(config.id)

                    activeLearningSettings.value = config.toLearningSettings(
                        materialState = materialState,
                        reinforcementState = reinforcementState
                    )
                    println("ACTIVE LEARNING SETTINGS: ${activeLearningSettings.value}")

                    rounds.value = generateGameRounds(configurationRepository)
                    println("ROUNDS GENERATED: ${rounds.value.size}")
                } else {
                    println("NO ACTIVE CONFIGURATION!")
                }
            }
        }
    }

    // ---------- FUNKCJA PRZEJŚCIA DO KOLEJNEJ RUNDY ----------
    fun goToNext(onGameFinished: (correct: Int, wrong: Int) -> Unit) {
        if (currentRoundIndex.value < rounds.value.lastIndex) {
            currentRoundIndex.value++
        } else {
            onGameFinished(correctAnswersCount.value, wrongAnswersCount.value)
            correctAnswersCount.value = 0
            wrongAnswersCount.value = 0
            currentRoundIndex.value = 0
            repeatStage.value = 0
            viewModelScope.launch {
                rounds.value = generateGameRounds(configurationRepository)
            }
        }

        // Reset stanu rundy przy przejściu do następnej
        answerJudged.value = false
        correctClicked.value = false
        hadMistakeThisRound.value = false
        showCongratsScreen.value = false
        goNextAfterCongrats.value = false
        showHint.value = false
    }
}
