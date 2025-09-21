package com.example.friendly_words.child_app.data

enum class InstructionType(val format: (String) -> String) {
    SHORT({ it }),
    WHERE_IS({ "Gdzie jest ${it.lowercase()}?" }),
    SHOW_ME({ "Pokaż, gdzie jest ${it.lowercase()}" });

    fun getInstructionText(label: String): String = format(label)
}

object GameSettings {
    var numberOfPicturesPerRound: Int = 1
    var numberOfWordsToTeach = gameItems.size
    var repetitionsPerWord = 1
    var isTestMode: Boolean = false
    var instructionType: InstructionType = InstructionType.SHOW_ME

    val praises = listOf(
        "Dobrze!",
        "Super!",
        "Świetnie!",
        "Brawo!",
        "Rewelacja!",
        "Ekstra!"
    )
}

