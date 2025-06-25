package com.example.friendly_words.therapist.ui.configuration.list

import com.example.friendly_words.data.entities.Configuration

sealed class ConfigurationEvent {
    data class SearchChanged(val query: String) : ConfigurationEvent()
    data class DeleteRequested(val configuration: Configuration) : ConfigurationEvent()
    data class ConfirmDelete(val configuration: Configuration) : ConfigurationEvent()
    data class ActivateRequested(val configuration: Configuration) : ConfigurationEvent()
    data class ConfirmActivate(val configuration: Configuration) : ConfigurationEvent()
    data class CopyRequested(val configuration: Configuration) : ConfigurationEvent()
    data class EditRequested(val configuration: Configuration) : ConfigurationEvent()
    //data class MarkShouldScrollToBottom(val scroll: Boolean) : ConfigurationEvent()
    object CreateRequested : ConfigurationEvent()
    object ClearInfoMessage : ConfigurationEvent()
    //data class ShowInfo(val message: String) : ConfigurationEvent()
    object DismissDialogs : ConfigurationEvent()
    object ScrollHandled : ConfigurationEvent()
    data class SetActiveMode(val configuration: Configuration, val mode: String) : ConfigurationEvent()

}

