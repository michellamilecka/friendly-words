package com.example.friendly_words.therapist.ui.materials.list

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
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
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        val message = navController.currentBackStackEntry
            ?.savedStateHandle
            ?.get<String>("message")

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
            SnackbarHost(hostState = snackbarHostState)
        }

    ) { padding ->
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Lista materiałów
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .background(DarkBlue.copy(alpha = 0.1f))
            ) {
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

                ScrollArea(state = scrollAreaState) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        LazyColumn(
                            state = listState,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            itemsIndexed(state.materials) { index, material ->
                                val isSelected = state.selectedIndex == index
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(if (isSelected) LightBlue.copy(alpha = 0.3f) else Color.Transparent)
                                        .clickable {
                                            viewModel.onEvent(
                                                MaterialsListEvent.SelectMaterial(
                                                    index
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
                                        Text(
                                            material.name,
                                            fontSize = 28.sp,
                                            fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal,
                                            modifier = Modifier.weight(1f)
                                        )
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
                                                    index,
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
                                )
                            }
                        }
                    }
                } else {
                    Text("Wybierz materiał z listy", fontSize = 25.sp)
                }
            }
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

