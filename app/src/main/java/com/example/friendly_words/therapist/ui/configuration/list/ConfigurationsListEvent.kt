package com.example.friendly_words.therapist.ui.configuration.list

sealed class ConfigurationEvent {
    data class SearchChanged(val query: String) : ConfigurationEvent()
    data class DeleteRequested(val name: String) : ConfigurationEvent()
    data class ConfirmDelete(val name: String) : ConfigurationEvent()
    data class ActivateRequested(val name: String) : ConfigurationEvent()
    data class ConfirmActivate(val name: String) : ConfigurationEvent()
    data class CopyRequested(val name: String) : ConfigurationEvent()
    data class EditRequested(val name: String) : ConfigurationEvent()
    object CreateRequested : ConfigurationEvent()
    object DismissDialogs : ConfigurationEvent()
}

