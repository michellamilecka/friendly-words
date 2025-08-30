package com.example.friendly_words.child_app.data

object GameSettings {
    var isTestMode: Boolean = false

    var numberOfWordsToTeach = 3
    var displayedImagesCount = 0
    var repetitionsPerWord = 2
    var instructionType = ""
    var showLabelsUnderImages = true
    var readCommand = true

    val praises = listOf(
        "Dobrze!",
        "Super!",
        "Świetnie!",
        "Brawo!",
        "Rewelacja!",
        "Ekstra!"
    )

    fun loadFromLearningSettings(settings: com.example.shared.data.entities.LearningSettings) {
        numberOfWordsToTeach = settings.numberOfWords
        displayedImagesCount = settings.displayedImagesCount
        repetitionsPerWord = settings.repetitionPerWord
        instructionType = settings.commandType
        showLabelsUnderImages = settings.showLabelsUnderImages
        readCommand = settings.readCommand
        StatesFromConfiguration.resetStates()
        StatesFromConfiguration.resetEnabled()
        settings.typesOfHints.forEach { hint ->
            when (hint) {
                "Obramuj poprawną"    -> StatesFromConfiguration.enableOutlineCorrect = true
                "Animuj poprawną"     -> StatesFromConfiguration.enableAnimateCorrect = true
                "Powiększ poprawną"   -> StatesFromConfiguration.enableScaleCorrect = true
                "Wyszarz niepoprawne" -> StatesFromConfiguration.enableDimIncorrect = true
            }
        }
        StatesFromConfiguration.effectsDelayMillis =
            (settings.hintAfterSeconds * 1000).toLong()

        //praises = settings.typesOfPraises
    }
    fun getInstructionText(label: String): String {
        return when (instructionType.uppercase()) {
            "SHORT" -> label
            "WHERE_IS" -> "Gdzie jest ${label.lowercase()}?"
            "SHOW_ME" -> "Pokaż, gdzie jest ${label.lowercase()}"
            else -> label // fallback
        }
    }

    /*fun loadFromTestSettings(settings: com.example.shared.data.entities.TestSettings) {
        numberOfWordsToTeach = settings.numberOfWords
        repetitionsPerWord = settings.repetitionPerWord
        instructionType = settings.commandType
        praises = listOf("Dobrze", "Super", "Świetnie", "Brawo") // np. default w teście
    }*/
}

