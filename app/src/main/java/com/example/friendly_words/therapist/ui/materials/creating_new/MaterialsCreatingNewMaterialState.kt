package com.example.friendly_words.therapist.ui.materials.creating_new

import androidx.compose.ui.text.input.TextFieldValue
import com.example.shared.data.entities.Image

data class MaterialsCreatingNewMaterialState (
    val resourceName: String = "",
    val images: List<Image> = emptyList(),
    val showExitConfirmation: Boolean = false,
    val isEditing: Boolean = false,
    val showEmptyTextFieldsDialog: Boolean = false,
    val showDuplicateNameConfirmation: Boolean = false,
    val saveCompleted: Boolean = false,
    val exitWithoutSaving: Boolean = false,
    val newlySavedResourceId: Long? = null,
    val learnedWord: TextFieldValue = TextFieldValue(""),
    val allowEditingResourceName: Boolean = false,
    val imageToConfirmDelete: Image? = null,
    val confirmingDuplicateSave: Boolean = false

)