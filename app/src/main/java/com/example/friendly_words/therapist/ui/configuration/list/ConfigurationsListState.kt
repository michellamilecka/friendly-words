package com.example.friendly_words.therapist.ui.configuration.list

data class ConfigurationState(
    val searchQuery: String = "",
    val configurations: List<String> = emptyList(),
    val activeConfiguration: Pair<String, String>? = null,
    val showDeleteDialogFor: String? = null,
    val showActivateDialogFor: String? = null
)

