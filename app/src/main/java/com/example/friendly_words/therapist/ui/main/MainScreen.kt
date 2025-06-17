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
    const val CONFIG_EDIT = "config/edit/{configId}" // opcjonalnie przyszłościowo
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
            MaterialsListScreen(
                onBackClick = { navController.popBackStack() },
                onCreateClick = { navController.navigate(NavRoutes.MATERIAL_CREATE) },
                onEditClick = { resourceId ->
                    navController.navigate("materials/edit/$resourceId")
                }
            )
        }


        composable(NavRoutes.MATERIAL_CREATE) {
            MaterialsCreatingNewMaterialScreen(
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
            MaterialsCreatingNewMaterialScreen(
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
            ConfigurationSettingsScreen(onBackClick = { navController.popBackStack() })
        }

        composable(
            route = NavRoutes.CONFIG_EDIT,
            arguments = listOf(navArgument("configId") { type = NavType.StringType })
        ) {
            ConfigurationSettingsScreen(onBackClick = { navController.popBackStack() })
        }

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
                        "Aktywny krok uczenia: ${it.first} (tryb: ${it.second})"
                    } ?: "Brak aktywnego kroku uczenia",
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
                    Text("KROKI UCZENIA", fontSize = 16.sp)
                }
            }
        }
    }
}
