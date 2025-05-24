package com.example.friendly_words.therapist.ui.materials.list

sealed class MaterialsListEvent {
    data class SelectMaterial(val index: Int) : MaterialsListEvent()
    data class RequestDelete(val index: Int, val name: String) : MaterialsListEvent()
    object ConfirmDelete : MaterialsListEvent()
    object DismissDeleteDialog : MaterialsListEvent()
}

