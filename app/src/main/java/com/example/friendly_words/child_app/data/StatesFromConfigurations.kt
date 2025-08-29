package com.example.friendly_words.child_app.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object StatesFromConfiguration {

    //Podpowiedzi
    var dimIncorrect by mutableStateOf(false)
        private set

    var scaleCorrect by mutableStateOf(false)
        private set

    var animateCorrect by mutableStateOf(false)
        private set

    var outlineCorrect by mutableStateOf(false)
        private set

    object StatesFromConfiguration {
        var effectsDelayMillis: Long = 5000L
    }
    //Czas do podpowiedzi
    var effectsDelayMillis: Long = 5000L

    fun triggerPromptOutline() {
        MainScope().launch {
            delay(effectsDelayMillis)
            outlineCorrect = true
        }
    }

    fun triggerPromptAnimate() {
        MainScope().launch {
            delay(effectsDelayMillis)
            animateCorrect = true
        }
    }

    fun triggerPromptScale() {
        MainScope().launch {
            delay(effectsDelayMillis)
            scaleCorrect = true
        }
    }

    fun triggerPromptDim() {
        MainScope().launch {
            delay(effectsDelayMillis)
            dimIncorrect = true
        }
    }

    fun resetStates() {
        dimIncorrect = false
        scaleCorrect = false
        animateCorrect = false
        outlineCorrect = false
    }
}
