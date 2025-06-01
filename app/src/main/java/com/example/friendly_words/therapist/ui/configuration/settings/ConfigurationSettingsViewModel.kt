package com.example.friendly_words.therapist.ui.configuration.settings

import androidx.lifecycle.ViewModel
import com.example.friendly_words.therapist.ui.configuration.learning.*
import com.example.friendly_words.therapist.ui.configuration.material.*
import com.example.friendly_words.therapist.ui.configuration.reinforcement.ConfigurationReinforcementViewModel
import com.example.friendly_words.therapist.ui.configuration.save.ConfigurationSaveViewModel
import com.example.friendly_words.therapist.ui.configuration.test.ConfigurationTestViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ConfigurationSettingsViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(ConfigurationSettingsState())
    val state: StateFlow<ConfigurationSettingsState> = _state

    fun onEvent(event: ConfigurationSettingsEvent) {
        when (event) {
            is ConfigurationSettingsEvent.Learning -> {
                _state.update { current ->
                    current.copy(
                        learningState = ConfigurationLearningViewModel.reduce(
                            current.learningState,
                            event.event
                        )
                    )
                }
            }

            is ConfigurationSettingsEvent.Material -> {
                _state.update { current ->
                    current.copy(
                        materialState = ConfigurationMaterialViewModel.reduce(
                            current.materialState,
                            event.event
                        )
                    )
                }
            }
            is ConfigurationSettingsEvent.Reinforcement -> {
                _state.update { current ->
                    current.copy(
                        reinforcementState = ConfigurationReinforcementViewModel.reduce(
                            current.reinforcementState,
                            event.event
                        )
                    )
                }
            }
            is ConfigurationSettingsEvent.Test -> {
                _state.update { current ->
                    current.copy(
                        testState = ConfigurationTestViewModel.reduce(
                            current.testState,
                            event.event
                        )
                    )
                }
            }
            is ConfigurationSettingsEvent.Save -> {
                // Handle save event, e.g., save settings to a database or shared preferences
                // This is a placeholder for actual save logic
                _state.update { current ->
                    current.copy(
                       saveState = ConfigurationSaveViewModel.reduce(
                            current.saveState,
                            event.event
                        )
                    )
                }
            }

        }
    }

}
