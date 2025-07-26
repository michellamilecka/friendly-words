package com.example.friendly_words.therapist.ui.configuration.list

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.friendly_words.data.entities.Configuration
import com.example.friendly_words.data.entities.ConfigurationImageUsage
import com.example.friendly_words.data.entities.ConfigurationResource
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
    private val configurationRepository: ConfigurationRepository,

) : ViewModel() {

    private val _state = MutableStateFlow(ConfigurationState())
    val state: StateFlow<ConfigurationState> = _state


    init {

        viewModelScope.launch {
            configurationRepository.getAll().collect { configurations ->
                if (configurations.isEmpty()) {
                    configurationRepository.insert(Configuration(name = "Krok przykładowy 1",isActive = true, activeMode = "uczenie", isExample = true))
                    configurationRepository.insert(Configuration(name = "Krok przykładowy 2", isExample = true))
                    configurationRepository.insert(Configuration(name = "Krok przykładowy 3", isExample = true))
                }
                val active = configurations.find { it.isActive }
                val (examples, userConfigs) = configurations.partition { it.isExample }

                _state.update {
                    it.copy(
                        configurations = examples + userConfigs.sortedBy { cfg -> cfg.name.lowercase() },
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
                val deletingConfig = event.configuration
                val isCurrentlyActive = deletingConfig.isActive
                if (isCurrentlyActive) {
                    val exampleConfig = _state.value.configurations.firstOrNull { it.isExample }
                    if (exampleConfig != null) {
                        val updatedExample = exampleConfig.copy(isActive = true, activeMode = "uczenie")
                        configurationRepository.update(updatedExample)
                    }
                }
                configurationRepository.delete(event.configuration)
                _state.update { it.copy(showDeleteDialogFor = null,infoMessage = "Pomyślnie usunięto krok uczenia") }
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
//            is ConfigurationEvent.MarkShouldScrollToBottom -> {
//                _state.update { it.copy(shouldScrollToBottom = event.scroll) }
//            }
//            is ConfigurationEvent.ShowInfo -> {
//                _state.update { it.copy(infoMessage = event.message) }
//            }
            is ConfigurationEvent.ClearInfoMessage -> {
                _state.update { it.copy(infoMessage = null) }
            }

            is ConfigurationEvent.CopyRequested -> {

                val originalConfiguration = event.configuration
                val newName = generateCopyName(event.configuration.name, _state.value.configurations.map { it.name })

                viewModelScope.launch {

                    val newId = configurationRepository.insert(
                        originalConfiguration.copy(
                            id = 0,
                            name = newName,
                            isActive = false,
                            activeMode = null,
                            isExample = false
                        )
                    )
                    _state.update { it.copy(
                        newlyAddedConfigId = newId,
                        infoMessage = "Pomyślnie skopiowano krok uczenia"
                    ) }

                    // kopiujemy polaczenia z materialami dla danego kroku
                    val resourceLinks = configurationRepository.getResources(originalConfiguration.id)
                        .map { link ->
                            ConfigurationResource(
                                configurationId = newId,
                                resourceId      = link.resourceId
                            )
                        }
                    configurationRepository.insertResources(resourceLinks)

                    // kopiujemy uzycia obrazow dla danego kroku
                    val imageUsages = configurationRepository.getImageUsages(originalConfiguration.id)
                        .map { usage ->
                            ConfigurationImageUsage(
                                configurationId = newId,
                                imageId         = usage.imageId,
                                inLearning      = usage.inLearning,
                                inTest          = usage.inTest
                            )
                        }
                    configurationRepository.insertImageUsages(imageUsages)
                    }
            }

            is ConfigurationEvent.EditRequested -> {
                // Obsługiwane w UI
            }
            is ConfigurationEvent.ScrollHandled -> {
                _state.update { it.copy(newlyAddedConfigId = null) }
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
        val (examples, userConfigs) = configurations.partition { it.isExample }

        _state.update {
            it.copy(
                configurations = examples + userConfigs.sortedBy { cfg -> cfg.name.lowercase() },
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