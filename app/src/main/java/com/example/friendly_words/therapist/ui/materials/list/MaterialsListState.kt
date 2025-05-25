package com.example.friendly_words.therapist.ui.materials.list

import com.example.friendly_words.data.entities.Resource

data class MaterialsListState(
    val materials: List<Resource> = emptyList(),
    val selectedIndex: Int? = 0,
    val showDeleteDialog: Boolean = false,
    val materialToDelete: Pair<Int, Resource>? = null
)
