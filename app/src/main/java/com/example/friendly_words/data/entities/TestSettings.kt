package com.example.friendly_words.data.entities

data class TestSettings(
    val numberOfWords: Int = 0,
    //val materials: List<Resource> = emptyList(),
    val displayedImagesCount: Int = 0,
    val repetitionPerWord: Int = 0,
    val commandType: String = "",
    val showLabelsUnderImages: Boolean = false,
    val readCommand: Boolean = false,
    val totalNumberOfAttempts: Int = 0,
    val answerTimeSeconds: Int = 0
)