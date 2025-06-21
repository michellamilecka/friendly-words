package com.example.friendly_words.therapist.ui.configuration.settings

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.friendly_words.data.entities.Configuration
import com.example.friendly_words.data.repositories.ConfigurationRepository
import com.example.friendly_words.data.repositories.ImageRepository
import com.example.friendly_words.data.repositories.ResourceRepository
import com.example.friendly_words.therapist.ui.configuration.learning.*
import com.example.friendly_words.therapist.ui.configuration.material.*
import com.example.friendly_words.therapist.ui.configuration.reinforcement.ConfigurationReinforcementViewModel
import com.example.friendly_words.therapist.ui.configuration.save.ConfigurationSaveEvent
import com.example.friendly_words.therapist.ui.configuration.save.ConfigurationSaveViewModel
import com.example.friendly_words.therapist.ui.configuration.test.ConfigurationTestEvent
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
    private val configurationRepository: ConfigurationRepository,
    private val resourceRepository: ResourceRepository,
    private val imageRepository: ImageRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ConfigurationSettingsState())
    val state: StateFlow<ConfigurationSettingsState> = _state

    init {
        viewModelScope.launch {
            updateAvailableWordsToAdd()
        }
    }

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
                when (val materialEvent = event.event) {
                    is ConfigurationMaterialEvent.AddWord -> {
                        viewModelScope.launch {
                            val resource = resourceRepository.getById(materialEvent.id) ?: return@launch
                            val images = imageRepository.getByResourceId(resource.id)

                            val newVocabularyItem = VocabularyItem(
                                id = resource.id,
                                word = resource.name,
                                learnedWord = resource.learnedWord,
                                selectedImages = List(images.size) { it == 0 },
                                inLearningStates = List(images.size) { it == 0 },
                                inTestStates = List(images.size) { it == 0 },
                                imagePaths = images.map { it.path }
                            )

                            _state.update { current ->
                                val updatedItems = current.materialState.vocabItems + newVocabularyItem
                                current.copy(
                                    materialState = current.materialState.copy(
                                        vocabItems = updatedItems,
                                        selectedWordIndex = updatedItems.lastIndex,
                                        showAddDialog = false
                                    )
                                )
                            }

                            logUsedImages()
                            updateAvailableWordsToAdd()
                        }
                    }

                    is ConfigurationMaterialEvent.ConfirmDelete -> {
                        viewModelScope.launch {
                            val oldState = _state.value.materialState
                            val updatedList = oldState.vocabItems.toMutableList().apply {
                                removeAt(materialEvent.index)
                            }
                            val newIndex = when {
                                oldState.selectedWordIndex == materialEvent.index -> if (updatedList.isNotEmpty()) 0 else -1
                                oldState.selectedWordIndex > materialEvent.index -> oldState.selectedWordIndex - 1
                                else -> oldState.selectedWordIndex
                            }

                            _state.update {
                                it.copy(
                                    materialState = it.materialState.copy(
                                        vocabItems = updatedList,
                                        selectedWordIndex = newIndex,
                                        wordIndexToDelete = null,
                                        showDeleteDialog = false
                                    )
                                )
                            }
                            updateAvailableWordsToAdd()
                        }
                    }

                    else -> {
                        _state.update { current ->
                            current.copy(
                                materialState = ConfigurationMaterialViewModel.reduce(
                                    current.materialState,
                                    materialEvent
                                )
                            )
                        }
                    }
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
            is ConfigurationSettingsEvent.ResetNavigation -> {
                _state.update {
                    it.copy(navigateToList = false)
                }
            }
            is ConfigurationSettingsEvent.Test -> {
                _state.update { current ->
                    val learning = current.learningState
                    val updatedTest = ConfigurationTestViewModel.reduce(
                        current.testState,
                        event.event,
                        if (
                            event.event is ConfigurationTestEvent.SetEditEnabled ||
                            event.event is ConfigurationTestEvent.ToggleTestEdit
                        ) learning else null
                    )
                    current.copy(testState = updatedTest)
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

                            val existing = configurationRepository.getAllOnce()
                            val alreadyExists = existing.any { it.name.equals(saveEvent.name, ignoreCase = true) }

                            if (alreadyExists) {
                                _state.update {
                                    it.copy(
                                        saveState = it.saveState.copy(showDuplicateNameDialog = true)
                                    )
                                }
                            } else {
                                val configuration = Configuration(
                                    name = saveEvent.name,
                                    isExample = false,
                                    learningSettings = learningSettings,
                                    testSettings = testSettings
                                )

                                configurationRepository.insert(configuration)

                                _state.update {
                                    it.copy(navigateToList = true)
                                }
                            }

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

    private suspend fun updateAvailableWordsToAdd() {
        val allResources = resourceRepository.getAllOnce()
        val usedIds = _state.value.materialState.vocabItems.map { it.id }
        val available = allResources.filter { it.id !in usedIds }

        _state.update {
            it.copy(
                materialState = it.materialState.copy(availableWordsToAdd = available)
            )
        }
    }

    private fun logUsedImages() {
        val usedImages = getDetailedImageUsage().filter { it.isSelected }

        val logMessage = buildString {
            append("Użyte zdjęcia:\n")
            usedImages.forEach {
                val mode = when {
                    it.inLearning && it.inTest -> "Learning + Test"
                    it.inLearning -> "Learning"
                    it.inTest -> "Test"
                    else -> "brak"
                }
                append("- Słowo: ${it.word} | Zdjęcie: ${it.imagePath} | Tryb: $mode\n")
            }
        }

        Log.d("ImageUsageInfo", logMessage)
    }

    fun getDetailedImageUsage(): List<ImageUsageInfo> {
        return _state.value.materialState.vocabItems.flatMap { item ->
            item.imagePaths.mapIndexed { index, path ->
                ImageUsageInfo(
                    word = item.word,
                    imagePath = path,
                    isSelected = item.selectedImages.getOrNull(index) == true,
                    inLearning = item.inLearningStates.getOrNull(index) == true,
                    inTest = item.inTestStates.getOrNull(index) == true
                )
            }
        }
    }

}
