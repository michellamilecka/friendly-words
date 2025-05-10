package com.example.friendly_words.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.friendly_words.ui.theme.DarkBlue

@Composable
fun NumberSelector(
    label: String,
    minValue: Int,
    maxValue: Int,
    initialValue: Int = minValue,
    onValueChange: (Int) -> Unit = {}
) {
    var value by remember { mutableStateOf(initialValue.coerceIn(minValue, maxValue)) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = label,
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black,
            textAlign = TextAlign.Center
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = {
                    if (value > minValue) {
                        value--
                        onValueChange(value)
                    }
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = DarkBlue)
            ) {
                Text("-", fontSize = 24.sp, color = Color.White)
            }

            Spacer(modifier = Modifier.width(24.dp))

            Text(
                text = value.toString(),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.width(24.dp))

            Button(
                onClick = {
                    if (value < maxValue) {
                        value++
                        onValueChange(value)
                    }
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = DarkBlue)
            ) {
                Text("+", fontSize = 24.sp, color = Color.White)
            }
        }
    }
}
