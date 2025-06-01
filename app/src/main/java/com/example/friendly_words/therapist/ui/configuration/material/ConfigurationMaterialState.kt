package com.example.friendly_words.therapist.ui.configuration.material

data class ConfigurationMaterialState(
    val vocabItems: List<VocabularyItem> = listOf(
        VocabularyItem.create("Misiu"),
        VocabularyItem.create("Tablet"),
        VocabularyItem.create("But")
    ),
    val availableWordsToAdd: List<String> = listOf("Kredka", "Parasol"),
    val selectedWordIndex: Int = 0,
    val wordIndexToDelete: Int? = null,
    val showDeleteDialog: Boolean = false,
    val showAddDialog: Boolean = false
)
