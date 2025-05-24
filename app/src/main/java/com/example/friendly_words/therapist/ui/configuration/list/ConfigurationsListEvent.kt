package com.example.friendly_words.therapist.ui.configuration.list

sealed class ConfigurationsListEvent {
    data class OnSearchQueryChange(val query: String) : ConfigurationsListEvent()
    data class RequestDelete(val config: String) : ConfigurationsListEvent()
    data class ConfirmDelete(val config: String) : ConfigurationsListEvent()
    object DismissDeleteDialog : ConfigurationsListEvent()

    data class RequestActivate(val config: String) : ConfigurationsListEvent()
    data class ConfirmActivate(val config: String) : ConfigurationsListEvent()
    object DismissActivateDialog : ConfigurationsListEvent()

    data class CopyConfiguration(val config: String) : ConfigurationsListEvent()
}
