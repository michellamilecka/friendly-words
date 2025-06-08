package com.example.friendly_words.therapist.ui.configuration.save

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.friendly_words.therapist.ui.theme.DarkBlue
import com.example.friendly_words.therapist.ui.theme.White
import kotlinx.coroutines.launch

@Composable
fun ConfigurationSaveScreen(
    state: ConfigurationSaveState,
    onEvent: (ConfigurationSaveEvent) -> Unit,
    onBackClick: () -> Unit = {}
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar("Konfiguracja została zapisana")
                    }
                },
                backgroundColor = White
            ) {
                Text("Zapisz")
            }
        }
    ) { padding ->
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .weight(0.3f)
                    .fillMaxHeight()
                    .padding(end = 16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally

            ) {
                Text(
                    text = "Nazwa kroku:",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                OutlinedTextField(
                    value = state.stepName,
                    onValueChange = { onEvent(ConfigurationSaveEvent.SetStepName(it)) },
                    label = { Text("Wpisz nazwę kroku") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Column(
                modifier = Modifier
                    .weight(0.7f)
                    .verticalScroll(rememberScrollState())
            ) {

                Row(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Informacje o kroku",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp),
                            textAlign = TextAlign.Left
                        )

                        InfoLabel("Liczba uczonych słów:")
                        InfoLabel("Uczone słowa:")
                        InfoLabel("Liczba wyświetlanych zdjęć:")
                        InfoLabel("Liczba powtórzeń dla każdego słowa:")
                        InfoLabel("Rodzaj polecenia:")
                        InfoLabel("Podpisy pod obrazkami:")
                        InfoLabel("Czytanie polecenia:")
                        InfoLabel("Pokaż podpowiedź po:")
                        InfoLabel("Podpowiedzi:")
                        InfoLabel("Pochwały słowne:")
                        InfoLabel("Czytanie głosowe pochwał:")
                        InfoLabel("Łączna liczba prób:")
                        InfoLabel("Czas na udzielenie odpowiedzi:")
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            "TRYB NAUKI",
                            fontWeight = FontWeight.Bold,
                            color = DarkBlue,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        InfoValue("3")
                        InfoValue("misiu, tablet, but")
                        InfoValue("3")
                        InfoValue("2")
                        InfoValue("{Słowo}")
                        InfoValue("Tak")
                        InfoValue("Tak")
                        InfoValue("5 sekundach")
                        InfoValue("Obramuj poprawną; Wyszarz niepoprawne")
                        InfoValue("dobrze, super, świetnie, ekstra, rewelacja, brawo")
                        InfoValue("Tak")
                        InfoValue("-")
                    }


                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            "TRYB TESTU",
                            fontWeight = FontWeight.Bold,
                            color = DarkBlue,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        InfoValue("-")
                        InfoValue("-")
                        InfoValue("3")
                        InfoValue("-")
                        InfoValue("{Słowo}")
                        InfoValue("Tak")
                        InfoValue("Tak")
                        InfoValue("-")
                        InfoValue("-")
                        InfoValue("-")
                        InfoValue("-")
                        InfoValue("2")
                        InfoValue("3 sekundy")
                    }
                }

                Spacer(modifier = Modifier.height(72.dp))
            }

        }
    }
}


@Composable
fun InfoLabel(label: String) {
    Text(
        text = label,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        modifier = Modifier
            .padding(vertical = 4.dp)
    )
}

@Composable
fun InfoValue(value: String) {
    Text(
        text = value,
        fontSize = 16.sp,
        modifier = Modifier
            .padding(vertical = 4.dp)
    )
}

