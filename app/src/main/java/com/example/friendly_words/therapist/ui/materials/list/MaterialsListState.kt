package com.example.friendly_words.therapist.ui.materials.list

data class MaterialsListState(
    val materials: List<String> = listOf("Misiu", "Tablet", "But"),
    val selectedIndex: Int? = 0,
    val showDeleteDialog: Boolean = false,
    val materialToDelete: Pair<Int, String>? = null
)
