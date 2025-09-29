package com.example.friendly_words.child_app.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import coil.compose.AsyncImage
import com.example.friendly_words.child_app.theme.YellowFrames

@Composable
fun ImageOptionBox(
    imagePath: String,
    label: String,
    size: Dp,
    isDimmed: Boolean = false,
    isScaled: Boolean = false,
    animateCorrect: Boolean = false,
    outlineCorrect: Boolean = false,
    showLabel: Boolean = true,
    onClick: () -> Unit = {}
) {
    val framePadding = size * 0.04f
    val contentSize = size - framePadding * 2
    val cornerShape = RoundedCornerShape(framePadding)

    val dimColor = if (isDimmed) Color.Black.copy(alpha = 0.4f) else Color.Transparent

    // Animacje
    val scaleAnim = remember { Animatable(1f) }
    LaunchedEffect(isScaled) {
        if (isScaled) scaleAnim.animateTo(1.10f, tween(2000)) else scaleAnim.snapTo(1f)
    }

    val rotationAnim = remember { Animatable(0f) }
    LaunchedEffect(animateCorrect) {
        if (animateCorrect) {
            while (true) {
                rotationAnim.animateTo(5f, tween(500))
                rotationAnim.animateTo(-5f, tween(500))
            }
        } else rotationAnim.snapTo(0f)
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(size)
            .clickable { onClick() }
            .scale(scaleAnim.value)
            .rotate(rotationAnim.value)
    ) {
        Box(modifier = Modifier.size(size)) {
            Canvas(modifier = Modifier.matchParentSize()) {
                drawRoundRect(
                    color = YellowFrames,
                    size = Size(size.toPx(), size.toPx()),
                    style = Stroke(
                        width = 4.dp.toPx(),
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f))
                    ),
                    cornerRadius = CornerRadius(framePadding.toPx())
                )
                if (outlineCorrect) {
                    drawRoundRect(
                        color = YellowFrames,
                        size = Size(size.toPx(), size.toPx()),
                        style = Stroke(width = 10.dp.toPx()),
                        cornerRadius = CornerRadius(framePadding.toPx())
                    )
                }
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .size(contentSize)
                    .align(Alignment.Center)
                    .clip(cornerShape)
                    .background(Color.White)
            ) {
                AsyncImage(
                    model = imagePath,
                    contentDescription = label,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentScale = ContentScale.Fit
                )

                Spacer(modifier = Modifier.height(contentSize * 0.08f))

                if (showLabel) {
                    Text(
                        text = label,
                        color = Color.Black,
                        fontSize = (contentSize.value * 0.07).sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            if (isDimmed) {
                Box(
                    modifier = Modifier
                        .size(contentSize)
                        .align(Alignment.Center)
                        .clip(cornerShape)
                        .background(dimColor)
                )
            }
        }
    }
}
