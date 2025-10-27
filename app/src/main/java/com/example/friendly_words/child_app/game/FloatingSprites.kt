package com.example.friendly_words.child_app.game

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlin.random.Random

enum class TravelDirection { UP, DOWN, LEFT, RIGHT }

@Composable
fun FloatingSprite(
    @DrawableRes resId: Int,
    direction: TravelDirection = TravelDirection.UP,
    startDelayMs: Int = 0,
    travelMs: Int = 2500,
    rotate: Boolean = true,
    rotateMs: Int = 40000,
    scale: Float = 1f,
    overshootPx: Float = 300f,
    crossPosPx: Float? = null
) {
    val cfg = LocalConfiguration.current
    val density = androidx.compose.ui.platform.LocalDensity.current
    val W = with(density) { cfg.screenWidthDp.dp.toPx() }
    val H = with(density) { cfg.screenHeightDp.dp.toPx() }

    val fixedCross = remember(crossPosPx) {
        crossPosPx ?: if (direction == TravelDirection.UP || direction == TravelDirection.DOWN)
            Random.nextFloat() * W
        else
            Random.nextFloat() * H
    }

    val main = remember { Animatable(0f) }
    val rotation = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        delay(startDelayMs.toLong())

        main.snapTo(
            when (direction) {
                TravelDirection.UP -> H + overshootPx
                TravelDirection.DOWN -> -overshootPx
                TravelDirection.LEFT -> W + overshootPx
                TravelDirection.RIGHT -> -overshootPx
            }
        )

        main.animateTo(
            when (direction) {
                TravelDirection.UP -> -overshootPx * 2
                TravelDirection.DOWN -> H + overshootPx * 2
                TravelDirection.LEFT -> -overshootPx * 2
                TravelDirection.RIGHT -> W + overshootPx * 2
            },
            animationSpec = tween(durationMillis = travelMs, easing = LinearEasing)
        )

        if (rotate) {
            rotation.animateTo(
                360f,
                animationSpec = tween(durationMillis = rotateMs, easing = LinearEasing)
            )
        }
    }

    val tx = if (direction == TravelDirection.UP || direction == TravelDirection.DOWN) fixedCross else main.value
    val ty = if (direction == TravelDirection.UP || direction == TravelDirection.DOWN) main.value else fixedCross

    Image(
        painter = painterResource(resId),
        contentDescription = null,
        contentScale = ContentScale.Fit,
        modifier = Modifier.graphicsLayer {
            translationX = tx
            translationY = ty
            rotationZ = rotation.value
            scaleX = scale
            scaleY = scale
        }
    )
}

@Composable
fun FloatingSpritesLayer(
    @DrawableRes sprites: List<Int>,
    count: Int = 12,
    direction: TravelDirection = TravelDirection.UP,
    baseTravelMs: Int = 2500,
    rotate: Boolean = true
) {
    if (sprites.isEmpty() || count <= 0) return

    val cfg = LocalConfiguration.current
    val density = androidx.compose.ui.platform.LocalDensity.current
    val W = with(density) { cfg.screenWidthDp.dp.toPx() }
    val H = with(density) { cfg.screenHeightDp.dp.toPx() }

    val spriteWidthPx = with(density) { 80.dp.toPx() }

    val step = (W - spriteWidthPx) / (count - 1)
    val jitter = step / 5f

    val slots = List(count) { i ->
        val base = i * step
        (base + Random.nextFloat() * jitter - jitter / 2f).coerceIn(0f, W - spriteWidthPx)
    }

    repeat(count) { i ->
        val res = sprites[i % sprites.size]
        val travel = baseTravelMs + Random.nextInt(-500, 500)
        val rotateTime = 30000 + Random.nextInt(-5000, 5000)
        val scale = 0.4f + Random.nextFloat() * 0.15f

        FloatingSprite(
            resId = res,
            direction = direction,
            startDelayMs = 0,
            travelMs = travel,
            rotate = rotate,
            rotateMs = rotateTime,
            scale = scale,
            overshootPx = 400f,
            crossPosPx = slots[i]
        )
    }
}

