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
                backgroundColor = MaterialTheme.colors.primary
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
                Text(
                    text = "Informacje o kroku",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp),
                    textAlign = TextAlign.Center
                )

                Text(text = "TRYB NAUKI", fontWeight = FontWeight.Bold, color = DarkBlue)
                Spacer(modifier = Modifier.height(8.dp))

                InfoLine("Liczba uczonych słów:", "3")
                InfoLine("Uczone słowa:", "misiu, tablet, but")
                InfoLine("Liczba zdjęć wyświetlanych na ekranie:", "3")
                InfoLine("Liczba powtórzeń dla każdego słowa:", "2")
                InfoLine("Rodzaj polecenia:", "{Słowo}")
                InfoLine("Podpisy pod obrazkami:", "Tak")
                InfoLine("Czytanie polecenia:", "Tak")
                InfoLine("Pokaż podpowiedź po:", "5 sekundach")
                InfoLine("Podpowiedzi:", "Obramuj poprawną; Wyszarz niepoprawne")
                InfoLine("Pochwały słowne:", "dobrze, super, świetnie, ekstra, rewelacja, brawo")
                InfoLine("Czytanie głosowe pochwał:", "Tak")

                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "TRYB TESTU", fontWeight = FontWeight.Bold, color = DarkBlue)
                Spacer(modifier = Modifier.height(8.dp))
                InfoLine("Łączna liczba prób:", "2")
                InfoLine("Czas na udzielenie odpowiedzi:", "3 sekundy")

                Spacer(modifier = Modifier.height(72.dp))
            }
        }
    }
}

@Composable
fun InfoLine(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 6.dp)
    ) {
        Text(
            text = label,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            modifier = Modifier.weight(1f),
            //textAlign = TextAlign.End
        )
    }
}
