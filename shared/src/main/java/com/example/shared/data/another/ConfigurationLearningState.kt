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
    val dimIncorrect: Boolean = true,
    val availableImagesForLearning: Int = 0

)
private fun clampDisplayedImagesCount(requested: Int, available: Int): Int {
    if (available <= 0) return 0
    val maxAllowed = minOf(6, available)
    val minAllowed = 1
    return requested.coerceIn(minAllowed, maxAllowed)
}

fun ConfigurationLearningState.toLearningSettings(
    materialState: ConfigurationMaterialState,
    reinforcementState: ConfigurationReinforcementState
): LearningSettings {
    return LearningSettings(
        numberOfWords = materialState.vocabItems.size,
        //materials = emptyList(), // jeśli chcesz później dodać obrazy — tu je podstawisz
        displayedImagesCount = this.imageCount,
        repetitionPerWord = this.repetitionCount,
        commandType = when (this.selectedPrompt) {
            "{Słowo}" -> "SHORT"
            "Gdzie jest {Słowo}" -> "WHERE_IS"
            "Pokaż gdzie jest {Słowo}" -> "SHOW_ME"
            else -> "SHORT"
        },
        showLabelsUnderImages = this.captionsEnabled,
        readCommand = this.readingEnabled,
        hintAfterSeconds = this.timeCount,
        typesOfHints = buildList {
            if (outlineCorrect) add("Obramuj poprawną")
            if (animateCorrect) add("Porusz poprawną")
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
