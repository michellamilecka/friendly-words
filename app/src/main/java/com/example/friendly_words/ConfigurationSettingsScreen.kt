package com.example.friendly_words

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.friendly_words.ui.components.NewConfigurationTopBar
import com.example.friendly_words.ui.components.NewConfigurationTopTabs


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
                //4 -> ConfigurationSaveScreen()
            }
        }
    }
}
