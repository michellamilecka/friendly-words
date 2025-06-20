package com.example.friendly_words.therapist.ui.configuration.save

sealed class ConfigurationSaveEvent {
    data class SetStepName(val name: String) : ConfigurationSaveEvent()
    data class SaveConfiguration(val name: String) : ConfigurationSaveEvent()
    object ValidateName : ConfigurationSaveEvent()
    object ShowEmptyNameDialog : ConfigurationSaveEvent()
    object DismissEmptyNameDialog : ConfigurationSaveEvent()

}