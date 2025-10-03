package com.example.friendly_words.therapist.ui.configuration.save

import androidx.compose.ui.text.input.TextFieldValue

sealed class ConfigurationSaveEvent {
    data class SetStepName(val name: TextFieldValue) : ConfigurationSaveEvent()
    data class SaveConfiguration(val name: String) : ConfigurationSaveEvent()
    object ValidateName : ConfigurationSaveEvent()
    object ShowEmptyNameDialog : ConfigurationSaveEvent()
    object DismissEmptyNameDialog : ConfigurationSaveEvent()
    object DismissDuplicateNameDialog : ConfigurationSaveEvent()
}