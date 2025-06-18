package com.example.friendly_words.therapist.ui.configuration.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.friendly_words.data.entities.Configuration
import com.example.friendly_words.data.repositories.ConfigurationRepository
import com.example.friendly_words.therapist.ui.configuration.learning.*
import com.example.friendly_words.therapist.ui.configuration.material.*
import com.example.friendly_words.therapist.ui.configuration.reinforcement.ConfigurationReinforcementViewModel
import com.example.friendly_words.therapist.ui.configuration.save.ConfigurationSaveEvent
import com.example.friendly_words.therapist.ui.configuration.save.ConfigurationSaveViewModel
import com.example.friendly_words.therapist.ui.configuration.test.ConfigurationTestViewModel
import com.example.friendly_words.therapist.ui.configuration.test.toTestSettings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConfigurationSettingsViewModel @Inject constructor(
    private val configurationRepository: ConfigurationRepository
) : ViewModel() {

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
                when (val saveEvent = event.event) {
                    is ConfigurationSaveEvent.SaveConfiguration -> {
                        viewModelScope.launch {
                            val currentState = state.value

                            val learningSettings = currentState.learningState.toLearningSettings(
                                materialState = currentState.materialState,
                                reinforcementState = currentState.reinforcementState
                            )

                            val testSettings = currentState.testState.toTestSettings(
                                materialState = currentState.materialState
                            )

                            val configuration = Configuration(
                                name = saveEvent.name,
                                isExample = false,
                                learningSettings = learningSettings,
                                testSettings = testSettings
                            )

                            configurationRepository.insert(configuration)
                        }
                    }

                    else -> {
                        _state.update {
                            it.copy(
                                saveState = ConfigurationSaveViewModel.reduce(it.saveState, saveEvent)
                            )
                        }
                    }
                }
            }
            is ConfigurationSettingsEvent.ShowExitDialog -> {
                _state.update { it.copy(showExitDialog = true) }
            }
            is ConfigurationSettingsEvent.CancelExitDialog -> {
                _state.update { it.copy(showExitDialog = false) }
            }
            is ConfigurationSettingsEvent.ConfirmExitDialog -> {
                _state.value = ConfigurationSettingsState()
            }


        }
    }

}
