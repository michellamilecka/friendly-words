package com.example.friendly_words.therapist.ui.materials.list

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.friendly_words.therapist.ui.theme.DarkBlue
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.window.Dialog
import com.example.friendly_words.therapist.ui.theme.LightBlue
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import com.example.friendly_words.therapist.ui.components.YesNoDialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.composables.core.ScrollArea
import com.composables.core.Thumb
import com.composables.core.VerticalScrollbar
import com.composables.core.rememberScrollAreaState

@Composable
fun MaterialsListScreen(
    navController: NavController,
    onBackClick: () -> Unit,
    onCreateClick: () -> Unit,
    onEditClick: (Long) -> Unit,
    viewModel: MaterialsListViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    var expandedImagePath by remember { mutableStateOf<String?>(null) }


    val snackbarHostState = remember { SnackbarHostState() }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    var searchQuery by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        val message = navController.currentBackStackEntry
            ?.savedStateHandle
            ?.get<String>("message")
        val index=navController.currentBackStackEntry
        message?.let {
            snackbarHostState.showSnackbar(
                message = it,
                duration = SnackbarDuration.Short
            )
            navController.currentBackStackEntry
                ?.savedStateHandle
                ?.remove<String>("message")
        }
    }
    LaunchedEffect(state.infoMessage) {
        state.infoMessage?.let {
            snackbarHostState.showSnackbar(
                message = it,
                duration = SnackbarDuration.Short
            )
            viewModel.onEvent(MaterialsListEvent.ClearInfoMessage)
        }
    }
    val newlySavedId = remember {
        navController.currentBackStackEntry
            ?.savedStateHandle
            ?.get<Long>("newlySavedResourceId")
    }.also {
        // od razu usuń, żeby już się nie pojawiło przy kolejnych rekonfiguracjach
        navController.currentBackStackEntry
            ?.savedStateHandle
            ?.remove<Long>("newlySavedResourceId")
    }

