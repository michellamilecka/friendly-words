package com.example.friendly_words.therapist.ui.materials.creating_new

import com.example.friendly_words.data.entities.Image

sealed class MaterialsCreatingNewMaterialEvent {
    data class ResourceNameChanged(val newName: String) : MaterialsCreatingNewMaterialEvent()
    data class AddImage(val image : Image) : MaterialsCreatingNewMaterialEvent()
    data class RemoveImage(val image: Image) : MaterialsCreatingNewMaterialEvent()
    object ShowExitDialog : MaterialsCreatingNewMaterialEvent()
    object DismissExitDialog : MaterialsCreatingNewMaterialEvent()
    object ConfirmExit : MaterialsCreatingNewMaterialEvent()
    object SaveClicked : MaterialsCreatingNewMaterialEvent()

}