package com.example.friendly_words.therapist.ui.configuration.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ConfigurationViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(ConfigurationState())
    val state: StateFlow<ConfigurationState> = _state

    fun onEvent(event: ConfigurationEvent) {
        when (event) {
            is ConfigurationEvent.SearchChanged -> _state.update {
                it.copy(searchQuery = event.query)
            }

            is ConfigurationEvent.DeleteRequested -> _state.update {
                it.copy(showDeleteDialogFor = event.name)
            }

            is ConfigurationEvent.ConfirmDelete -> _state.update {
                val newList = it.configurations.filter { name -> name != event.name }
                val newActive = if (it.activeConfiguration?.first == event.name)
                    "1 konfiguracja NA STAŁE" to "uczenie" else it.activeConfiguration
                it.copy(
                    configurations = newList,
                    activeConfiguration = newActive,
                    showDeleteDialogFor = null
                )
            }

            is ConfigurationEvent.ActivateRequested -> _state.update {
                it.copy(showActivateDialogFor = event.name)
            }

            is ConfigurationEvent.ConfirmActivate -> _state.update {
                it.copy(
                    activeConfiguration = event.name to "uczenie",
                    showActivateDialogFor = null
                )
            }

            is ConfigurationEvent.CopyRequested -> _state.update {
                val newName = generateCopyName(event.name, it.configurations)
                it.copy(configurations = it.configurations + newName)
            }

            is ConfigurationEvent.EditRequested -> {
                // Obsługiwane w UI
            }

            ConfigurationEvent.CreateRequested -> {
                // Obsługiwane w UI
            }

            ConfigurationEvent.DismissDialogs -> _state.update {
                it.copy(showDeleteDialogFor = null, showActivateDialogFor = null)
            }
        }
    }

    private fun generateCopyName(original: String, existing: List<String>): String {
        var copyIndex = 1
        var newName: String
        do {
            newName = "$original (kopia $copyIndex)"
            copyIndex++
        } while (newName in existing)
        return newName
    }
}