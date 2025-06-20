package com.example.friendly_words.therapist.ui.materials.creating_new

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CheckboxDefaults
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
import java.io.File
import androidx.compose.ui.platform.LocalContext
import java.io.IOException
import android.graphics.Bitmap
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import java.io.FileOutputStream
import com.example.friendly_words.data.entities.Image

@Composable
fun MaterialsCreatingNewMaterialScreen(
    viewModel: MaterialsCreatingNewMaterialViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onSaveClick: (Long) -> Unit,
    //resourceId: Long?
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(state.saveCompleted) {
    val savedId = state.newlySavedResourceId
        if (state.saveCompleted && savedId != null) {
            onSaveClick(savedId)
            viewModel.onEvent(MaterialsCreatingNewMaterialEvent.ResetSaveCompleted)

        }
    }

    LaunchedEffect(state.exitWithoutSaving) {
        if (state.exitWithoutSaving) {
            onBackClick()
            viewModel.onEvent(MaterialsCreatingNewMaterialEvent.ResetExitWithoutSaving)
        }
    }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
        keyboardController?.show()
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

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview(),
        onResult = { bitmap ->
            bitmap?.let {
                val filename = "photo_${System.currentTimeMillis()}.jpg"
                val file = File(context.filesDir, filename)

                try {
                    FileOutputStream(file).use { out ->
                        it.compress(Bitmap.CompressFormat.JPEG, 100, out)
                    }

                    val image = Image(
                        path = file.absolutePath,
                        resourceId = null
                    )

                    viewModel.onEvent(MaterialsCreatingNewMaterialEvent.ImageTakenFromCamera(image))

                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    )

    Scaffold(
        topBar = {
            NewConfigurationTopBar(
                title = if (state.isEditing) {
                   "Edycja: ${state.resourceName}"
                } else {
                     "Nowy materiał: ${state.resourceName}"
                },
                onBackClick = { viewModel.onEvent(MaterialsCreatingNewMaterialEvent.ShowExitDialog) }
            )
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
                    Spacer(modifier = Modifier.height(40.dp))//bylo 110

                    Text("Uczone słowo", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = DarkBlue)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = state.learnedWord,
                        onValueChange = {
                            viewModel.onEvent(MaterialsCreatingNewMaterialEvent.LearnedWordChanged(it))
                        },
                        label = { Text("Wpisz uczone słowo") },
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .focusRequester(focusRequester),
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                keyboardController?.hide()
                            }
                        ),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            backgroundColor = Color.White,
                            disabledTextColor = Color.DarkGray,
                            disabledBorderColor = Color.Gray,
                            focusedBorderColor = DarkBlue,
                            unfocusedBorderColor = Color.Gray,
                            focusedLabelColor = DarkBlue,
                            unfocusedLabelColor = Color.DarkGray,
                            cursorColor = Color.Black
                        )
                    )


                    Spacer(modifier = Modifier.height(16.dp))

                    // Checkbox: czy zezwolić na edycję "Nazwa zasobu"
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth(0.8f)
                    ) {
                        androidx.compose.material.Checkbox(
                            checked = state.allowEditingResourceName,
                            onCheckedChange = {
                                viewModel.onEvent(MaterialsCreatingNewMaterialEvent.ToggleAllowEditingResourceName(it))
                            },
                            colors = CheckboxDefaults.colors(
                                checkedColor = DarkBlue,
                                uncheckedColor = Color.Gray,
                                checkmarkColor = Color.White
                            )

                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Chcę edytować nazwę materiału", fontSize = 16.sp)
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        "Nazwa materiału",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (state.allowEditingResourceName) DarkBlue else Color.Gray
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = state.resourceName,
                        onValueChange = {
                            if (state.allowEditingResourceName) {
                                viewModel.onEvent(MaterialsCreatingNewMaterialEvent.ResourceNameChanged(it))
                            }
                        },
                        label = { Text("Wpisz nazwę") },
                        enabled = state.allowEditingResourceName,
                        modifier = Modifier.fillMaxWidth(0.8f),
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                keyboardController?.hide()
                            }
                        ),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            backgroundColor = Color.White,
                            disabledTextColor = Color.DarkGray,
                            disabledBorderColor = Color.Gray,
                            focusedBorderColor = DarkBlue,
                            unfocusedBorderColor = Color.Gray,
                            focusedLabelColor = DarkBlue,
                            unfocusedLabelColor = Color.DarkGray,
                            cursorColor = Color.Black
                        )
                    )

                    if (state.showNameConflictDialog) {
                        InfoDialog(
                            show = true,
                            message = "Materiał o tej nazwie już istnieje.",
                            onDismiss = {
                                viewModel.onEvent(MaterialsCreatingNewMaterialEvent.DismissNameConflictDialog)
                            }
                        )
                    }
                    Spacer(modifier = Modifier.height(80.dp))
                    Button(
                        onClick = {
                            viewModel.onEvent(MaterialsCreatingNewMaterialEvent.SaveClicked)
                        },
                        colors = ButtonDefaults.buttonColors(backgroundColor = DarkBlue),
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .height(48.dp)
                    ) {
                        Text("Zapisz", color = Color.White, fontSize = 16.sp)
                    }

                }

                // Prawa kolumna
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth().weight(1f),
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
                                                                    MaterialsCreatingNewMaterialEvent.RequestImageDeletion(image)
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

                    // 🔽 Przyciski na dole
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            onClick = { galleryLauncher.launch("image/*") },
                            colors = ButtonDefaults.buttonColors(backgroundColor = DarkBlue),
                            modifier = Modifier.weight(1f).padding(horizontal = 8.dp).height(48.dp)
                        ) {
                            Text("Z galerii", color = Color.White)
                        }

                        Button(
                            onClick = { cameraLauncher.launch() },
                            colors = ButtonDefaults.buttonColors(backgroundColor = DarkBlue),
                            modifier = Modifier.weight(1f).padding(horizontal = 8.dp).height(48.dp)
                        ) {
                            Text("Zrób zdjęcie", color = Color.White)
                        }
                    }
                }

            }
        }

        if (state.showExitConfirmation) {
            YesNoDialog(
                show = true,
                message = "Czy na pewno chcesz wyjść bez zapisania zmian?",
                onConfirm = {
                    viewModel.onEvent(MaterialsCreatingNewMaterialEvent.DismissExitDialog)
                    onBackClick()
                },
                onDismiss = {
                    viewModel.onEvent(MaterialsCreatingNewMaterialEvent.DismissExitDialog)
                }
            )
        }
        if (state.showDuplicateNameConfirmation) {
            YesNoDialog(
                show = true,
                message = "Materiał o tej nazwie już istnieje. Czy chcesz mimo to zapisać?",
                onConfirm = {
                    viewModel.onEvent(MaterialsCreatingNewMaterialEvent.ConfirmSaveDespiteDuplicate)
                },
                onDismiss = {
                    viewModel.onEvent(MaterialsCreatingNewMaterialEvent.DismissDuplicateNameDialog)
                }
            )
        }
        if (state.imageToConfirmDelete != null) {
            YesNoDialog(
                show = true,
                message = "Czy na pewno chcesz usunąć wybrane zdjęcie?",
                onConfirm = {
                    viewModel.onEvent(MaterialsCreatingNewMaterialEvent.ConfirmImageDeletion)
                },
                onDismiss = {
                    viewModel.onEvent(MaterialsCreatingNewMaterialEvent.CancelImageDeletion)
                }
            )
        }

    }
}
