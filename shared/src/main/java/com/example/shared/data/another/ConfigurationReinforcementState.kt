package com.example.shared.data.another

data class ConfigurationReinforcementState(
    val praiseStates: Map<String, Boolean> = defaultPraiseMap(),
    val animationsEnabled: Boolean = true
)

fun defaultPraiseMap(): Map<String, Boolean> = listOf(
    "dobrze", "super", "świetnie", "ekstra", "rewelacja", "brawo"
).associateWith { true }
