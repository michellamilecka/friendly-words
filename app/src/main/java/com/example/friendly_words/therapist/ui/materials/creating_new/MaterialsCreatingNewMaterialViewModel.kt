package com.example.friendly_words.therapist.ui.materials.creating_new

import android.content.Context
import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.friendly_words.R
import com.example.friendly_words.data.entities.Image
import com.example.friendly_words.data.entities.Resource
import com.example.friendly_words.data.repositories.ImageRepository
import com.example.friendly_words.data.repositories.ResourceRepository
import dagger.assisted.Assisted
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException

@HiltViewModel
class MaterialsCreatingNewMaterialViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    savedStateHandle: SavedStateHandle,
    private val imageRepository: ImageRepository,
    private val resourceRepository: ResourceRepository
)  : ViewModel() {

    private val resourceIdToEdit = savedStateHandle.get<Long?>("resourceId")
    private val imagesToDelete = mutableListOf<Image>()

    private val _state = MutableStateFlow(
        MaterialsCreatingNewMaterialState(
            resourceName = if (resourceIdToEdit == null) "Nowy zasób" else "",
            images = emptyList(),
            isEditing = resourceIdToEdit != null
        )
    )
    val state: StateFlow<MaterialsCreatingNewMaterialState> = _state

    private suspend fun copyImageToInternalStorage(uri: Uri): String {
        val inputStream = context.contentResolver.openInputStream(uri)
            ?: throw IOException("Cannot open input stream for URI: $uri")

        val fileName = "image_${System.currentTimeMillis()}.jpg"
        val file = File(context.filesDir, fileName)

        file.outputStream().use { output ->
            inputStream.copyTo(output)
        }

        return file.absolutePath
    }


    init {
        viewModelScope.launch {
            if (resourceIdToEdit != null) {
                val resource = resourceRepository.getById(resourceIdToEdit)
                val images = imageRepository.getByResourceId(resourceIdToEdit)
                _state.update {
                    it.copy(
                        resourceName = resource.name,
                        images = images
                    )
                }
            }
        }
    }

    fun onEvent(event: MaterialsCreatingNewMaterialEvent) {
        when (event) {
            is MaterialsCreatingNewMaterialEvent.ResourceNameChanged -> {
                _state.update { it.copy(resourceName = event.newName) }
            }
            is MaterialsCreatingNewMaterialEvent.AddImage -> {
                viewModelScope.launch {
                    val localPath = copyImageToInternalStorage(event.uri)

                    val image = Image(
                        path = localPath,
                        resourceId = null // jeszcze nie przypisujemy — dopiero po SaveClicked
                    )

                    _state.update {
                        it.copy(images = it.images + image)
                    }
                }
            }
            is MaterialsCreatingNewMaterialEvent.RemoveImage -> {
                if (resourceIdToEdit != null && event.image.id != 0L) {
                    imagesToDelete.add(event.image) // dodaj do usunięcia przy zapisie
                }
                _state.update {
                    it.copy(images = it.images.filterNot { img -> img.path == event.image.path })
                }
            }
            is MaterialsCreatingNewMaterialEvent.ShowExitDialog -> {
                _state.update { it.copy(showExitConfirmation = true) }
            }
            is MaterialsCreatingNewMaterialEvent.DismissExitDialog -> {
                _state.update { it.copy(showExitConfirmation = false) }
            }
            is MaterialsCreatingNewMaterialEvent.ConfirmExit -> {
                viewModelScope.launch {
                    imageRepository.deleteUnassignedImages()
                    _state.update { it.copy(showExitConfirmation = false, exitWithoutSaving = true) }
                }
            }
            is MaterialsCreatingNewMaterialEvent.SaveClicked -> {
                viewModelScope.launch {
                    val name = state.value.resourceName.trim()
                    if (name.isBlank()) {
                        // Możesz też mieć osobne pole: showBlankNameError
                        return@launch
                    }

                    val allResources = resourceRepository.getAllOnce()
                    val alreadyExists = allResources.any { it.name.equals(name, ignoreCase = true) && it.id != resourceIdToEdit }

                    if (alreadyExists) {
                        _state.update { it.copy(showNameConflictDialog = true) }
                        return@launch
                    }

                    val resourceId = if (resourceIdToEdit == null) {
                        resourceRepository.insert(Resource(name = state.value.resourceName))
                    } else {
                        resourceRepository.update(Resource(id = resourceIdToEdit, name = state.value.resourceName))
                        resourceIdToEdit
                    }

                    state.value.images.forEach { image ->
                        val updated = image.copy(resourceId = resourceId)
                        imageRepository.insert(updated)
                    }

                    imagesToDelete.forEach { imageRepository.delete(it) }
                    imagesToDelete.clear()

                    _state.update { it.copy(saveCompleted = true, newlySavedResourceId = resourceId) }


                }
            }
            is MaterialsCreatingNewMaterialEvent.DismissNameConflictDialog -> {
                _state.update { it.copy(showNameConflictDialog = false) }
            }
            is MaterialsCreatingNewMaterialEvent.ResetSaveCompleted -> {
                _state.update { it.copy(saveCompleted = false, newlySavedResourceId = null) }
            }
            is MaterialsCreatingNewMaterialEvent.ResetExitWithoutSaving -> {
                _state.update {
                    it.copy(
                        exitWithoutSaving = false,
                        images = emptyList()
                    )
                }
            }



        }
    }
    private suspend fun reloadImages() {
        val id = resourceIdToEdit
        val images = if (id != null) {
            imageRepository.getByResourceId(id)
        } else {
            imageRepository.getAll().filter { it.resourceId == null }
        }
        _state.update { it.copy(images = images) }
    }
}
