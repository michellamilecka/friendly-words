package com.example.friendly_words.therapist.ui.materials.list

import com.example.shared.data.entities.Resource

sealed class MaterialsListEvent {
    data class SelectMaterial(val index: Int) : MaterialsListEvent()
    data class RequestDelete(val index: Int, val resource: Resource) : MaterialsListEvent()
    object ConfirmDelete : MaterialsListEvent()
    object DismissDeleteDialog : MaterialsListEvent()
    object ClearInfoMessage : MaterialsListEvent()
    data class SelectByResourceId(val resourceId: Long) : MaterialsListEvent()
}

