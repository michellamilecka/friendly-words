package com.example.friendly_words.therapist.ui.configuration.test

import androidx.lifecycle.ViewModel
import com.example.friendly_words.therapist.ui.configuration.learning.ConfigurationLearningEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ConfigurationTestViewModel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow(ConfigurationTestState())
    val state: StateFlow<ConfigurationTestState> = _state

    fun onEvent(event: ConfigurationTestEvent) {
        _state.update {
            reduce(it, event)
        }
    }

    companion object {
        fun reduce(state: ConfigurationTestState, event: ConfigurationTestEvent): ConfigurationTestState {
            return when (event) {
                is ConfigurationTestEvent.SetAttemptsCount -> state.copy(attemptsCount = event.count)
                is ConfigurationTestEvent.SetImageCount -> state.copy(imageCount = event.count)
                is ConfigurationTestEvent.SetEditEnabled -> state.copy(testEditEnabled = event.enabled)
                is ConfigurationTestEvent.ToggleTestEdit -> state.copy(testEditEnabled = !state.testEditEnabled)
                is ConfigurationTestEvent.SetPrompt -> state.copy(selectedPrompt = event.prompt)
                is ConfigurationTestEvent.ToggleCaptions -> state.copy(captionsEnabled = event.enabled)
                is ConfigurationTestEvent.ToggleReading -> state.copy(readingEnabled = event.enabled)
            }
        }
    }
}