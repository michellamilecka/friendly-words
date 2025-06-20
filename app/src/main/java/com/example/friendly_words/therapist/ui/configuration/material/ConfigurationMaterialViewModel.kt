package com.example.friendly_words.therapist.ui.configuration.material

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.friendly_words.data.repositories.ImageRepository
import com.example.friendly_words.data.repositories.ResourceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import android.util.Log


@HiltViewModel
class ConfigurationMaterialViewModel @Inject constructor(
    private val resourceRepository: ResourceRepository,
    private val imageRepository: ImageRepository
) : ViewModel() {
    private val _state = MutableStateFlow(ConfigurationMaterialState())
    val state: StateFlow<ConfigurationMaterialState> = _state
    private suspend fun updateAvailableWordsToAdd() {
        val allResources = resourceRepository.getAllOnce()
        //Log.d("ConfigurationMaterial", "Wszystkie zasoby z bazy: ${allResources.map { it.name }}")

        val usedIds = _state.value.vocabItems.map { it.id }
        //Log.d("ConfigurationMaterial", "Słowa już użyte w konfiguracji: $usedIds")

        val available = allResources.filter { it.id !in usedIds }
        //Log.d("ConfigurationMaterial", "Dostępne do dodania: $available")

        _state.update {
            it.copy(availableWordsToAdd = available)
        }
    }

    init {
        viewModelScope.launch {
            Log.i("ConfigurationMaterial", "Wywołano init w ViewModelu")
            println("<<< ConfigurationMaterialViewModel INIT >>>")

            updateAvailableWordsToAdd()
        }
    }
    fun onEvent(event: ConfigurationMaterialEvent) {
        when (event) {
            is ConfigurationMaterialEvent.AddWord -> {
                viewModelScope.launch {
                    Log.d("ConfigurationMaterial", "Dodajemy zasób o ID: ${event.id}")

                    val resource = resourceRepository.getById(event.id)
                    Log.d("ConfigurationMaterial", "Zasób z bazy: ${resource?.name}, id: ${resource?.id}")

                    val images = imageRepository.getByResourceId(resource.id)
                    Log.d("ConfigurationMaterial", "Zdjęcia dla ${resource.name}: ${images.map { it.path }}")

                    val newVocabularyItem = VocabularyItem(
                        id = resource.id,
                        word = resource.name,
                        learnedWord = resource.learnedWord,
                        selectedImages = List(images.size) { it == 0 },
                        inLearningStates = List(images.size) { it == 0 },
                        inTestStates = List(images.size) { it == 0 },
                        imagePaths = images.map { it.path }
                    )

                    _state.update {
                        val updatedItems = it.vocabItems + newVocabularyItem
                        it.copy(
                            vocabItems = it.vocabItems + newVocabularyItem,
                            selectedWordIndex = updatedItems.lastIndex,
                            showAddDialog = false
                        )
                    }

                    updateAvailableWordsToAdd()
                }
            }

            is ConfigurationMaterialEvent.WordDeleted -> {
                _state.update {
                    it.copy(
                        wordIndexToDelete = event.index,
                        showDeleteDialog = true
                    )
                }
            }

            is ConfigurationMaterialEvent.ConfirmDelete -> {
                viewModelScope.launch {
                    val updatedList = _state.value.vocabItems.toMutableList().apply { removeAt(event.index) }
                    val newIndex = when {
                        _state.value.selectedWordIndex == event.index -> {
                            if (updatedList.isNotEmpty()) 0 else -1
                        }
                        _state.value.selectedWordIndex > event.index -> _state.value.selectedWordIndex - 1
                        else -> _state.value.selectedWordIndex
                    }


                    _state.update {
                        it.copy(
                            vocabItems = updatedList,
                            selectedWordIndex = newIndex,
                            wordIndexToDelete = null,
                            showDeleteDialog = false
                        )
                    }

                    updateAvailableWordsToAdd() // <-- ważne!
                }
            }



            else -> {
                _state.update { reduce(it, event) }
            }
        }
    }


    companion object {
        fun reduce(
            state: ConfigurationMaterialState,
            event: ConfigurationMaterialEvent
        ): ConfigurationMaterialState {
            return when (event) {
                is ConfigurationMaterialEvent.WordSelected -> state.copy(selectedWordIndex = event.index)





                ConfigurationMaterialEvent.CancelDelete -> state.copy(
                    wordIndexToDelete = null,
                    showDeleteDialog = false
                )

                ConfigurationMaterialEvent.ShowAddDialog -> state.copy(showAddDialog = true)

                ConfigurationMaterialEvent.HideAddDialog -> state.copy(showAddDialog = false)



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
