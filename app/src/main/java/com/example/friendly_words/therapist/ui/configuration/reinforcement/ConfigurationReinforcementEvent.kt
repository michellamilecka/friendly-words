package com.example.friendly_words.therapist.ui.configuration.reinforcement

sealed class ConfigurationReinforcementEvent {
    data class TogglePraise(val word: String, val enabled: Boolean) : ConfigurationReinforcementEvent()
    data class ToggleReading(val enabled: Boolean) : ConfigurationReinforcementEvent()
}