// Uruchom, gdy mamy both: nowy ID **i** już załadowane materiały
    LaunchedEffect(newlySavedId, state.materials) {
        if (newlySavedId != null && state.materials.isNotEmpty()) {
            viewModel.onEvent(MaterialsListEvent.SelectByResourceId(newlySavedId))
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                backgroundColor = DarkBlue,
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
                            "Materiały edukacyjne",
                            fontSize = 30.sp,
                            color = Color.White,
                            modifier = Modifier.weight(1f)
                        )

                        Text(
                            "DODAJ",
                            fontSize = 30.sp,
                            color = Color.White,
                            modifier = Modifier.clickable { onCreateClick() }
                        )
                        Spacer(modifier = Modifier.width(15.dp))
                    }
                }
            )
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState
            ) { snackbarData ->
                Snackbar(
                    modifier = Modifier
                        .padding(16.dp)
                        .height(80.dp),  // Zwiększona wysokość
                    backgroundColor = Color.DarkGray,
                    contentColor = Color.White
                ) {
                    Text(
                        text = snackbarData.message,
                        fontSize = 28.sp,                  // Większa czcionka
                        textAlign = TextAlign.Center,      // Wyrównanie do środka
                        modifier = Modifier.fillMaxWidth() // Zajmuje całą szerokość
                    )
                }
            }
        }

    ) { padding ->
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    focusManager.clearFocus()
                    keyboardController?.hide()
                }
        ) {

            // Lista materiałów
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .background(DarkBlue.copy(alpha = 0.1f))
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text("Szukaj materiału...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            keyboardController?.hide()
                            focusManager.clearFocus()
                        }
                    ),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = DarkBlue,
                        unfocusedBorderColor = Color.Gray,
                        focusedLabelColor = DarkBlue,
                        unfocusedLabelColor = Color.Gray,
                        cursorColor = Color.Black
                    ),
                    shape = RoundedCornerShape(10)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                ) {
                    Text("NAZWA MATERIAŁU", fontSize = 20.sp, color = Color.Gray, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                    Text("AKCJE", fontSize = 20.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                }


                val listState = rememberLazyListState()
                val scrollAreaState = rememberScrollAreaState(listState)
                LaunchedEffect(state.selectedIndex, searchQuery) {
                    state.selectedIndex?.let { selectedIdx ->
                        if (selectedIdx >= 0) {
                            val selectedMaterial = state.materials.getOrNull(selectedIdx)
                            selectedMaterial?.let { material ->
                                // Znajdź indeks w filtrowanej liście
                                val filteredMaterials = state.materials.filter {
                                    it.name.contains(searchQuery, ignoreCase = true) || it.category.contains(searchQuery, ignoreCase = true)
                                }

                                val filteredIndex = filteredMaterials.indexOf(material)

                                // Scrolluj tylko jeśli materiał jest widoczny w filtrowanej liście
                                if (filteredIndex >= 0) {
                                    Log.d(
                                        "MaterialsListScreen",
                                        "Scrolling to selectedIndex = $selectedIdx → filteredIndex = $filteredIndex, resourceId = ${material.id}"
                                    )
                                    listState.animateScrollToItem(filteredIndex)
                                } else {
                                    Log.d(
                                        "MaterialsListScreen",
                                        "Selected material not visible in filtered list"
                                    )
                                }
                            }
                        }
                    }
                }

                ScrollArea(state = scrollAreaState) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        LazyColumn(
                            state = listState,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            val filteredMaterials = state.materials.filter {
                                it.name.contains(searchQuery, ignoreCase = true) || it.category.contains(searchQuery, ignoreCase = true)
                            }

                            // POPRAWIONE: używamy filteredMaterials zamiast state.materials
                            itemsIndexed(filteredMaterials) { filteredIndex, material ->
                                // Znajdź oryginalny indeks w pełnej liście
                                val originalIndex = state.materials.indexOf(material)
                                val isSelected = state.selectedIndex == originalIndex

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(if (isSelected) LightBlue.copy(alpha = 0.3f) else Color.Transparent)
                                        .clickable {
                                            viewModel.onEvent(
                                                MaterialsListEvent.SelectMaterial(
                                                    originalIndex // Używamy oryginalnego indeksu
                                                )
                                            )
                                        }
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 8.dp, horizontal = 12.dp)
                                            .height(55.dp)
                                    ) {
                                        Row(
                                            modifier = Modifier.weight(1f),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                material.name,
                                                fontSize = 28.sp,
                                                fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal
                                            )
                                            if (material.category.isNotBlank()) {
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Text(
                                                    "(${material.category})",
                                                    fontSize = 22.sp,
                                                    color = Color.Gray,
                                                    fontWeight = FontWeight.Normal
                                                )
                                            }
                                        }

                                        IconButton(onClick = {
                                            onEditClick(material.id)
                                        }) {
                                            Icon(
                                                Icons.Default.Edit,
                                                contentDescription = "Edytuj",
                                                tint = DarkBlue,
                                                modifier = Modifier.size(35.dp)
                                            )
                                        }
                                        IconButton(onClick = {
                                            viewModel.onEvent(
                                                MaterialsListEvent.RequestDelete(
                                                    originalIndex, // Używamy oryginalnego indeksu
                                                    material
                                                )
                                            )
                                        }) {
                                            Icon(
                                                Icons.Default.Delete,
                                                contentDescription = "Usuń",
                                                tint = DarkBlue,
                                                modifier = Modifier.size(35.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                        VerticalScrollbar(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .fillMaxHeight()
                                .width(4.dp)
                        ) {
                            Thumb(Modifier.background(Color.Gray))
                        }
                    }
                }
            }

            // Obrazki dla wybranego materiału
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(2f)
                    .padding(16.dp)
            ) {
                if (state.selectedIndex != null && state.selectedIndex in state.materials.indices) {
                    val word = state.materials[state.selectedIndex!!]
                    val images = state.imagesForSelected[word.id] ?: emptyList()


                    Column(modifier = Modifier.fillMaxSize()) {
                        //Text("Obrazki dla: $word", fontSize = 24.sp, modifier = Modifier.padding(bottom = 12.dp))

                        LazyVerticalGrid(
                            columns = GridCells.Fixed(3),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                        ) {
                            items(images) { image ->
                                val painter = rememberAsyncImagePainter(model = image.path)
                                Image(
                                    painter = painter,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .aspectRatio(1f)
                                        .clickable { expandedImagePath = image.path }
                                )
                            }
                        }
                    }
                } else {
                    if (state.materials.isEmpty()) {
                        Text(
                            "Brak materiałów do wyświetlenia. Dodaj nowy materiał klikając przycisk DODAJ.",
                            fontSize = 25.sp,
                            color = Color.Black
                        )
                    } else {
                        Text(
                            "Wybierz materiał z listy",
                            fontSize = 25.sp,
                            color = Color.Black
                        )
                    }}
            }
        }
    }
    if (expandedImagePath != null) {
        // Opcjonalnie: obsługa wstecz, by zamykać overlay przy Back
        androidx.activity.compose.BackHandler(enabled = true) {
            expandedImagePath = null
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.95f))
                .clickable { expandedImagePath = null }, // <- tap anywhere to close
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = rememberAsyncImagePainter(expandedImagePath),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(16.dp)
                    .clickable { expandedImagePath = null }, // <- tap image to close
                alignment = Alignment.Center,
                contentScale = androidx.compose.ui.layout.ContentScale.Fit
            )
        }
    }


    // Dialog potwierdzający usunięcie
    val materialToDelete = state.materialToDelete
    if (state.showDeleteDialog && materialToDelete != null) {
        materialToDelete.let { (index, name) ->
            YesNoDialog(
                show = true,
                message = "Czy na pewno chcesz usunąć materiał: ${name.name}?",
                onConfirm = { viewModel.onEvent(MaterialsListEvent.ConfirmDelete) },
                onDismiss = { viewModel.onEvent(MaterialsListEvent.DismissDeleteDialog) }
            )
        }
    }
}