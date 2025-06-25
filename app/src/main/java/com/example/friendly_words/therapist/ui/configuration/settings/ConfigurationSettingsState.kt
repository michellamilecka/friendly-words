package com.example.friendly_words.therapist.ui.configuration.settings

import com.example.friendly_words.therapist.ui.configuration.learning.ConfigurationLearningState
import com.example.friendly_words.therapist.ui.configuration.material.ConfigurationMaterialState
import com.example.friendly_words.therapist.ui.configuration.reinforcement.ConfigurationReinforcementEvent
import com.example.friendly_words.therapist.ui.configuration.reinforcement.ConfigurationReinforcementState
import com.example.friendly_words.therapist.ui.configuration.save.ConfigurationSaveState
import com.example.friendly_words.therapist.ui.configuration.test.ConfigurationTestState

data class ConfigurationSettingsState (
    val materialState: ConfigurationMaterialState = ConfigurationMaterialState(),
    val learningState: ConfigurationLearningState = ConfigurationLearningState(),
    val reinforcementState: ConfigurationReinforcementState = ConfigurationReinforcementState(),
    val testState: ConfigurationTestState = ConfigurationTestState(),
    val saveState: ConfigurationSaveState = ConfigurationSaveState(),
    val showExitDialog: Boolean = false,
    val message: String? = null,
    val navigateToList: Boolean = false

)