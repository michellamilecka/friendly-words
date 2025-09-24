package com.example.friendly_words.child_app.main

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ChildMainViewModel @Inject constructor(
    val configurationDao: com.example.shared.data.daos.ConfigurationDao
) : ViewModel() {

    private val _state = MutableStateFlow(ChildMainState())
    val state: StateFlow<ChildMainState> = _state

    fun onEvent(event: ChildMainEvent) {
        _state.update { currentState ->
            reduce(currentState, event)
        }
    }

    private fun reduce(state: ChildMainState, event: ChildMainEvent): ChildMainState {
        return when (event) {
            is ChildMainEvent.GoToNextScreen -> state.copy(
                screenState = when (state.screenState) {
                    "info" -> "main"
                    "main" -> "game"
                    "game" -> "end"
                    "end" -> "main"
                    else -> "info"
                }
            )
            is ChildMainEvent.SetCorrectAnswers -> state.copy(correctAnswers = event.correct)
            is ChildMainEvent.SetWrongAnswers -> state.copy(wrongAnswers = event.wrong)
        }
    }
}