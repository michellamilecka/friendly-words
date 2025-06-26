package com.example.friendly_words.therapist.ui.configuration.list

import com.example.friendly_words.data.entities.Configuration

data class ConfigurationState(
    val searchQuery: String = "",
    val configurations: List<Configuration> = emptyList(),
    val activeConfiguration: Configuration? = null,
    val showDeleteDialogFor: Configuration? = null,
    val showActivateDialogFor: Configuration? = null,
    val shouldScrollToBottom: Boolean=false,
    val infoMessage: String? = null



)

