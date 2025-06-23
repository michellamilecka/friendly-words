package com.example.friendly_words.data.entities

import com.example.friendly_words.therapist.ui.configuration.learning.ConfigurationLearningState
import com.example.friendly_words.therapist.ui.configuration.reinforcement.ConfigurationReinforcementState

data class LearningSettings (
    val numberOfWords: Int = 0,
    val displayedImagesCount: Int = 0,
    val repetitionPerWord: Int = 2,
    val commandType: String = "", //idk czy tu ustawiac cos docelowo czy dopiero wewnatrz logiki aplikacji
    val showLabelsUnderImages: Boolean = true,
    val readCommand: Boolean = true,
    val hintAfterSeconds: Int = 3,
    val typesOfHints: List<String> = emptyList(),
    val typesOfPraises: List<String> = emptyList(),  //listOf("dobrze", "super", "świetnie", "ekstra", "rewelacja", "brawo"),
    val verbalPraiseEnabled: Boolean = true
)

fun LearningSettings.toConfigurationLearningState(): ConfigurationLearningState {
    return ConfigurationLearningState(
        imageCount       = displayedImagesCount,
        repetitionCount  = repetitionPerWord,
        selectedPrompt   = commandType,
        captionsEnabled  = showLabelsUnderImages,
        readingEnabled   = readCommand,
        timeCount        = hintAfterSeconds,
        outlineCorrect   = "Obramuj poprawną"  in typesOfHints,
        animateCorrect   = "Animuj poprawną"   in typesOfHints,
        scaleCorrect     = "Powiększ poprawną" in typesOfHints,
        dimIncorrect     = "Wyszarz niepoprawne" in typesOfHints
    )
}

fun LearningSettings.toConfigurationReinforcementState(): ConfigurationReinforcementState {
    return ConfigurationReinforcementState(
        praiseStates = listOf("dobrze", "super", "świetnie", "ekstra", "rewelacja", "brawo")
            .associateWith { typesOfPraises.contains(it) }
            .withDefault { false },
        praiseReadingEnabled = verbalPraiseEnabled
    )
}
