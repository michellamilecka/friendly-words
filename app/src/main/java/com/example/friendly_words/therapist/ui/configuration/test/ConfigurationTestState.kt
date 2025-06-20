package com.example.friendly_words.therapist.ui.configuration.test

import com.example.friendly_words.data.entities.TestSettings
import com.example.friendly_words.therapist.ui.configuration.material.ConfigurationMaterialState

data class ConfigurationTestState(
    val attemptsCount: Int = 2,
    val imageCount: Int = 3,
    val testEditEnabled: Boolean = false,
    val selectedPrompt: String = "{Słowo}",
    val captionsEnabled: Boolean = true,
    val readingEnabled: Boolean = true
)

fun ConfigurationTestState.toTestSettings(
    materialState: ConfigurationMaterialState
): TestSettings {
    return TestSettings(
        numberOfWords = materialState.vocabItems.size,
        //materials = emptyList(), // do uzupełnienia później
        displayedImagesCount = this.imageCount,
        repetitionPerWord = 0, // brak powtórzeń w teście
        commandType = this.selectedPrompt,
        showLabelsUnderImages = this.captionsEnabled,
        readCommand = this.readingEnabled,
        totalNumberOfAttempts = this.attemptsCount,
        //answerTimeSeconds = this.timePerTask
        //todo: dodac miejsce na ustawienie czasu na odpowiedz (chyba)
    )
}
