package com.example.shared.data.another

import com.example.shared.data.entities.LearningSettings

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

fun ConfigurationLearningState.toLearningSettings(
    materialState: ConfigurationMaterialState,
    reinforcementState: ConfigurationReinforcementState
): LearningSettings {
    return LearningSettings(
        numberOfWords = materialState.vocabItems.size,
        //materials = emptyList(), // jeśli chcesz później dodać obrazy — tu je podstawisz
        displayedImagesCount = this.imageCount,
        repetitionPerWord = this.repetitionCount,
        commandType = this.selectedPrompt,
        showLabelsUnderImages = this.captionsEnabled,
        readCommand = this.readingEnabled,
        hintAfterSeconds = this.timeCount,
        typesOfHints = buildList {
            if (outlineCorrect) add("Obramuj poprawną")
            if (animateCorrect) add("Animuj poprawną")
            if (scaleCorrect) add("Powiększ poprawną")
            if (dimIncorrect) add("Wyszarz niepoprawne")
        },
        typesOfPraises = reinforcementState.praiseStates
            .filterValues { it }
            .keys
            .toList(),
        animationsEnabled = reinforcementState.animationsEnabled
    )
}
