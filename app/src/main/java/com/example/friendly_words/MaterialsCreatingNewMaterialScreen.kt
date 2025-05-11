package com.example.friendly_words

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.friendly_words.ui.components.NewConfigurationTopBar
import com.example.friendly_words.ui.components.NewMaterialTopTabs

@Composable
fun MaterialsCreatingNewMaterialScreen(onBackClick: () -> Unit) {
    var selectedTabIndex by remember { mutableStateOf(0) }

    Scaffold(
        topBar = {
            NewConfigurationTopBar(title = "Nazwa zasobu:", onBackClick = onBackClick)
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            NewMaterialTopTabs(
                selectedTabIndex = selectedTabIndex,
                onTabSelected = { selectedTabIndex = it }
            )

            when (selectedTabIndex) {
                0 -> MaterialsImagesScreen()
                1 -> MaterialsSaveScreen()
            }
        }
    }
}
