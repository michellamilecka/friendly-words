package com.example.child_app

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.child_app.ui.theme.Blue
import com.example.child_app.ui.theme.LightBlue
import com.example.child_app.ui.components.ImageOptionBox
import kotlinx.coroutines.delay


@Composable
fun GameScreen() {
    var selected by remember { mutableStateOf<String?>(null) }
    var dimOthers by remember { mutableStateOf(false) }

    // Start 5-second timer
    LaunchedEffect(Unit) {
        delay(5000L)
        if (selected == null) {
            dimOthers = true
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Blue),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            Text(
                "Gdzie jest misiu?",
                fontSize = 60.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(80.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(32.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
            ) {
                Spacer(modifier = Modifier.weight(1f))

                ImageOptionBox(
                    imageRes = R.drawable.misiu_1,
                    label = "Misiu",
                    size = 350.dp,
                    isDimmed = false,
                    onClick = { selected = "Misiu" }
                )
                ImageOptionBox(
                    imageRes = R.drawable.kredka_1,
                    label = "Kredka",
                    size = 350.dp,
                    isDimmed = dimOthers,
                    onClick = { selected = "Kredka" }
                )
                ImageOptionBox(
                    imageRes = R.drawable.but_1,
                    label = "But",
                    size = 350.dp,
                    isDimmed = dimOthers,
                    onClick = { selected = "But" }
                )

                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}




