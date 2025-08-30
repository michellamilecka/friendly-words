package com.example.friendly_words.child_app.main

import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.friendly_words.child_app.game.GameEndScreen
import com.example.friendly_words.child_app.game.GameScreen
import com.example.friendly_words.therapist.ui.main.InformationScreen
import kotlinx.coroutines.delay

@Composable
fun ChildMainScreen(viewModel: ChildMainViewModel = hiltViewModel()) {
    val screen by viewModel.screenState.collectAsState()
    val activeConfig by viewModel.activeConfig.collectAsState()
    val correct by viewModel.correctAnswers.collectAsState()
    val wrong by viewModel.wrongAnswers.collectAsState()
    val isTestMode by viewModel.isTestMode.collectAsState()

    when (screen) {
        "info" -> {
            LaunchedEffect(Unit) {
                delay(5000)
                viewModel.navigateTo("main")
            }
            InformationScreen()
        }
        "main" -> {
            MainScreen(
                onPlayClick = { viewModel.navigateTo("game") },
                activeConfig = activeConfig,
                isTestMode = isTestMode
            )
        }
        "game" -> {
            GameScreen(viewModel = viewModel,onGameFinished = { c, w -> viewModel.finishGame(c, w) })
        }
        "end" -> {
            GameEndScreen(
                correctAnswers = correct,
                wrongAnswers = wrong,
                onPlayAgain = { viewModel.resetGame() }
            )
        }
    }
}
