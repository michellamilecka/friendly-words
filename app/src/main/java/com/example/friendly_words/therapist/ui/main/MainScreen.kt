package com.example.friendly_words.therapist.ui.main

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.friendly_words.therapist.ui.configuration.settings.ConfigurationSettingsScreen
import com.example.friendly_words.therapist.ui.configuration.list.ConfigurationsListScreen
import com.example.friendly_words.therapist.ui.materials.creating_new.MaterialsCreatingNewMaterialScreen
import com.example.friendly_words.therapist.ui.materials.list.MaterialsListScreen
import com.example.friendly_words.therapist.ui.theme.DarkBlue
import com.example.friendly_words.therapist.ui.theme.LightBlue2

object NavRoutes {
    const val MAIN = "main"

    // Materiały
    const val MATERIALS = "materials"
    const val MATERIAL_CREATE = "materials/create"
    const val MATERIAL_EDIT = "materials/edit/{resourceId}"

    // Konfiguracje
    const val CONFIG_LIST = "config"
    const val CONFIG_CREATE = "config/create"
    const val CONFIG_EDIT = "config/edit/{configId}"
}


@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val viewModel: MainScreenViewModel = hiltViewModel()
    val activeConfig by viewModel.activeConfiguration.collectAsState()

    NavHost(
        navController = navController,
        startDestination = NavRoutes.MAIN
    ) {
        composable(NavRoutes.MAIN) {
            MainContent(
                onConfigClick = { navController.navigate(NavRoutes.CONFIG_LIST) },
                onMaterialsClick = { navController.navigate(NavRoutes.MATERIALS) },
                activeConfiguration = activeConfig?.let {
                    it.name to (it.activeMode ?: "uczenie")
                }
            )
        }

        composable(NavRoutes.MATERIALS) {
            MaterialsListScreen(navController = navController,
                onBackClick = { navController.popBackStack() },
                onCreateClick = { navController.navigate(NavRoutes.MATERIAL_CREATE) },
                onEditClick = { resourceId ->
                    navController.navigate("materials/edit/$resourceId")
                }
            )
        }


        composable(NavRoutes.MATERIAL_CREATE) {
            MaterialsCreatingNewMaterialScreen(navController = navController,
                onBackClick = { navController.popBackStack() },
                onSaveClick = { savedResourceId ->
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("newlySavedResourceId", savedResourceId)
                    navController.popBackStack()
                }

            )
        }

        composable(
            route = NavRoutes.MATERIAL_EDIT,
            arguments = listOf(navArgument("resourceId") {
                type = NavType.LongType
            })
        ) {
            MaterialsCreatingNewMaterialScreen(navController = navController,
                onBackClick = { navController.popBackStack() },
                onSaveClick = { savedResourceId ->
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("newlySavedResourceId", savedResourceId)
                    navController.popBackStack()
                }

            )
        }

        composable(NavRoutes.CONFIG_LIST) {
            ConfigurationsListScreen(
                navController = navController,
                onBackClick = { navController.popBackStack() },
                onCreateClick = {
                    navController.navigate(NavRoutes.CONFIG_CREATE)
                },
                onEditClick = { configId ->
                    navController.navigate("config/edit/$configId")
                }
            )
        }

        composable(NavRoutes.CONFIG_CREATE) {
            ConfigurationSettingsScreen(navController = navController,onBackClick = { navController.popBackStack() })
        }

        composable(
            route = NavRoutes.CONFIG_EDIT,
            arguments = listOf(navArgument("configId") { type = NavType.LongType })
        ) { backStackEntry ->

            val configId = backStackEntry.arguments?.getLong("configId")

            ConfigurationSettingsScreen(navController = navController,
                onBackClick = { navController.popBackStack() },
                configId = configId
            )
        }

    }
}




@Composable
fun MainContent(
    activeConfiguration: Pair<String, String>?,
    onConfigClick: () -> Unit,
    onMaterialsClick: () -> Unit
) {
    var showInfo by remember { mutableStateOf(false) }
    var showMaterialsInfo by remember { mutableStateOf(false) }
    var showConfigInfo by remember { mutableStateOf(false) }
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
                            color = Color.White
                        )
                            IconButton(onClick = { showInfo = true }) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = "Informacje",
                                    tint = Color.White,
                                    modifier = Modifier.size(32.dp)
                                )
                            }

                    }
                },
                backgroundColor = DarkBlue

            )
        }
    )  { padding ->
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
                        "Aktywny krok uczenia: ${it.first} (tryb: ${it.second})"
                    } ?: "Brak aktywnego kroku uczenia",
                    fontSize = 25.sp
                )

                Button(
                    onClick = onMaterialsClick,
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = LightBlue2,
                        contentColor = Color.Black
                    ),
                    modifier = Modifier
                        .width((LocalConfiguration.current.screenWidthDp * 0.7f).dp)
                        .height(85.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {

                        Text("MATERIAŁY EDUKACYJNE", fontSize = 25.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        IconButton(
                            onClick = { showMaterialsInfo = true },
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = "Informacje o materiałach edukacyjnych",
                                tint = DarkBlue,
                                modifier = Modifier.size(32.dp)
                            )
                        }

                    }
                }

                Button(
                    onClick = onConfigClick,
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = LightBlue2,
                        contentColor = Color.Black
                    ),
                    modifier = Modifier
                        .width(((LocalConfiguration.current.screenWidthDp * 0.7f).dp))
                        .height(85.dp)
                ) {
                    Text("KROKI UCZENIA", fontSize = 25.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(
                        onClick = { showConfigInfo = true },
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Informacje o krokach uczenia",
                            tint = DarkBlue,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            }
        }
    }
    if (showMaterialsInfo) {
        AlertDialog(
            onDismissRequest = { showMaterialsInfo = false },
            title = { Text("Materiały edukacyjne") },
            text = { Text("Tekst do wklejenia od Pani z Insytutu.") },
            confirmButton = {
                TextButton(onClick = { showMaterialsInfo = false }) {
                    Text("OK", color = DarkBlue)
                }
            }
        )
    }
    if (showConfigInfo) {
        AlertDialog(
            onDismissRequest = { showConfigInfo = false },
            title = { Text("Kroki uczenia") },
            text = { Text("Tekst do wklejenia od Pani z Insytutu.") },
            confirmButton = {
                TextButton(onClick = { showConfigInfo = false }) {
                    Text("OK", color = DarkBlue)
                }
            }
        )
    }
    if (showInfo) {
        AlertDialog(
            onDismissRequest = { showInfo = false },
            title = { Text("Na czym polega aplikacja?") },
            text = {
                Text(
                    "Tekst przygotowany przez Insytut."
                )
            },
            confirmButton = {
                TextButton(onClick = { showInfo = false }) {
                    Text("OK",color= DarkBlue)
                }
            }
        )
    }

}
