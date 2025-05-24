package com.example.friendly_words.therapist.ui.materials.creating_new

import androidx.lifecycle.ViewModel
import com.example.friendly_words.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class MaterialsCreatingNewMaterialViewModel : ViewModel() {
    private val _state = MutableStateFlow(MaterialsCreatingNewMaterialState())
    val state: StateFlow<MaterialsCreatingNewMaterialState> = _state

    fun onEvent(event: MaterialsCreatingNewMaterialEvent) {
        when (event) {
            is MaterialsCreatingNewMaterialEvent.ResourceNameChanged -> {
                _state.update { it.copy(resourceName = event.newName) }
            }
            is MaterialsCreatingNewMaterialEvent.AddImage -> {
                _state.update { it.copy(images = listOf(R.drawable.misiu_1) + it.images) }
            }
            is MaterialsCreatingNewMaterialEvent.RemoveImage -> {
                _state.update { it.copy(images = it.images - event.imageRes) }
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
                // Zapisywanie – np. walidacja, wysyłanie, itd.
            }
        }
    }
}
