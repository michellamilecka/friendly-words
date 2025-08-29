package com.example.shared.data.another

import com.example.shared.data.entities.TestSettings

data class ConfigurationTestState(
    val imageCount: Int = 3,
    val repetitionCount: Int = 0,
    val answerTime: Int = 0, // todo: chyba trzeba dodac komponent do ustawiania czasu na odpowiedz w teście
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
        displayedImagesCount = this.imageCount,
        repetitionPerWord = this.repetitionCount,
        commandType = this.selectedPrompt,
        showLabelsUnderImages = this.captionsEnabled,
        readCommand = this.readingEnabled
    )
}
fun ConfigurationLearningState.toDerivedTestState(): ConfigurationTestState {
    return ConfigurationTestState(
        imageCount = this.imageCount,
        repetitionCount = this.repetitionCount,
        selectedPrompt = this.selectedPrompt,
        captionsEnabled = this.captionsEnabled,
        readingEnabled = this.readingEnabled,
        testEditEnabled = false
    )
}

