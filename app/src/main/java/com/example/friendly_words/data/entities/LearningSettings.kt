package com.example.friendly_words.data.entities

data class LearningSettings (
    val numberOfWords: Int = 0,
    //val materials: List<Resource> = emptyList(),
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