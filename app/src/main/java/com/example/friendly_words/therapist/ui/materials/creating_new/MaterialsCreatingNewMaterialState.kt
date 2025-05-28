package com.example.friendly_words.therapist.ui.materials.creating_new

import com.example.friendly_words.data.entities.Image

data class MaterialsCreatingNewMaterialState (
    val resourceName: String = "",
    val images: List<Image> = emptyList(),
    val showExitConfirmation: Boolean = false
)