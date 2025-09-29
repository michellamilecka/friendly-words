package com.example.friendly_words.child_app.main

import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.friendly_words.child_app.game.GameScreen
import com.example.friendly_words.child_app.game.GameEndScreen
import com.example.friendly_words.child_app.game.GameViewModel
import kotlinx.coroutines.delay

@Composable
fun ScreenNavigationGame(viewModel: ChildMainViewModel) {
    val state by viewModel.state.collectAsState()

    when (state.screenState) {
        "info" -> {
            LaunchedEffect(Unit) {
                delay(5000L)
                viewModel.onEvent(ChildMainEvent.GoToNextScreen)
            }
            InformationScreen()
        }
        "main" -> {
            MainScreen(
                configurationDao = viewModel.configurationDao,
                onPlayClick = {
                    viewModel.onEvent(ChildMainEvent.GoToNextScreen)
                }
            )
        }
        "game" -> {
            val gameViewModel: GameViewModel = hiltViewModel()
            GameScreen(
                viewModel = gameViewModel,
                onGameFinished = { correct, wrong ->
                    viewModel.onEvent(ChildMainEvent.SetCorrectAnswers(correct))
                    viewModel.onEvent(ChildMainEvent.SetWrongAnswers(wrong))
                    viewModel.onEvent(ChildMainEvent.GoToNextScreen)
                }
            )
        }
        "end" -> {
            GameEndScreen(
                correctAnswers = state.correctAnswers,
                wrongAnswers = state.wrongAnswers,
                onPlayAgain = {
                    viewModel.onEvent(ChildMainEvent.GoToNextScreen)
                }
            )
        }
    }
}