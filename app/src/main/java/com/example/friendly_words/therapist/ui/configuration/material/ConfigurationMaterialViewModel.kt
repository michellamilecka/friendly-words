package com.example.friendly_words.therapist.ui.configuration.material

import androidx.lifecycle.ViewModel

import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.example.shared.data.another.ConfigurationMaterialState

data class ImageUsageInfo(
    val word: String,
    val imagePath: String,
    val isSelected: Boolean,
    val inLearning: Boolean,
    val inTest: Boolean
)
@HiltViewModel
class ConfigurationMaterialViewModel @Inject constructor() : ViewModel() {

    companion object {
        fun reduce(
            state: ConfigurationMaterialState,
            event: ConfigurationMaterialEvent
        ): ConfigurationMaterialState {
            return when (event) {

                is ConfigurationMaterialEvent.WordDeleted -> state.copy(wordIndexToDelete = event.index, showDeleteDialog = true)

                is ConfigurationMaterialEvent.WordSelected -> state.copy(selectedWordIndex = event.index)

                is ConfigurationMaterialEvent.CancelDelete -> state.copy(
                    wordIndexToDelete = null,
                    showDeleteDialog = false
                )

                is ConfigurationMaterialEvent.ShowAddDialog -> state.copy(showAddDialog = true)

                is ConfigurationMaterialEvent.HideAddDialog -> state.copy(showAddDialog = false)

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
