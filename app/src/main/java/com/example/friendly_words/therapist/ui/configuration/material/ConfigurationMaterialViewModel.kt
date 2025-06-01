package com.example.friendly_words.therapist.ui.configuration.material

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ConfigurationMaterialViewModel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow(ConfigurationMaterialState())
    val state: StateFlow<ConfigurationMaterialState> = _state

    fun onEvent(event: ConfigurationMaterialEvent) {
        _state.update { reduce(it, event) }
    }

    companion object {
        fun reduce(
            state: ConfigurationMaterialState,
            event: ConfigurationMaterialEvent
        ): ConfigurationMaterialState {
            return when (event) {
                is ConfigurationMaterialEvent.WordSelected -> state.copy(selectedWordIndex = event.index)

                is ConfigurationMaterialEvent.WordDeleted -> state.copy(
                    wordIndexToDelete = event.index,
                    showDeleteDialog = true
                )

                is ConfigurationMaterialEvent.ConfirmDelete -> {
                    val updatedList = state.vocabItems.toMutableList().apply { removeAt(event.index) }
                    val newIndex = when {
                        state.selectedWordIndex == event.index -> -1
                        state.selectedWordIndex > event.index -> state.selectedWordIndex - 1
                        else -> state.selectedWordIndex
                    }
                    state.copy(
                        vocabItems = updatedList,
                        selectedWordIndex = newIndex,
                        wordIndexToDelete = null,
                        showDeleteDialog = false
                    )
                }

                ConfigurationMaterialEvent.CancelDelete -> state.copy(
                    wordIndexToDelete = null,
                    showDeleteDialog = false
                )

                ConfigurationMaterialEvent.ShowAddDialog -> state.copy(showAddDialog = true)

                ConfigurationMaterialEvent.HideAddDialog -> state.copy(showAddDialog = false)

                is ConfigurationMaterialEvent.AddWord -> state.copy(
                    vocabItems = state.vocabItems + VocabularyItem.create(event.word),
                    availableWordsToAdd = state.availableWordsToAdd - event.word,
                    showAddDialog = false
                )

                is ConfigurationMaterialEvent.ImageSelectionChanged -> {
                    val updatedItems = state.vocabItems.mapIndexed { index, item ->
                        if (index == state.selectedWordIndex)
                            item.copy(selectedImages = event.selected)
                        else item
                    }
                    state.copy(vocabItems = updatedItems)
                }

                is ConfigurationMaterialEvent.LearningTestChanged -> {
                    val updatedItems = state.vocabItems.mapIndexed { index, item ->
                        if (index == state.selectedWordIndex) {
                            item.copy(
                                inLearningStates = item.inLearningStates.toMutableList().also {
                                    it[event.index] = event.inLearning
                                },
                                inTestStates = item.inTestStates.toMutableList().also {
                                    it[event.index] = event.inTest
                                }
                            )
                        } else item
                    }
                    state.copy(vocabItems = updatedItems)
                }
                else -> state
            }
        }
    }
}
