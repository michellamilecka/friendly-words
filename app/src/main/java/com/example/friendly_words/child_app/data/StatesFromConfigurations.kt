package com.example.friendly_words.child_app.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object StatesFromConfiguration {

    // ====== Flagi aktywnych efektów (z configa) ======
    var enableDimIncorrect: Boolean = false
    var enableScaleCorrect: Boolean = false
    var enableAnimateCorrect: Boolean = false
    var enableOutlineCorrect: Boolean = false

    // ====== Flagi faktycznego działania (po triggerze) ======
    var dimIncorrect by mutableStateOf(false)
        private set

    var scaleCorrect by mutableStateOf(false)
        private set

    var animateCorrect by mutableStateOf(false)
        private set

    var outlineCorrect by mutableStateOf(false)
        private set

    var effectsDelayMillis: Long = 5000L

    fun triggerPromptOutline() {
        if (enableOutlineCorrect) {
            MainScope().launch {
                delay(effectsDelayMillis)
                outlineCorrect = true
            }
        }
    }

    fun triggerPromptAnimate() {
        if (enableAnimateCorrect) {
            MainScope().launch {
                delay(effectsDelayMillis)
                animateCorrect = true
            }
        }
    }

    fun triggerPromptScale() {
        if (enableScaleCorrect) {
            MainScope().launch {
                delay(effectsDelayMillis)
                scaleCorrect = true
            }
        }
    }

    fun triggerPromptDim() {
        if (enableDimIncorrect) {
            MainScope().launch {
                delay(effectsDelayMillis)
                dimIncorrect = true
            }
        }
    }

    fun resetStates() {
        dimIncorrect = false
        scaleCorrect = false
        animateCorrect = false
        outlineCorrect = false
    }

    fun resetEnabled() {
        enableDimIncorrect = false
        enableScaleCorrect = false
        enableAnimateCorrect = false
        enableOutlineCorrect = false
    }
}

