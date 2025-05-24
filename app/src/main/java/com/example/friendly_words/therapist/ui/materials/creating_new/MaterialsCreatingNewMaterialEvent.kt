package com.example.friendly_words.therapist.ui.materials.creating_new

sealed class MaterialsCreatingNewMaterialEvent {
    data class ResourceNameChanged(val newName: String) : MaterialsCreatingNewMaterialEvent()
    object AddImage : MaterialsCreatingNewMaterialEvent()
    data class RemoveImage(val imageRes: Int) : MaterialsCreatingNewMaterialEvent()
    object ShowExitDialog : MaterialsCreatingNewMaterialEvent()
    object DismissExitDialog : MaterialsCreatingNewMaterialEvent()
    object ConfirmExit : MaterialsCreatingNewMaterialEvent()
    object SaveClicked : MaterialsCreatingNewMaterialEvent()

}