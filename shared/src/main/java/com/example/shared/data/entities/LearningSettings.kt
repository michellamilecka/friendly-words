package com.example.shared.data.entities

import com.example.shared.data.another.ConfigurationLearningState
import com.example.shared.data.another.ConfigurationReinforcementState
import com.example.shared.data.another.RoundSettings

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
    val animationsEnabled: Boolean = true
)

fun LearningSettings.toConfigurationLearningState(): ConfigurationLearningState {
    return ConfigurationLearningState(
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
        timeCount        = hintAfterSeconds,
        outlineCorrect   = "Obramuj poprawną"  in typesOfHints,
        animateCorrect   = "Porusz poprawną"   in typesOfHints,
        scaleCorrect     = "Powiększ poprawną" in typesOfHints,
        dimIncorrect     = "Wyszarz niepoprawne" in typesOfHints
    )
}

fun LearningSettings.toConfigurationReinforcementState(): ConfigurationReinforcementState {
    return ConfigurationReinforcementState(
        praiseStates = listOf("dobrze", "super", "świetnie", "ekstra", "rewelacja", "brawo")
            .associateWith { typesOfPraises.contains(it) }
            .withDefault { false },
        animationsEnabled = animationsEnabled
    )
}

fun LearningSettings.asRoundSettings(): RoundSettings = object : RoundSettings {
    override val numberOfWords = this@asRoundSettings.numberOfWords
    override val displayedImagesCount = this@asRoundSettings.displayedImagesCount
    override val repetitionPerWord = this@asRoundSettings.repetitionPerWord
}

