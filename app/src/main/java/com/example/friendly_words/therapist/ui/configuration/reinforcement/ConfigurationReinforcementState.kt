package com.example.friendly_words.therapist.ui.configuration.reinforcement

data class ConfigurationReinforcementState(
    val praiseStates: Map<String, Boolean> = defaultPraiseMap(),
    val animationsEnabled: Boolean = true
)

fun defaultPraiseMap(): Map<String, Boolean> = listOf(
    "dobrze", "super", "świetnie", "ekstra", "rewelacja", "brawo"
).associateWith { true }
