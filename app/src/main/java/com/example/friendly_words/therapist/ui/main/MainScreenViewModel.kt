package com.example.friendly_words.therapist.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shared.data.entities.Configuration
import com.example.shared.data.repositories.ConfigurationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val configurationRepository: ConfigurationRepository
) : ViewModel() {

    private val _activeConfiguration = MutableStateFlow<Configuration?>(null)
    val activeConfiguration: StateFlow<Configuration?> = _activeConfiguration

    init {
        viewModelScope.launch {
            configurationRepository.getAll().collectLatest { configs ->
                _activeConfiguration.value = configs.find { it.isActive }
            }
        }
    }
}
