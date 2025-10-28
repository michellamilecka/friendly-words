package com.example.friendly_words.therapist.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val DATASTORE_NAME = "user_preferences"

val Context.dataStore by preferencesDataStore(
    name = DATASTORE_NAME
)

class PreferencesRepository(private val context: Context){

    private val dataStore = context.dataStore

    val hideExampleMaterialsFlow: Flow<Boolean> = dataStore.data
        .map { preferences ->
            preferences[SettingsPreferencesKeys.HIDE_EXAMPLE_MATERIALS] ?: false
        }

    suspend fun setHideExampleMaterials(hidden: Boolean) {
        dataStore.edit { preferences ->
            preferences[SettingsPreferencesKeys.HIDE_EXAMPLE_MATERIALS] = hidden
        }
    }
}