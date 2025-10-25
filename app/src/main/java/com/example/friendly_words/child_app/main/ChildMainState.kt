package com.example.friendly_words.child_app.main

data class ChildMainState(
    val screenState: String = "info",
    val correctAnswers: Int = 0,
    val wrongAnswers: Int = 0,
    val canPlay: Boolean = false
)
