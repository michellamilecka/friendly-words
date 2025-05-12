package com.example.friendly_words

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.friendly_words.ui.components.NumberSelector


@Composable
fun ConfigurationTestScreen(
    onBackClick: () -> Unit
) {
    var attemptsCountTest by remember { mutableStateOf(2) }
    var timeCountTest by remember { mutableStateOf(3) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                NumberSelector(
                    label = "Łączna liczba prób w teście:",
                    minValue = 1,
                    maxValue = 5,
                    initialValue = attemptsCountTest,
                    onValueChange = { newValue -> attemptsCountTest = newValue }
                )

                Spacer(modifier = Modifier.height(24.dp))

                NumberSelector(
                    label = "Czas na udzielenie odpowiedzi (sekundy):",
                    minValue = 3,
                    maxValue = 9,
                    initialValue = timeCountTest,
                    onValueChange = { newValue -> timeCountTest = newValue }
                )
            }

            Spacer(modifier = Modifier.width(32.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "W trybie testu dziecko pracuje na materiałach w zakładce MATERIAŁ. " +
                            "W trybie testu nie używa się podpowiedzi i wzmocnień, " +
                            "a terapeuta powinien powstrzymać się od interwencji w interakcje dziecka aż do zakończenia testu.",
                    fontSize = 16.sp,
                    color = Color.DarkGray,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }
        }
    }
}




