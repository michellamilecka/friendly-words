package com.example.friendly_words.therapist.ui.materials.list

import androidx.compose.runtime.mutableStateOf
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
    private val resourceRepository: ResourceRepository,
    private val imageRepository: ImageRepository
) : ViewModel() {

    var uiState = mutableStateOf(MaterialsListState())
        private set

    init {
        viewModelScope.launch {
            resourceRepository.getAll().collect { resources ->
                // Mapujemy wszystkie zdjęcia dla każdego zasobu
                val imagesById = resources.associate { resource ->
                    resource.id to imageRepository.getByResourceId(resource.id)
                }

                uiState.value = uiState.value.copy(
                    materials = resources,
                    imagesForSelected = imagesById
                )
            }
        }
    }




    private fun observeResources() {
        viewModelScope.launch {
            resourceRepository.getAll().collect { resources ->
                uiState.value = uiState.value.copy(materials = resources)
            }
        }
    }

//    private suspend fun loadImagesFor(resourceId: Long) {
//        val images = imageRepository.getAll().filter { it.resourceId == resourceId }
//        uiState.value = uiState.value.copy(imagesForSelected = images)
//    }


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
                viewModelScope.launch {
                    resourceRepository.delete(resource)
                    // Nie trzeba ręcznie aktualizować listy — Flow zrobi to automatycznie
                    uiState.value = uiState.value.copy(
                        selectedIndex = null,
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