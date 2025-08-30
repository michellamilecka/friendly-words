package com.example.shared.data.enums

enum class InstructionType(val format: (String) -> String) {
    SHORT({ it }),
    WHERE_IS({ "Gdzie jest ${it.lowercase()}?" }),
    SHOW_ME({ "Pokaż, gdzie jest ${it.lowercase()}" });

    fun getInstructionText(label: String): String = format(label)
}