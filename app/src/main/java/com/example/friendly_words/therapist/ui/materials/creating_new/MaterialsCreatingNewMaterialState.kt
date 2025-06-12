package com.example.friendly_words.therapist.ui.materials.creating_new

import com.example.friendly_words.data.entities.Image

data class MaterialsCreatingNewMaterialState (
    val resourceName: String = "",
    val images: List<Image> = emptyList(),
    val showExitConfirmation: Boolean = false,
    val isEditing: Boolean = false,
    //val nameExists: Boolean = false,
    val showNameConflictDialog: Boolean = false,
    val saveCompleted: Boolean = false,
    val exitWithoutSaving: Boolean = false,
    val newlySavedResourceId: Long? = null,
    val learnedWord: String = "",
    val allowEditingResourceName: Boolean = false
)