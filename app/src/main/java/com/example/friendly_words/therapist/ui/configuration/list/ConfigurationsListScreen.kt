package com.example.friendly_words.therapist.ui.configuration.list

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.friendly_words.therapist.ui.components.YesNoDialog
import com.example.friendly_words.therapist.ui.theme.DarkBlue

@Composable
fun ConfigurationsListScreen(
    onBackClick: () -> Unit,
    onCreateClick: () -> Unit,
    onEditClick: (String) -> Unit,
    viewModel: ConfigurationViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val scrollState = rememberScrollState()

    val filteredConfigurations = remember(state.searchQuery, state.configurations) {
        state.configurations.filter { it.contains(state.searchQuery, ignoreCase = true) }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        IconButton(onClick = onBackClick) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                        }
                        Text("Przyjazne Słowa Ustawienia", fontSize = 30.sp, modifier = Modifier.weight(1f), color = Color.White)
                        Text("UTWÓRZ", fontSize = 30.sp, color = Color.White, modifier = Modifier.clickable { viewModel.onEvent(ConfigurationEvent.CreateRequested); onCreateClick() })
                        Spacer(modifier = Modifier.width(15.dp))
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
        ) {
            TextField(
                value = state.searchQuery,
                onValueChange = { viewModel.onEvent(ConfigurationEvent.SearchChanged(it)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.2f)
                    .padding(16.dp),
                placeholder = { Text("Wyszukaj", fontSize = 35.sp) },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = "Search Icon", tint = Color.Gray)
                },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = Color.Black
                ),
                shape = RoundedCornerShape(8.dp)
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Spacer(modifier = Modifier.width(20.dp))
                var showTooltipName by remember { mutableStateOf(false) }
                var showTooltipActions by remember { mutableStateOf(false) }

                Text("NAZWA KROKU UCZENIA", fontSize = 25.sp, color = Color.Gray)
                Spacer(modifier = Modifier.width(6.dp))
                Box {
                    Icon(Icons.Default.Info, contentDescription = "Info", tint = Color.Gray,
                        modifier = Modifier.size(40.dp).clickable { showTooltipName = !showTooltipName })
                    if (showTooltipName) {
                        Popup(onDismissRequest = { showTooltipName = false }) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color.White)
                                    .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                                    .padding(8.dp)
                            ) {
                                Text("Zestaw do nauki z dodatkowymi ustawieniami procesu uczenia.", fontSize = 30.sp)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.weight(1f))
                Text("AKCJE", fontSize = 25.sp, color = Color.Gray)
                Spacer(modifier = Modifier.width(15.dp))
                Box {
                    Icon(Icons.Default.Info, contentDescription = "Info", tint = Color.Gray,
                        modifier = Modifier.size(40.dp).clickable { showTooltipActions = !showTooltipActions })
                    if (showTooltipActions) {
                        Popup(onDismissRequest = { showTooltipActions = false }) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color.White)
                                    .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                                    .padding(8.dp)
                            ) {
                                Text("Skopiuj, Edytuj, Usuń", fontSize = 30.sp)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.fillMaxHeight(0.03f))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height((100.dp + 20.dp) * 4)
                    .verticalScroll(scrollState)
            ) {
                filteredConfigurations.forEach { config ->
                    val activeConfig = state.activeConfiguration
                    val activeMode = if (activeConfig?.first == config) activeConfig.second else null

                    ConfigurationItem(
                        title = config,
                        isActive = state.activeConfiguration?.first == config,
                        activeMode = activeMode,
                        onActivate = { mode -> viewModel.onEvent(ConfigurationEvent.ConfirmActivate(config)) },
                        onActivateRequest = { viewModel.onEvent(ConfigurationEvent.ActivateRequested(config)) },
                        onDeleteRequest = { viewModel.onEvent(ConfigurationEvent.DeleteRequested(config)) },
                        onEdit = {
                            viewModel.onEvent(ConfigurationEvent.EditRequested(config))
                            onEditClick(config)
                        },
                        onCopy = { viewModel.onEvent(ConfigurationEvent.CopyRequested(config)) }
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }

            state.showDeleteDialogFor?.let { configToDelete ->
                YesNoDialog(
                    show = true,
                    message = "Czy chcesz usunąć krok uczenia:\n$configToDelete?",
                    onConfirm = {
                        viewModel.onEvent(ConfigurationEvent.ConfirmDelete(configToDelete))
                    },
                    onDismiss = { viewModel.onEvent(ConfigurationEvent.DismissDialogs) }
                )
            }

            state.showActivateDialogFor?.let { configToActivate ->
                YesNoDialog(
                    show = true,
                    message = "Czy chcesz aktywować krok uczenia:\n$configToActivate?",
                    onConfirm = {
                        viewModel.onEvent(ConfigurationEvent.ConfirmActivate(configToActivate))
                    },
                    onDismiss = { viewModel.onEvent(ConfigurationEvent.DismissDialogs) }
                )
            }
        }
    }
}

@Composable
fun ConfigurationItem(
    title: String,
    isActive: Boolean,
    activeMode: String?,
    onActivate: (String) -> Unit,
    onActivateRequest: () -> Unit,
    onDeleteRequest: () -> Unit,
    onEdit: () -> Unit,
    onCopy: () -> Unit
) {
    var switchChecked by remember { mutableStateOf(activeMode == "test") }
    val isSpecialConfig = title == "1 konfiguracja NA STAŁE"

    LaunchedEffect(activeMode) {
        switchChecked = activeMode == "test"
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .background(DarkBlue.copy(alpha = 0.2f)),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1.8f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = isActive,
                        onCheckedChange = {
                            if (!isActive) onActivateRequest()
                        },
                        colors = CheckboxDefaults.colors(
                            checkedColor = DarkBlue,
                            uncheckedColor = Color.Gray,
                            checkmarkColor = Color.White
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Spacer(modifier = Modifier.height(13.dp))
                        Text(title, fontSize = 30.sp)
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            if (isActive) "(aktywny krok w trybie: $activeMode)"
                            else "(krok nieaktywny)",
                            fontSize = 20.sp
                        )
                    }
                }
            }

            Column(modifier = Modifier.weight(1.25f), horizontalAlignment = Alignment.CenterHorizontally) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                    Text("Uczenie", fontSize = 18.sp)
                    Switch(
                        checked = switchChecked,
                        onCheckedChange = {
                            switchChecked = it
                            if (isActive) {
                                onActivate(if (it) "test" else "uczenie")
                            }
                        },
                        enabled = isActive,
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = DarkBlue,
                            uncheckedThumbColor = DarkBlue
                        )
                    )
                    Text("Test", fontSize = 18.sp)
                }
            }

            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(onClick = onCopy) {
                    Icon(
                        imageVector = Icons.Default.FileCopy,
                        contentDescription = "Copy",
                        tint = DarkBlue,
                        modifier = Modifier.size(65.dp)
                    )
                }

                if (!isSpecialConfig) {
                    Spacer(modifier = Modifier.width(20.dp))
                    IconButton(onClick = onEdit) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit",
                            tint = DarkBlue,
                            modifier = Modifier.size(65.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(20.dp))
                    IconButton(onClick = onDeleteRequest) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = DarkBlue,
                            modifier = Modifier.size(65.dp)
                        )
                    }
                }
            }
        }
    }
}

