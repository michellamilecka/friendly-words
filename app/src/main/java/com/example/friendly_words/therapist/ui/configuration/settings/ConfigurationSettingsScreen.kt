package com.example.friendly_words.therapist.ui.configuration.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.friendly_words.therapist.ui.components.NewConfigurationTopBar
import com.example.friendly_words.therapist.ui.components.NewConfigurationTopTabs
import com.example.friendly_words.therapist.ui.components.YesNoDialog
import com.example.friendly_words.therapist.ui.configuration.test.ConfigurationTestScreen
import com.example.friendly_words.therapist.ui.configuration.learning.ConfigurationLearningScreen
import com.example.friendly_words.therapist.ui.configuration.material.ConfigurationMaterialScreen
import com.example.friendly_words.therapist.ui.configuration.reinforcement.ConfigurationReinforcementScreen
import com.example.friendly_words.therapist.ui.configuration.save.ConfigurationSaveScreen


@Composable
fun ConfigurationSettingsScreen(
    onBackClick: () -> Unit,
    viewModel: ConfigurationSettingsViewModel = hiltViewModel()
) {
    var selectedTabIndex by remember { mutableStateOf(0) }
    val state by viewModel.state.collectAsState()

    if (state.showExitDialog) {
        YesNoDialog(
            show = true,
            message = "Czy chcesz wyjść bez zapisywania?",
            onConfirm = {
                viewModel.onEvent(ConfigurationSettingsEvent.ConfirmExitDialog)
                onBackClick()
            },
            onDismiss = {
                viewModel.onEvent(ConfigurationSettingsEvent.CancelExitDialog)
            }
        )
    }

    Scaffold(
        topBar = {
            NewConfigurationTopBar(
                title = "Nowa konfiguracja",
                onBackClick = {
                    viewModel.onEvent(ConfigurationSettingsEvent.ShowExitDialog)
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            NewConfigurationTopTabs(
                selectedTabIndex = selectedTabIndex,
                onTabSelected = { selectedTabIndex = it }
            )

            when (selectedTabIndex) {
                0 -> ConfigurationMaterialScreen(
                    onBackClick = onBackClick
                )
                1 -> ConfigurationLearningScreen(
                    state = viewModel.state.collectAsState().value.learningState,
                    onEvent = { viewModel.onEvent(ConfigurationSettingsEvent.Learning(it)) },
                    onBackClick = onBackClick
                )
                2 -> ConfigurationReinforcementScreen(
                    state = viewModel.state.collectAsState().value.reinforcementState,
                    onEvent = { viewModel.onEvent(ConfigurationSettingsEvent.Reinforcement(it)) },
                    onBackClick = onBackClick
                )
                3 -> ConfigurationTestScreen(
                    state = viewModel.state.collectAsState().value.testState,
                    onEvent = { viewModel.onEvent(ConfigurationSettingsEvent.Test(it)) },
                    onBackClick = onBackClick
                )
                4 -> ConfigurationSaveScreen(
                    state = viewModel.state.collectAsState().value.saveState,
                    onEvent = { viewModel.onEvent(ConfigurationSettingsEvent.Save(it)) },
                    onBackClick = onBackClick
                )
            }
        }
    }
}
