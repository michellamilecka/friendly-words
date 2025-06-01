package com.example.friendly_words.therapist.ui.configuration.save

sealed class ConfigurationSaveEvent {
    data class SetStepName(val name: String) : ConfigurationSaveEvent()
    object SaveClicked : ConfigurationSaveEvent()
}