package com.example.friendly_words

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.friendly_words.ui.theme.DarkBlue
import com.example.friendly_words.ui.theme.LightBlue2

@Composable
fun MainScreen() {
    var currentScreen by remember { mutableStateOf("main") }
    val activeConfiguration = remember { mutableStateOf<Pair<String, String>?>(Pair("1 konfiguracja", "uczenie")) } // Domyślnie zaznaczona pierwsza konfiguracja

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
                modifier = Modifier.height(95.dp),
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(top = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(120.dp))

            Text(
                text = activeConfiguration?.let {
                    "Aktywna konfiguracja: ${it.first} (tryb: ${it.second})"
                } ?: "Brak aktywnej konfiguracji",
                fontSize = 24.sp
            )

            Button(
                onClick = onMaterialsClick,
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = LightBlue2,
                    contentColor = Color.Black
                ),
                modifier = Modifier
                    .width(360.dp)
                    .height(70.dp)
            ) {
                Text("MATERIAŁY", fontSize = 15.sp)
            }

            Button(
                onClick = onConfigClick,
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = LightBlue2,
                    contentColor = Color.Black
                ),
                modifier = Modifier
                    .width(360.dp)
                    .height(70.dp)
            ) {
                Text("KONFIGURACJE", fontSize = 15.sp)
            }
        }
    }
}
