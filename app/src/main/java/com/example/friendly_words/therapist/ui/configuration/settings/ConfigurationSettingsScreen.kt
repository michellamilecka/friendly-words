package com.example.friendly_words.therapist.ui.configuration.settings

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.friendly_words.therapist.ui.components.NewConfigurationTopBar
import com.example.friendly_words.therapist.ui.components.NewConfigurationTopTabs
import com.example.friendly_words.therapist.ui.components.YesNoDialog
import com.example.friendly_words.therapist.ui.configuration.test.ConfigurationTestScreen
import com.example.friendly_words.therapist.ui.configuration.learning.ConfigurationLearningScreen
import com.example.friendly_words.therapist.ui.configuration.list.ConfigurationEvent
import com.example.friendly_words.therapist.ui.configuration.list.ConfigurationViewModel
import com.example.friendly_words.therapist.ui.configuration.material.ConfigurationMaterialScreen
import com.example.friendly_words.therapist.ui.configuration.reinforcement.ConfigurationReinforcementScreen
import com.example.friendly_words.therapist.ui.configuration.save.ConfigurationSaveEvent
import com.example.friendly_words.therapist.ui.configuration.save.ConfigurationSaveScreen
import com.example.friendly_words.therapist.ui.configuration.test.ConfigurationTestEvent
import kotlinx.coroutines.delay


@Composable
fun ConfigurationSettingsScreen(
    navController: NavController,
    onBackClick: () -> Unit,
    configId: Long? = null,
    viewModel: ConfigurationSettingsViewModel = hiltViewModel()
) {
    var selectedTabIndex by remember { mutableStateOf(0) }
    val state by viewModel.state.collectAsState()




    LaunchedEffect(state.navigateToList) {
        //Log.d("ScrollDebug", "LaunchedEffect — navigateToList=${state.navigateToList}")
        if (configId != null) {
            viewModel.loadConfiguration(configId.toLong())
        }

        if (state.navigateToList) {
            state.message?.let {
                navController.previousBackStackEntry?.savedStateHandle?.set("message", it)
            }
            onBackClick()
            viewModel.onEvent(ConfigurationSettingsEvent.ResetNavigation)
        }
    }




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
                title = "Nowy krok uczenia: ${state.saveState.stepName}",
                onBackClick = {
                    viewModel.onEvent(ConfigurationSettingsEvent.ShowExitDialog)
                }
            )
        },

    )  { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            NewConfigurationTopTabs(
                selectedTabIndex = selectedTabIndex,
                onTabSelected = { selectedTabIndex = it }
            )
            val settingsState = viewModel.state.collectAsState().value

            when (selectedTabIndex) {
                0 -> ConfigurationMaterialScreen(
                    state = viewModel.state.collectAsState().value.materialState,
                    onEvent = { viewModel.onEvent(ConfigurationSettingsEvent.Material(it)) },
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
                3 -> {
                    val currentState = viewModel.state.collectAsState().value
                    val testState = currentState.testState

                    LaunchedEffect(Unit) {
                        if (!testState.testEditEnabled) {
                            viewModel.onEvent(
                                ConfigurationSettingsEvent.Test(
                                    ConfigurationTestEvent.SetEditEnabled(false)
                                )
                            )
                        }
                    }

                    ConfigurationTestScreen(
                        state = testState,
                        onEvent = { viewModel.onEvent(ConfigurationSettingsEvent.Test(it)) },
                        onBackClick = onBackClick
                    )
                }

                4 -> ConfigurationSaveScreen(
                    materialState = settingsState.materialState,
                    learningState = settingsState.learningState,
                    reinforcementState = settingsState.reinforcementState,
                    testState = settingsState.testState,
                    saveState = settingsState.saveState,
                    onEvent = { viewModel.onEvent(ConfigurationSettingsEvent.Save(it)) },
                    onSettingsEvent = { viewModel.onEvent(it) },
                    onBackClick = onBackClick
                )
            }
        }
    }
}
