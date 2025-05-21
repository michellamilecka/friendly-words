package com.example.friendly_words

import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.friendly_words.ui.components.YesNoDialog
import com.example.friendly_words.ui.theme.DarkBlue

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
            mutableListOf("1 konfiguracja", "2 konfiguracja", "3 konfiguracja", "4 konfiguracja", "przyklad")
        )
    }

    val scrollState = rememberScrollState()

    // Filtrujemy listę wg wpisanego tekstu, ignorując wielkość liter
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
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White
                            )
                        }
                        Text(
                            "Przyjazne Słowa Ustawienia",
                            fontSize = 30.sp,
                            modifier = Modifier.weight(1f),
                            color = Color.White
                        )
                        Text(
                            "UTWÓRZ",
                            fontSize = 30.sp,
                            color = Color.White,
                            modifier = Modifier.clickable { onCreateClick() }
                        )
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
                    .padding(16.dp),
                placeholder = { Text("Wyszukaj", fontSize = 35.sp) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search Icon",
                        tint = Color.Gray
                    )
                },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = Color.Black
                ),
                shape = RoundedCornerShape(8.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height((100.dp + 20.dp) * 4) // wysokość dla 3 konfiguracji
                    .verticalScroll(scrollState)
            ) {
                filteredConfigurations.forEach { config ->
                    ConfigurationItem(
                        title = config,
                        isActive = activeConfiguration?.first == config,
                        activeMode = if (activeConfiguration?.first == config) activeConfiguration.second else null,
                        onActivate = { selectedMode ->
                            onSetActiveConfiguration(config to selectedMode)
                        },
                        onDelete = {
                            configurations = configurations.toMutableList().apply { remove(config) }
                            if (activeConfiguration?.first == config) {
                                onSetActiveConfiguration(null)
                            }
                        },
                        onEdit = { onEditClick(config) }
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                }
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
    onDelete: () -> Unit,
    onEdit: () -> Unit
) {
    var switchChecked by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }

    // Synchronizacja switcha tylko dla aktywnej konfiguracji
    LaunchedEffect(activeMode) {
        switchChecked = (activeMode == "test")
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .background(DarkBlue.copy(alpha = 0.2f)),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = isActive,
                onCheckedChange = {
                    if (!isActive) {
                        showDialog = true
                    }
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
                Text(
                    text = title,
                    fontSize = 30.sp
                )
                Spacer(modifier = Modifier.height(3.dp))
                Box(
                    modifier = Modifier
                        .height(26.sp.value.dp)
                        .width(400.sp.value.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = if (isActive)
                            "(aktywna konfiguracja w trybie: $activeMode)"
                        else
                            "(konfiguracja nieaktywna)",
                        fontSize = 20.sp
                    )
                }
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Tryb", fontSize = 18.sp)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
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

            Spacer(modifier = Modifier.width(425.dp))
            IconButton(onClick = { /* TODO: Copy action */ }) {
                Icon(
                    imageVector = Icons.Default.FileCopy,
                    contentDescription = "Copy",
                    tint = DarkBlue,
                    modifier = Modifier.size(65.dp)
                )
            }
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
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = DarkBlue,
                    modifier = Modifier.size(65.dp)
                )
            }
        }
    }

    if (showDialog) {
        YesNoDialog(
            show = showDialog,
            message = "Czy chcesz aktywować konfigurację:\n$title?",
            onConfirm = {
                onActivate("uczenie") // Domyślnie uczenie przy aktywacji
                showDialog = false
            },
            onDismiss = {
                showDialog = false
            }
        )
    }
}
