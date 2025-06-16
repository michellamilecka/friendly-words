package com.example.friendly_words.therapist.ui.configuration.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.friendly_words.data.entities.Configuration
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.friendly_words.data.repositories.ConfigurationRepository
import kotlinx.coroutines.flow.first

@HiltViewModel
class ConfigurationViewModel @Inject constructor(
    private val configurationRepository: ConfigurationRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ConfigurationState())
    val state: StateFlow<ConfigurationState> = _state


    init {
        viewModelScope.launch {
            configurationRepository.getAll().collect { configurations ->
                if (configurations.isEmpty()) {
                    configurationRepository.insert(Configuration(name = "Przykładowa konfiguracja 1"))
                    configurationRepository.insert(Configuration(name = "Przykładowa konfiguracja 2"))
                }
                val active = configurations.find { it.isActive }
                _state.update {
                    it.copy(
                        configurations = configurations,
                        activeConfiguration = active
                    )
                }
            }
        }
    }

    fun onEvent(event: ConfigurationEvent) {
        when (event) {
            is ConfigurationEvent.SearchChanged -> _state.update {
                it.copy(searchQuery = event.query)
            }

            is ConfigurationEvent.DeleteRequested -> _state.update {
                it.copy(showDeleteDialogFor = event.configuration)
            }

            is ConfigurationEvent.ConfirmDelete -> viewModelScope.launch {
                configurationRepository.delete(event.configuration)
                _state.update { it.copy(showDeleteDialogFor = null) }
            }

            is ConfigurationEvent.ActivateRequested -> _state.update {
                it.copy(showActivateDialogFor = event.configuration)
            }

            is ConfigurationEvent.ConfirmActivate -> {
                viewModelScope.launch {
                    configurationRepository.setActiveConfiguration(event.configuration, "uczenie")
                    refreshConfigurations()
                    _state.update {
                        it.copy(showActivateDialogFor = null)
                    }
                }
            }


            is ConfigurationEvent.CopyRequested -> {
                val newName = generateCopyName(event.configuration.name, _state.value.configurations.map { it.name })
                val copied = event.configuration.copy(id = 0, name = newName)
                viewModelScope.launch { configurationRepository.insert(copied) }
            }

            is ConfigurationEvent.EditRequested -> {
                // Obsługiwane w UI
            }

            is ConfigurationEvent.SetActiveMode -> {
                viewModelScope.launch {
                    configurationRepository.setActiveConfiguration(event.configuration, event.mode)
                    refreshConfigurations()
                }
            }

            ConfigurationEvent.CreateRequested -> {
                // Obsługiwane w UI
            }

            ConfigurationEvent.DismissDialogs -> _state.update {
                it.copy(showDeleteDialogFor = null, showActivateDialogFor = null)
            }
        }
    }

    private suspend fun refreshConfigurations() {
        val configurations = configurationRepository.getAll().first()
        val active = configurations.find { it.isActive }
        _state.update {
            it.copy(
                configurations = configurations,
                activeConfiguration = active
            )
        }
    }


    private fun generateCopyName(original: String, existing: List<String>): String {
        var copyIndex = 1
        var newName: String
        do {
            newName = "$original (kopia $copyIndex)"
            copyIndex++
        } while (newName in existing)
        return newName
    }
}