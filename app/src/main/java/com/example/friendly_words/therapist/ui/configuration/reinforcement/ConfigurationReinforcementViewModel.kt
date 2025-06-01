package com.example.friendly_words.therapist.ui.configuration.reinforcement

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ConfigurationReinforcementViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(ConfigurationReinforcementState())
    val state: StateFlow<ConfigurationReinforcementState> = _state

    fun onEvent(event: ConfigurationReinforcementEvent) {
        _state.update {
            reduce(it, event)
        }
    }

    companion object {
        fun reduce(
            state: ConfigurationReinforcementState,
            event: ConfigurationReinforcementEvent
        ): ConfigurationReinforcementState {
            return when (event) {
                is ConfigurationReinforcementEvent.TogglePraise -> {
                    val updated = state.praiseStates.toMutableMap()
                    updated[event.word] = event.enabled
                    state.copy(praiseStates = updated)
                }
                is ConfigurationReinforcementEvent.ToggleReading -> {
                    state.copy(praiseReadingEnabled = event.enabled)
                }
            }
        }
    }
}
