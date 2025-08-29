package com.example.friendly_words.child_app.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.friendly_words.child_app.theme.YellowFrames

@Composable
fun PlayButton(
    onClick: () -> Unit,
    size: Dp = 500.dp,
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
                modifier = Modifier.size(size * 0.75f)
            )
        }
    }
}
