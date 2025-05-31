package com.example.friendly_words.therapist.ui.materials.creating_new

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.friendly_words.R
import com.example.friendly_words.therapist.ui.components.NewConfigurationTopBar
import com.example.friendly_words.therapist.ui.components.YesNoDialog
import com.example.friendly_words.therapist.ui.theme.DarkBlue
import com.example.friendly_words.therapist.ui.theme.LightBlue
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.friendly_words.therapist.ui.components.InfoDialog


@Composable
fun MaterialsCreatingNewMaterialScreen(
    viewModel: MaterialsCreatingNewMaterialViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit,
    //resourceId: Long?
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(state.saveCompleted) {
        if (state.saveCompleted) {
            onSaveClick()
            viewModel.onEvent(MaterialsCreatingNewMaterialEvent.ResetSaveCompleted)
        }
    }

    LaunchedEffect(state.exitWithoutSaving) {
        if (state.exitWithoutSaving) {
            onBackClick()
            viewModel.onEvent(MaterialsCreatingNewMaterialEvent.ResetExitWithoutSaving)
        }
    }



    // Launcher do wyboru obrazu z galerii
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            uri?.let {
                viewModel.onEvent(MaterialsCreatingNewMaterialEvent.AddImage(it))
            }
        }
    )

    Scaffold(
        topBar = {
            NewConfigurationTopBar(
                title = if (state.isEditing) "Edycja zasobu:" else "Tworzenie zasobu:",
                onBackClick = { viewModel.onEvent(MaterialsCreatingNewMaterialEvent.ShowExitDialog) }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.onEvent(MaterialsCreatingNewMaterialEvent.SaveClicked)
                    //onSaveClick()
                },
                backgroundColor = DarkBlue,
                contentColor = Color.White,
                modifier = Modifier.size(72.dp)
            ) {
                Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(32.dp))
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color.White)
                .padding(24.dp)
        ) {
            Row(modifier = Modifier.fillMaxSize().weight(1f)) {
                // Lewa kolumna
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(200.dp))
                    Text("Nazwa zasobu", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = DarkBlue)
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(
                        value = state.resourceName,
                        onValueChange = { viewModel.onEvent(MaterialsCreatingNewMaterialEvent.ResourceNameChanged(it)) },
                        placeholder = { Text("Wpisz nazwę...") },
                        modifier = Modifier.fillMaxWidth(0.8f),
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = Color.White,
                            focusedIndicatorColor = DarkBlue,
                            unfocusedIndicatorColor = Color.Gray
                        )
                    )
                    if (state.showNameConflictDialog) {
                        InfoDialog(
                            show = true,
                            message = "Zasób o tej nazwie już istnieje.",
                            onDismiss = {
                                viewModel.onEvent(MaterialsCreatingNewMaterialEvent.DismissNameConflictDialog)
                            }
                        )
                    }
                    Spacer(modifier = Modifier.height(80.dp))
                    Button(
                        onClick = {
                            galleryLauncher.launch("image/*")
                        },
                        colors = ButtonDefaults.buttonColors(backgroundColor = DarkBlue),
                        modifier = Modifier.fillMaxWidth(0.3f).height(48.dp)
                    ) {
                        Text("Dodaj obrazek", color = Color.White, fontSize = 16.sp)
                    }
                }

                // Prawa kolumna
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(20.dp))
                    Text("Dodane obrazki", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = DarkBlue)
                    Spacer(modifier = Modifier.height(16.dp))
                    val groupedImages = state.images.chunked(3)

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(500.dp)
                            .padding(horizontal = 8.dp)
                    ) {
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            items(groupedImages) { group ->
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    group.forEach { image ->
                                        Box(
                                            modifier = Modifier.weight(1f).aspectRatio(1f).padding(top = 8.dp),
                                            contentAlignment = Alignment.TopEnd
                                        ) {
                                            val painter = rememberAsyncImagePainter(model = image.path)
                                            Image(
                                                painter = painter,
                                                contentDescription = null,
                                                contentScale = ContentScale.Fit,
                                                modifier = Modifier.fillMaxSize()
                                            )
                                            Box(
                                                modifier = Modifier
                                                    .align(Alignment.TopEnd)
                                                    .padding(6.dp)
                                                    .size(38.dp)
                                                    .background(LightBlue, shape = MaterialTheme.shapes.small),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Filled.Clear,
                                                    contentDescription = "Usuń zdjęcie",
                                                    tint = Color.White,
                                                    modifier = Modifier
                                                        .size(14.dp)
                                                        .clickable {
                                                            viewModel.onEvent(
                                                                MaterialsCreatingNewMaterialEvent.RemoveImage(image)
                                                            )
                                                        }
                                                )
                                            }
                                        }
                                    }
                                    repeat(3 - group.size) {
                                        Spacer(modifier = Modifier.weight(1f))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        if (state.showExitConfirmation) {
            YesNoDialog(
                show = true,
                message = "Wyjść bez zapisywania?",
                onConfirm = {
                    viewModel.onEvent(MaterialsCreatingNewMaterialEvent.DismissExitDialog)
                    onBackClick()
                },
                onDismiss = {
                    viewModel.onEvent(MaterialsCreatingNewMaterialEvent.DismissExitDialog)
                }
            )
        }
    }
}
