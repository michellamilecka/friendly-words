package com.example.friendly_words.therapist.ui.materials.creating_new

data class MaterialsCreatingNewMaterialState (
    val resourceName: String = "",
    val images: List<Int> = emptyList(),
    val showExitConfirmation: Boolean = false
)