package com.example.friendly_words.child_app.main

sealed class ChildMainEvent {
    object GoToNextScreen : ChildMainEvent()
    data class SetCorrectAnswers(val correct: Int) : ChildMainEvent()
    data class SetWrongAnswers(val wrong: Int) : ChildMainEvent()
}
