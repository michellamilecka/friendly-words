package com.example.friendly_words.therapist.ui.configuration.test

sealed class ConfigurationTestEvent {
    data class SetAttemptsCount(val count: Int) : ConfigurationTestEvent()
    data class SetTimeCount(val count: Int) : ConfigurationTestEvent()
}