package com.example.friendly_words.therapist.ui.main

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.friendly_words.therapist.ui.configuration.settings.ConfigurationSettingsScreen
import com.example.friendly_words.therapist.ui.configuration.list.ConfigurationsListScreen
import com.example.friendly_words.therapist.ui.materials.creating_new.MaterialsCreatingNewMaterialScreen
import com.example.friendly_words.therapist.ui.materials.list.MaterialsListScreen
import com.example.friendly_words.therapist.ui.theme.DarkBlue
import com.example.friendly_words.therapist.ui.theme.LightBlue2

@Composable
fun MainScreen() {
    var currentScreen by remember { mutableStateOf("main") }
    val activeConfiguration = remember { mutableStateOf<Pair<String, String>?>(Pair("1 konfiguracja NA STAŁE", "uczenie")) } // Domyślnie zaznaczona pierwsza konfiguracja

    when (currentScreen) {
        "main" -> MainContent(
            activeConfiguration = activeConfiguration.value,
            onConfigClick = { currentScreen = "config" },
            onMaterialsClick = { currentScreen = "materials" }
        )
        "config" -> ConfigurationsListScreen(
            onBackClick = { currentScreen = "main" },
            onCreateClick = { currentScreen = "create" },
            onEditClick = { currentScreen = "create" },
            activeConfiguration = activeConfiguration.value,
            onSetActiveConfiguration = { activeConfiguration.value = it }
        )
        "create" -> ConfigurationSettingsScreen(
            onBackClick = { currentScreen = "config" }
        )
        "materials" -> MaterialsListScreen(
            onBackClick = { currentScreen = "main" },
            onCreateClick = { currentScreen = "createMaterial" }
        )
        "createMaterial" -> MaterialsCreatingNewMaterialScreen(
            onBackClick = { currentScreen = "materials" },
            onSaveClick = { currentScreen = "materials" }
        )
    }
}

@Composable
fun MainContent(
    activeConfiguration: Pair<String, String>?,
    onConfigClick: () -> Unit,
    onMaterialsClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                //modifier = Modifier.height(95.dp),
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            "Przyjazne Słowa Ustawienia",
                            fontSize = 30.sp,
                            modifier = Modifier.weight(1f),
                            color = Color.White
                        )
                    }
                },
                backgroundColor = DarkBlue
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp),
                modifier = Modifier
                    .fillMaxWidth(0.9f)
            ) {
                Text(
                    text = activeConfiguration?.let {
                        "Aktywna konfiguracja: ${it.first} (tryb: ${it.second})"
                    } ?: "Brak aktywnej konfiguracji",
                    fontSize = 20.sp
                )

                Button(
                    onClick = onMaterialsClick,
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = LightBlue2,
                        contentColor = Color.Black
                    ),
                    modifier = Modifier
                        .width((LocalConfiguration.current.screenWidthDp/2).dp)
                        .height(70.dp)
                ) {
                    Text("MATERIAŁY", fontSize = 16.sp)
                }

                Button(
                    onClick = onConfigClick,
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = LightBlue2,
                        contentColor = Color.Black
                    ),
                    modifier = Modifier
                        .width(((LocalConfiguration.current.screenWidthDp/2).dp))
                        .height(70.dp)
                ) {
                    Text("KONFIGURACJE", fontSize = 16.sp)
                }
            }
        }
    }
}
