package com.example.friendly_words.child_app.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.friendly_words.child_app.data.GameSettings
import com.example.friendly_words.child_app.data.StatesFromConfiguration
import com.example.shared.data.entities.Configuration
import com.example.shared.data.entities.Resource
import com.example.shared.data.repositories.ConfigurationRepository
import com.example.shared.data.repositories.ImageRepository
import com.example.shared.data.repositories.ResourceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChildMainViewModel @Inject constructor(
    private val configurationRepository: ConfigurationRepository,
    private val resourceRepository: ResourceRepository,
    private val imageRepository: ImageRepository
) : ViewModel() {

    private val _screenState = MutableStateFlow("info")
    val screenState: StateFlow<String> = _screenState.asStateFlow()

    private val _correctAnswers = MutableStateFlow(0)
    val correctAnswers: StateFlow<Int> = _correctAnswers.asStateFlow()

    private val _wrongAnswers = MutableStateFlow(0)
    val wrongAnswers: StateFlow<Int> = _wrongAnswers.asStateFlow()

    private val _activeConfig = MutableStateFlow<Configuration?>(null)
    val activeConfig: StateFlow<Configuration?> = _activeConfig.asStateFlow()

    val isTestMode: StateFlow<Boolean> = _activeConfig
        .map { it?.activeMode == "test" }
        .stateIn(viewModelScope, SharingStarted.Lazily, false)

    private val _resources = MutableStateFlow<List<Resource>>(emptyList())
    val resources: StateFlow<List<Resource>> = _resources.asStateFlow()

    init {
        viewModelScope.launch {
            configurationRepository.getAll().collect { configs ->
                val active = configs.find { it.isActive }
                _activeConfig.value = active

                active?.let { config ->
                    loadActiveConfiguration(config)

                    val configResources = configurationRepository.getResources(config.id)
                    val resourceList = configResources.mapNotNull { link ->
                        resourceRepository.getById(link.resourceId)
                    }
                    _resources.value = resourceList
                }
            }
        }
    }

    private fun loadActiveConfiguration(config: Configuration) {
        when (config.activeMode) {
            "learning", "test" -> GameSettings.loadFromLearningSettings(config.learningSettings)
            else -> {
                GameSettings.isTestMode = false
                StatesFromConfiguration.resetStates()
            }
        }
    }

    fun navigateTo(screen: String) { _screenState.value = screen }

    fun finishGame(correct: Int, wrong: Int) {
        _correctAnswers.value = correct
        _wrongAnswers.value = wrong
        _screenState.value = "end"
    }

    fun resetGame() {
        _correctAnswers.value = 0
        _wrongAnswers.value = 0
        _screenState.value = "info"
    }
}
