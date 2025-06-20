package com.example.friendly_words.therapist.ui.configuration.material

import com.example.friendly_words.data.entities.Resource

data class ConfigurationMaterialState(
    val vocabItems: List<VocabularyItem> = emptyList(),
    val availableWordsToAdd: List<Resource> = emptyList(),
    val selectedWordIndex: Int = 0,
    val wordIndexToDelete: Int? = null,
    val showDeleteDialog: Boolean = false,
    val showAddDialog: Boolean = false
)
