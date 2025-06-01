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
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MaterialsListViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val resourceRepository: ResourceRepository,
    private val imageRepository: ImageRepository,
) : ViewModel() {

    var uiState = mutableStateOf(MaterialsListState())

    init {
        // 1. Nasłuchiwanie ID nowo zapisanego zasobu z SavedStateHandle
        viewModelScope.launch {
            savedStateHandle.getStateFlow<Long?>("newlySavedResourceId", null).collect { id ->
                id?.let {
                    uiState.value = uiState.value.copy(pendingSelectId = it)
                    savedStateHandle["newlySavedResourceId"] = null
                }
            }
        }

        // 2. Nasłuchiwanie zmian w bazie zasobów
        viewModelScope.launch {
            resourceRepository.getAll().collect { resources ->
                val imagesById = resources.associate { resource ->
                    resource.id to imageRepository.getByResourceId(resource.id)
                }

                // Jeśli jest ID zasobu do zaznaczenia, szukamy jego indeksu
                val pendingId = uiState.value.pendingSelectId
                val indexToSelect = pendingId?.let { id ->
                    resources.indexOfFirst { it.id == id }.takeIf { it != -1 }
                }

                uiState.value = uiState.value.copy(
                    materials = resources,
                    imagesForSelected = imagesById,
                    selectedIndex = indexToSelect ?: uiState.value.selectedIndex,
                    pendingSelectId = null // Resetujemy, użyte
                )
            }
        }
    }

    fun onEvent(event: MaterialsListEvent) {
        when (event) {
            is MaterialsListEvent.SelectMaterial -> {
                val selected = event.index
                val selectedResource = uiState.value.materials.getOrNull(selected) ?: return

                viewModelScope.launch {
                    //val allImages = imageRepository.getAll()
                    val relatedImages = imageRepository.getByResourceId(selectedResource.id)

                    val updatedMap = uiState.value.imagesForSelected.toMutableMap().apply {
                        this[selectedResource.id] = relatedImages
                    }

                    uiState.value = uiState.value.copy(
                        selectedIndex = selected,
                        imagesForSelected = updatedMap
                    )


                }
            }


            is MaterialsListEvent.RequestDelete -> {
                uiState.value = uiState.value.copy(
                    showDeleteDialog = true,
                    materialToDelete = event.index to event.resource
                )
            }

            MaterialsListEvent.ConfirmDelete -> {
                val (index, resource) = uiState.value.materialToDelete ?: return
                val currentSelected = uiState.value.selectedIndex
                viewModelScope.launch {
                    resourceRepository.delete(resource)

                    val updatedMaterials = uiState.value.materials.filter { it.id != resource.id }

                    val newSelectedIndex = when {
                        currentSelected == index -> updatedMaterials.indices.firstOrNull()
                        currentSelected != null && currentSelected > index -> currentSelected - 1
                        else -> currentSelected
                    }

                    uiState.value = uiState.value.copy(
                        selectedIndex = newSelectedIndex,
                        showDeleteDialog = false,
                        materialToDelete = null
                    )
                }

            }

            MaterialsListEvent.DismissDeleteDialog -> {
                uiState.value = uiState.value.copy(
                    showDeleteDialog = false,
                    materialToDelete = null
                )
            }
        }
    }
}