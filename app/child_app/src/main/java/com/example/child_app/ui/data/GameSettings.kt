package com.example.child_app.ui.data

enum class InstructionType(val format: (String) -> String) {
    SHORT({ it }),
    WHERE_IS({ "Gdzie jest ${it.lowercase()}?" }),
    SHOW_ME({ "Pokaż, gdzie jest ${it.lowercase()}" });

    fun getInstructionText(label: String): String = format(label)
}

object GameSettings {
    var numberOfWordsToTeach = 3
    var repetitionsPerWord = 2
    var isTestMode: Boolean = false
    var instructionType: InstructionType = InstructionType.SHOW_ME
}
