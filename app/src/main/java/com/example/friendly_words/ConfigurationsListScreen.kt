package com.example.friendly_words

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
import com.example.friendly_words.ui.components.YesNoDialog
import com.example.friendly_words.ui.theme.DarkBlue

fun generateCopyName(original: String, existing: List<String>): String {
    var copyIndex = 1
    var newName: String
    do {
        newName = "$original (kopia $copyIndex)"
        copyIndex++
    } while (newName in existing)
    return newName
}

@Composable
fun ConfigurationsListScreen(
    onBackClick: () -> Unit,
    onCreateClick: () -> Unit,
    onEditClick: (String) -> Unit,
    activeConfiguration: Pair<String, String>?,
    onSetActiveConfiguration: (Pair<String, String>?) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var configurations by remember {
        mutableStateOf(
            mutableListOf("1 konfiguracja NA STAŁE", "2 konfiguracja", "3 konfiguracja", "4 konfiguracja", "przyklad")
        )
    }

    var showDeleteDialogFor by remember { mutableStateOf<String?>(null) }
    var showActivateDialogFor by remember { mutableStateOf<String?>(null) }

    val scrollState = rememberScrollState()
    val filteredConfigurations = configurations.filter {
        it.contains(searchQuery, ignoreCase = true)
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
                        Text("UTWÓRZ", fontSize = 30.sp, color = Color.White, modifier = Modifier.clickable { onCreateClick() })
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
                value = searchQuery,
                onValueChange = { searchQuery = it },
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

                Text("NAZWA KONFIGURACJI", fontSize = 25.sp, color = Color.Gray)
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
                    ConfigurationItem(
                        title = config,
                        isActive = activeConfiguration?.first == config,
                        activeMode = if (activeConfiguration?.first == config) activeConfiguration.second else null,
                        onActivate = { selectedMode -> onSetActiveConfiguration(config to selectedMode) },
                        onActivateRequest = { showActivateDialogFor = config },
                        onDeleteRequest = { showDeleteDialogFor = config },
                        onEdit = { onEditClick(config) },
                        onCopy = {
                            val newName = generateCopyName(config, configurations)
                            configurations = configurations.toMutableList().apply { add(newName) }
                        }
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }

            // Dialog usuwania - NAPRAWIONY
            showDeleteDialogFor?.let { configToDelete ->
                YesNoDialog(
                    show = true,
                    message = "Czy chcesz usunąć konfigurację:\n$configToDelete?",
                    onConfirm = {
                        configurations = configurations.toMutableList().apply { remove(configToDelete) }
                        if (activeConfiguration?.first == configToDelete) {
                            onSetActiveConfiguration("1 konfiguracja NA STAŁE" to "uczenie")
                        }
                        showDeleteDialogFor = null
                    },
                    onDismiss = { showDeleteDialogFor = null }
                )
            }

            // Dialog aktywacji - NAPRAWIONY
            showActivateDialogFor?.let { configToActivate ->
                YesNoDialog(
                    show = true,
                    message = "Czy chcesz aktywować konfigurację:\n$configToActivate?",
                    onConfirm = {
                        onSetActiveConfiguration(configToActivate to "uczenie")
                        showActivateDialogFor = null
                    },
                    onDismiss = { showActivateDialogFor = null }
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

    // Aktualizacja stanu switcha gdy zmieni się activeMode
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
                            if (isActive) "(aktywna konfiguracja w trybie: $activeMode)"
                            else "(konfiguracja nieaktywna)",
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
                IconButton(onClick = { onCopy() }) {
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