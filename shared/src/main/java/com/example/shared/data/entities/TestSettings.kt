package com.example.shared.data.entities

import com.example.shared.data.another.ConfigurationTestState
import com.example.shared.data.another.RoundSettings

data class TestSettings(
    val numberOfWords: Int = 0,
    val displayedImagesCount: Int = 0,
    val repetitionPerWord: Int = 0,
    val commandType: String = "",
    val showLabelsUnderImages: Boolean = false,
    val readCommand: Boolean = false,
    val answerTimeSeconds: Int = 0
)

fun TestSettings.toConfigurationTestState(): ConfigurationTestState {
    return ConfigurationTestState(
        imageCount       = displayedImagesCount,
        repetitionCount  = repetitionPerWord,
        selectedPrompt   = when (commandType) {
            "SHORT" -> "{Słowo}"
            "WHERE_IS" -> "Gdzie jest {Słowo}"
            "SHOW_ME" -> "Pokaż gdzie jest {Słowo}"
            else -> "{Słowo}"
        },
        captionsEnabled  = showLabelsUnderImages,
        readingEnabled   = readCommand,
        answerTime       = answerTimeSeconds
    )
}

fun TestSettings.asRoundSettings(): RoundSettings = object : RoundSettings {
    override val numberOfWords = this@asRoundSettings.numberOfWords
    override val displayedImagesCount = this@asRoundSettings.displayedImagesCount
    override val repetitionPerWord = this@asRoundSettings.repetitionPerWord
}

