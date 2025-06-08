package com.example.friendly_words.therapist.ui.configuration.learning

data class ConfigurationLearningState(
    val imageCount: Int = 3,
    val repetitionCount: Int = 2,
    val timeCount: Int = 3,
    val selectedPrompt: String = "{Słowo}",
    val captionsEnabled: Boolean = true,
    val readingEnabled: Boolean = true,
    val outlineCorrect: Boolean = false,
    val animateCorrect: Boolean = false,
    val scaleCorrect: Boolean = false,
    val dimIncorrect: Boolean = true
)