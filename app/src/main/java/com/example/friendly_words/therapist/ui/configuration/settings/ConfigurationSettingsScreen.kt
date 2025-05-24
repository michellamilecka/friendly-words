package com.example.friendly_words.therapist.ui.configuration.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.friendly_words.therapist.ui.components.NewConfigurationTopBar
import com.example.friendly_words.therapist.ui.components.NewConfigurationTopTabs
import com.example.friendly_words.therapist.ui.configuration.test.ConfigurationTestScreen
import com.example.friendly_words.therapist.ui.configuration.learning.ConfigurationLearningScreen
import com.example.friendly_words.therapist.ui.configuration.material.ConfigurationMaterialScreen
import com.example.friendly_words.therapist.ui.configuration.reinforcement.ConfigurationReinforcementScreen
import com.example.friendly_words.therapist.ui.configuration.save.ConfigurationSaveScreen


@Composable
fun ConfigurationSettingsScreen(onBackClick: () -> Unit) {
    var selectedTabIndex by remember { mutableStateOf(0) }

    Scaffold(
        topBar = {
            NewConfigurationTopBar(title = "Nowa konfiguracja", onBackClick = onBackClick)
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
                0 -> ConfigurationMaterialScreen(onBackClick = onBackClick)
                1 -> ConfigurationLearningScreen(onBackClick = onBackClick)
                2 -> ConfigurationReinforcementScreen(onBackClick = onBackClick)
                3 -> ConfigurationTestScreen(onBackClick = onBackClick)
                4 -> ConfigurationSaveScreen(onBackClick = onBackClick)
            }
        }
    }
}
