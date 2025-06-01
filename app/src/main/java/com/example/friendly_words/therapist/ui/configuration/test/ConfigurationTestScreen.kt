package com.example.friendly_words.therapist.ui.configuration.test

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.friendly_words.therapist.ui.components.NumberSelector
import com.example.friendly_words.therapist.ui.theme.DarkBlue


@Composable
fun ConfigurationTestScreen(
    state: ConfigurationTestState,
    onEvent: (ConfigurationTestEvent) -> Unit,
    onBackClick: () -> Unit
) {

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
                    value = state.attemptsCount,
                    onValueChange = { newValue ->
                        onEvent(ConfigurationTestEvent.SetAttemptsCount(newValue))
                    }
                )

                Spacer(modifier = Modifier.height(24.dp))

                NumberSelector(
                    label = "Czas na udzielenie odpowiedzi (sekundy):",
                    minValue = 3,
                    maxValue = 9,
                    value = state.timeCount,
                    onValueChange = { newValue ->
                        onEvent(ConfigurationTestEvent.SetTimeCount(newValue))
                    }
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
                Column(
                    modifier = Modifier
                        .widthIn(max = 450.dp)
                        .background(Color(0xFFF0F0F0), shape = RoundedCornerShape(8.dp))
                        .padding(12.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Informacja",
                        tint = DarkBlue,
                        modifier = Modifier
                            .size(36.dp)
                            .padding(bottom = 8.dp)
                    )

                    Text(
                        text = "W trybie testu dziecko pracuje na materiałach w zakładce MATERIAŁ. " +
                                "W trybie testu nie używa się podpowiedzi i wzmocnień, " +
                                "a terapeuta powinien powstrzymać się od interwencji w interakcje dziecka aż do zakończenia testu.",
                        fontSize = 16.sp,
                        color = Color.Black,
                        lineHeight = 22.sp,
                        textAlign = TextAlign.Start
                    )
                }
            }
        }
    }
}




