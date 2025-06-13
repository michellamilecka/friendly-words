package com.example.friendly_words.therapist.ui.materials.creating_new

import android.net.Uri
import com.example.friendly_words.data.entities.Image

sealed class MaterialsCreatingNewMaterialEvent {
    data class ResourceNameChanged(val newName: String) : MaterialsCreatingNewMaterialEvent()
    data class AddImage(val uri : Uri) : MaterialsCreatingNewMaterialEvent()
    data class RemoveImage(val image: Image) : MaterialsCreatingNewMaterialEvent()
    object ShowExitDialog : MaterialsCreatingNewMaterialEvent()
    object DismissExitDialog : MaterialsCreatingNewMaterialEvent()
    object ConfirmExit : MaterialsCreatingNewMaterialEvent()
    object SaveClicked : MaterialsCreatingNewMaterialEvent()
    object DismissNameConflictDialog : MaterialsCreatingNewMaterialEvent()
    object ResetSaveCompleted : MaterialsCreatingNewMaterialEvent()
    object ResetExitWithoutSaving : MaterialsCreatingNewMaterialEvent()
    data class LearnedWordChanged(val word: String) : MaterialsCreatingNewMaterialEvent()
    data class ToggleAllowEditingResourceName(val allowed: Boolean) : MaterialsCreatingNewMaterialEvent()
    data class ImageTakenFromCamera(val image: Image) : MaterialsCreatingNewMaterialEvent()


}