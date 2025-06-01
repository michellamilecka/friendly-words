package com.example.friendly_words.therapist.ui.configuration.list

data class ConfigurationState(
    val searchQuery: String = "",
    val configurations: List<String> = listOf(
        "1 konfiguracja NA STAŁE", "2 konfiguracja", "3 konfiguracja", "4 konfiguracja", "przyklad"
    ),
    val activeConfiguration: Pair<String, String>? = null,
    val showDeleteDialogFor: String? = null,
    val showActivateDialogFor: String? = null
)

