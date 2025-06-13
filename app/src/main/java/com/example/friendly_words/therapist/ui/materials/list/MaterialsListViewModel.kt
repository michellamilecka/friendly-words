package com.example.friendly_words.therapist.ui.materials.list

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.friendly_words.data.entities.Image
import com.example.friendly_words.data.entities.Resource
import com.example.friendly_words.data.repositories.ImageRepository
import com.example.friendly_words.data.repositories.ResourceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MaterialsListViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val resourceRepository: ResourceRepository,
    private val imageRepository: ImageRepository,
) : ViewModel() {

//    var uiState = mutableStateOf(MaterialsListState())
    private val _uiState = MutableStateFlow(MaterialsListState())
    val uiState: StateFlow<MaterialsListState> = _uiState


    init {
        // 1. Nasłuchiwanie ID nowo zapisanego zasobu z SavedStateHandle
        viewModelScope.launch {
            savedStateHandle.getStateFlow<Long?>("newlySavedResourceId", null).collect { id ->
                id?.let { newId ->
                    _uiState.update {
                        it.copy(pendingSelectId = newId)
                    }
                    savedStateHandle["newlySavedResourceId"] = null
                }
            }
        }

        // 2. Nasłuchiwanie zmian w bazie zasobów
        viewModelScope.launch {
            resourceRepository.getAll().collect { resources ->
//                val imagesById = resources.associate { resource ->
//                    resource.id to imageRepository.getByResourceId(resource.id)
//                }
                val imagesById = buildMap {
                    for (resource in resources) {
                        put(resource.id, imageRepository.getByResourceId(resource.id))
                    }
                }

                // Jeśli jest ID zasobu do zaznaczenia, szukamy jego indeksu
                val pendingId = _uiState.value.pendingSelectId
                val indexToSelect = pendingId?.let { id ->
                    resources.indexOfFirst { it.id == id }.takeIf { it != -1 }
                }

//                uiState.value = uiState.value.copy(
//                    materials = resources,
//                    imagesForSelected = imagesById,
//                    selectedIndex = indexToSelect ?: uiState.value.selectedIndex,
//                    pendingSelectId = null // Resetujemy, użyte
//                )
                _uiState.update {
                    it.copy(
                        materials = resources,
                        imagesForSelected = imagesById,
                        selectedIndex = indexToSelect ?: it.selectedIndex?.coerceAtMost(resources.lastIndex),
                        pendingSelectId = null // Resetujemy
                    )
                }
            }
        }
    }

    fun onEvent(event: MaterialsListEvent) {
        when (event) {
            is MaterialsListEvent.SelectMaterial -> {
                val selected = event.index
                val selectedResource = _uiState.value.materials.getOrNull(selected) ?: return

                viewModelScope.launch {
                    //val allImages = imageRepository.getAll()
                    val relatedImages = imageRepository.getByResourceId(selectedResource.id)

                    val updatedMap = _uiState.value.imagesForSelected.toMutableMap().apply {
                        this[selectedResource.id] = relatedImages
                    }

                    _uiState.update  {it.copy(
                        selectedIndex = selected,
                        imagesForSelected = updatedMap
                    )}


                }
            }


            is MaterialsListEvent.RequestDelete -> {
                _uiState.update {it.copy(
                    showDeleteDialog = true,
                    materialToDelete = event.index to event.resource
                )}
            }

            MaterialsListEvent.ConfirmDelete -> {
                val (index, resource) = _uiState.value.materialToDelete ?: return
                val currentSelected = _uiState.value.selectedIndex
                viewModelScope.launch {
                    resourceRepository.delete(resource)

                    val updatedMaterials = _uiState.value.materials.filter { it.id != resource.id }

                    val newSelectedIndex = when {
                        currentSelected == index -> updatedMaterials.indices.firstOrNull()
                        currentSelected != null && currentSelected > index -> currentSelected - 1
                        else -> currentSelected
                    }

                    _uiState.update {
                        it.copy(
                        selectedIndex = newSelectedIndex,
                        showDeleteDialog = false,
                        materialToDelete = null
                    )}
                }

            }

            MaterialsListEvent.DismissDeleteDialog -> {
                _uiState.update {it.copy(
                    showDeleteDialog = false,
                    materialToDelete = null
                )}
            }
        }
    }
}