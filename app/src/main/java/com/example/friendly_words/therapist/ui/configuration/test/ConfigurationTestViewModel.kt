package com.example.friendly_words.therapist.ui.configuration.test

import androidx.lifecycle.ViewModel
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
                is ConfigurationTestEvent.SetTimeCount -> state.copy(timeCount = event.count)
            }
        }
    }
}