package com.example.child_app

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.Dp
import androidx.compose.foundation.Canvas
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PlayArrow
import com.example.child_app.ui.theme.LightBlue
import com.example.child_app.ui.theme.YellowFrames
import androidx.compose.foundation.clickable
import androidx.compose.ui.text.font.FontWeight
import com.example.child_app.ui.theme.Blue

@Composable
fun MainScreen(onPlayClick: () -> Unit) {
    val activeConfiguration = remember {
        mutableStateOf<Pair<String, String>?>(Pair("1 konfiguracja NA STAŁE", "uczenie"))
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Blue),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp),
            modifier = Modifier.fillMaxWidth(0.9f)
        ) {
            Text(
                "Przyjazne Słowa",
                fontSize = 50.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = activeConfiguration.value?.let {
                    "Aktywna konfiguracja: ${it.first} (tryb: ${it.second})"
                } ?: "Brak aktywnej konfiguracji",
                fontSize = 20.sp,
                color = LightBlue
            )
            Spacer(modifier = Modifier.height(40.dp))

            PlayButton(onClick = onPlayClick )
        }
    }
}


@Composable
fun PlayButton(
    onClick: () -> Unit,
    size: Dp = 600.dp,
    outerBorderColor: Color = YellowFrames,
    innerCircleColor: Color = Color.White,
    iconColor: Color = Color(0xFF0B930B)
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(size)
            .clickable(onClick = onClick)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokeWidth = 2.dp.toPx()
            drawCircle(
                color = outerBorderColor,
                style = Stroke(
                    width = strokeWidth,
                    pathEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(
                        floatArrayOf(10f, 10f), 0f
                    ),
                    cap = StrokeCap.Round
                )
            )
        }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(size * 0.95f)
                .clip(CircleShape)
                .background(innerCircleColor)
        ) {
            Icon(
                imageVector = Icons.Rounded.PlayArrow,
                contentDescription = "Play",
                tint = iconColor,
                modifier = Modifier.size(size * 0.7f)
            )
        }
    }
}
