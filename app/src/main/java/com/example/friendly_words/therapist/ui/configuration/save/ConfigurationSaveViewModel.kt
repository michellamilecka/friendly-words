package com.example.friendly_words.therapist.ui.configuration.save

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ConfigurationSaveViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(ConfigurationSaveState())
    val state: StateFlow<ConfigurationSaveState> = _state

    fun onEvent(event: ConfigurationSaveEvent) {
        _state.update {
            reduce(it, event)
        }
    }

    companion object {
        fun reduce(state: ConfigurationSaveState, event: ConfigurationSaveEvent): ConfigurationSaveState {
            return when (event) {
                is ConfigurationSaveEvent.SetStepName -> state.copy(stepName = event.name)
                is ConfigurationSaveEvent.SaveConfiguration -> state // nic nie zmieniamy, ale wymagane
            }
        }
    }
}