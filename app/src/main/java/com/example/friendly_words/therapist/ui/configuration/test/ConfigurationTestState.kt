package com.example.friendly_words.therapist.ui.configuration.test

data class ConfigurationTestState(
    val attemptsCount: Int = 2,
    val imageCount: Int = 3,
    val testEditEnabled: Boolean = false,
    val selectedPrompt: String = "{Słowo}",
    val captionsEnabled: Boolean = true,
    val readingEnabled: Boolean = true
)