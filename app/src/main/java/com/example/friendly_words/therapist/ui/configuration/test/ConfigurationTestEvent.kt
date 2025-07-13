package com.example.friendly_words.therapist.ui.configuration.test

sealed class  ConfigurationTestEvent {
    data class SetRepetitionCount(val count: Int) : ConfigurationTestEvent()
    data class SetImageCount(val count: Int) : ConfigurationTestEvent()
    data class SetEditEnabled(val enabled: Boolean) : ConfigurationTestEvent()
    object ToggleTestEdit : ConfigurationTestEvent()
    data class SetPrompt(val prompt: String) : ConfigurationTestEvent()
    data class ToggleCaptions(val enabled: Boolean) : ConfigurationTestEvent()
    data class ToggleReading(val enabled: Boolean) : ConfigurationTestEvent()
}