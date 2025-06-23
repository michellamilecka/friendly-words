package com.example.friendly_words.therapist.ui.configuration.save

data class ConfigurationSaveState(
    val stepName: String = "Nowy krok",
    val showNameError: Boolean = false,
    val editingConfigId: Long? = null,
    val showEmptyNameDialog: Boolean = false,
    val showDuplicateNameDialog: Boolean = false

)