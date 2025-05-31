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

    NavHost(
        navController = navController,
        startDestination = NavRoutes.MAIN
    ) {
        composable(NavRoutes.MAIN) {
            MainContent(
                onConfigClick = { navController.navigate(NavRoutes.CONFIG_LIST) },
                onMaterialsClick = { navController.navigate(NavRoutes.MATERIALS) },
                activeConfiguration = Pair("1 konfiguracja NA STAŁE", "uczenie")
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
                    navController.popBackStack()
                    navController.currentBackStackEntry
                        ?.savedStateHandle
                        ?.set("newlySavedResourceId", savedResourceId)
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
                    navController.popBackStack()
                    navController.currentBackStackEntry
                        ?.savedStateHandle
                        ?.set("newlySavedResourceId", savedResourceId)
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


        // TODO ekrany, ktore jeszcze nie posiadaja viewmodeli
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
