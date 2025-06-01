package com.example.friendly_words.therapist.ui.configuration.material

sealed class ConfigurationMaterialEvent {
    data class WordSelected(val index: Int) : ConfigurationMaterialEvent()
    data class WordDeleted(val index: Int) : ConfigurationMaterialEvent()
    data class ConfirmDelete(val index: Int) : ConfigurationMaterialEvent()
    data class ImageSelectionChanged(val selected: List<Boolean>) : ConfigurationMaterialEvent()
    data class LearningTestChanged(val index: Int, val inLearning: Boolean, val inTest: Boolean) : ConfigurationMaterialEvent()
    data class AddWord(val word: String) : ConfigurationMaterialEvent()
    object ShowAddDialog : ConfigurationMaterialEvent()
    object HideAddDialog : ConfigurationMaterialEvent()
    object CancelDelete : ConfigurationMaterialEvent()
}
