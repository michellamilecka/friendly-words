package com.example.friendly_words.therapist.ui.materials.creating_new

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.friendly_words.R
import com.example.friendly_words.data.entities.Image
import com.example.friendly_words.data.entities.Resource
import com.example.friendly_words.data.repositories.ImageRepository
import com.example.friendly_words.data.repositories.ResourceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class MaterialsCreatingNewMaterialViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val imageRepository: ImageRepository,
    private val resourceRepository: ResourceRepository,
    //private val resourceImageRepository: ResourceImageRepository
)  : ViewModel() {

    private val initialResourceName = savedStateHandle.get<String>("resourceName") ?: "Nowy zasób"
    private val _state = MutableStateFlow(MaterialsCreatingNewMaterialState(resourceName = initialResourceName, images = emptyList()))
    val state: StateFlow<MaterialsCreatingNewMaterialState> = _state

    init {
        viewModelScope.launch {
            val imagesFromDb = imageRepository.getAll()
            _state.update { it.copy(images = imagesFromDb) }
        }
    }

    fun onEvent(event: MaterialsCreatingNewMaterialEvent) {
        when (event) {
            is MaterialsCreatingNewMaterialEvent.ResourceNameChanged -> {
                _state.update { it.copy(resourceName = event.newName) }
            }
            is MaterialsCreatingNewMaterialEvent.AddImage -> {
                viewModelScope.launch {
                    imageRepository.insert(event.image)
                    val updatedImageList = imageRepository.getAll()
                    _state.update { it.copy(images = updatedImageList) }
                }
            }
            is MaterialsCreatingNewMaterialEvent.RemoveImage -> {
                viewModelScope.launch {
                    imageRepository.delete(event.image)
                    val updatedImageList = imageRepository.getAll()
                    _state.update { it.copy(images = updatedImageList) }
                }
            }
            is MaterialsCreatingNewMaterialEvent.ShowExitDialog -> {
                _state.update { it.copy(showExitConfirmation = true) }
            }
            is MaterialsCreatingNewMaterialEvent.DismissExitDialog -> {
                _state.update { it.copy(showExitConfirmation = false) }
            }
            is MaterialsCreatingNewMaterialEvent.ConfirmExit -> {
                // Trzeba zadziałać z callbackiem, patrz screen
            }
            is MaterialsCreatingNewMaterialEvent.SaveClicked -> {
                viewModelScope.launch {
                    val resourceId = resourceRepository.insert(Resource(name = state.value.resourceName))

                    state.value.images.forEach { image ->
                        val updatedImage = image.copy(resourceId = resourceId)
                        imageRepository.insert(updatedImage)
                    }
                }
            }
        }
    }
}
