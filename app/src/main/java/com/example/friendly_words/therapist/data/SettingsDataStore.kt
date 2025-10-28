package com.example.friendly_words.therapist.data

import androidx.datastore.preferences.core.booleanPreferencesKey

object SettingsPreferencesKeys{
    val HIDE_EXAMPLE_MATERIALS = booleanPreferencesKey("hide_example_materials")
    val HIDE_EXAMPLE_STEPS_KEY = booleanPreferencesKey("hide_example_steps")
}