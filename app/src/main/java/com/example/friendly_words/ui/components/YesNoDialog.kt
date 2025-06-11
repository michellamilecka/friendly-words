package com.example.friendly_words.ui.components
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.friendly_words.ui.theme.DarkBlue

import androidx.compose.ui.text.style.TextAlign

@Composable
fun YesNoDialog(
    show: Boolean,
    message: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    if (show) {
        Dialog(
            onDismissRequest = {
                onDismiss()
            }
        ) {
            Box(
                modifier = Modifier
                    .padding(24.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White, shape = MaterialTheme.shapes.medium)
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = message,
                        fontSize = 24.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 32.dp),
                        textAlign = TextAlign.Center
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            onClick = onConfirm,
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color.White)
                        ) {
                            Text("Tak", fontSize = 20.sp, color = DarkBlue)
                        }

                        Button(
                            onClick = onDismiss,
                            colors = ButtonDefaults.buttonColors(backgroundColor = DarkBlue)
                        ) {
                            Text("Nie", fontSize = 20.sp, color = Color.White)
                        }
                    }
                }
            }
        }
    }
}
