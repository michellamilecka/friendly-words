package com.example.friendly_words.data.entities

import com.example.friendly_words.therapist.ui.configuration.test.ConfigurationTestState

data class TestSettings(
    val numberOfWords: Int = 0,
    //val materials: List<Resource> = emptyList(),
    val displayedImagesCount: Int = 0,
    val repetitionPerWord: Int = 0,
    val commandType: String = "",
    val showLabelsUnderImages: Boolean = false,
    val readCommand: Boolean = false,
    val totalNumberOfAttempts: Int = 0,
    val answerTimeSeconds: Int = 0
)

fun TestSettings.toConfigurationTestState(): ConfigurationTestState {
    return ConfigurationTestState(
        attemptsCount    = totalNumberOfAttempts,
        imageCount       = displayedImagesCount,
        repetitionCount  = repetitionPerWord,
        selectedPrompt   = commandType,
        captionsEnabled  = showLabelsUnderImages,
        readingEnabled   = readCommand,
        answerTime       = answerTimeSeconds
    )
}
