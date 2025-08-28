package com.example.friendly_words.child_app.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.friendly_words.child_app.theme.YellowFrames

@Composable
fun ImageOptionBox(
    imageRes: Int,
    label: String,
    size: Dp,
    isDimmed: Boolean = false,
    isScaled: Boolean = false,
    animateCorrect: Boolean = false,
    outlineCorrect: Boolean = false,
    onClick: () -> Unit = {}
) {
    val dimColor = if (isDimmed) Color.Black.copy(alpha = 0.4f) else Color.Transparent
    val framePadding = 12.dp
    val cornerShape = RoundedCornerShape(24.dp)

    // Animacja powiększenia
    val scaleAnim = remember { Animatable(1f) }

    LaunchedEffect(isScaled) {
        if (isScaled) {
            scaleAnim.animateTo(
                targetValue = 1.10f,
                animationSpec = tween(durationMillis = 2000)
            )
        } else {
            scaleAnim.snapTo(1f) // <-- najważniejszy fix
        }
    }

    // Animacja obracania (kołysanie się)
    val rotationAnim = remember { Animatable(0f) }
    LaunchedEffect(animateCorrect) {
        if (animateCorrect) {
            while (true) {
                rotationAnim.animateTo(5f, tween(500))
                rotationAnim.animateTo(-5f, tween(500))
            }
        } else {
            rotationAnim.snapTo(0f) // reset obracania w nowej rundzie
        }
    }


    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(size + framePadding * 2)
            .clickable { onClick() }
            .scale(scaleAnim.value)
            .rotate(rotationAnim.value)
    ) {
        Box(
            modifier = Modifier.size(size + framePadding * 2)
        ) {
            Canvas(modifier = Modifier.matchParentSize()) {
                drawRoundRect(
                    color = YellowFrames,
                    size = Size(this.size.width, this.size.height),
                    style = Stroke(
                        width = 4.dp.toPx(),
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f))
                    ),
                    cornerRadius = CornerRadius(32.dp.toPx())
                )
                if (outlineCorrect) {
                    drawRoundRect(
                        color = YellowFrames,
                        size = Size(this.size.width, this.size.height),
                        style = Stroke(width = 10.dp.toPx()),
                        cornerRadius = CornerRadius(32.dp.toPx())
                    )
                }
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .size(size)
                    .align(Alignment.Center)
                    .clip(cornerShape)
                    .background(Color.White)
                    .padding(12.dp)
            ) {
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = label,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = label,
                    color = Color.Black,
                    fontSize = (size.value * 0.07).sp,
                    fontWeight = FontWeight.Bold
                )
            }

            if (isDimmed) {
                Box(
                    modifier = Modifier
                        .size(size)
                        .align(Alignment.Center)
                        .clip(cornerShape)
                        .background(dimColor)
                )
            }
        }
    }
}
